package lombok.javac.handlers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.WritableValue;
import javafx.css.StyleableProperty;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.code.Type.TypeVar;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.core.AST.Kind;
import lombok.core.AnnotationValues;
import lombok.core.handlers.HandlerUtil;
import lombok.experimental.Accessors;
import lombok.experimental.FXProperty;
import lombok.javac.Javac;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import org.mangosdk.spi.ProviderFor;
import static lombok.javac.Javac.CTC_VOID;
import static lombok.javac.handlers.JavacHandlerUtil.deleteAnnotationIfNeccessary;
import static lombok.javac.handlers.JavacHandlerUtil.injectMethod;
import static lombok.javac.handlers.JavacHandlerUtil.shouldReturnThis;
import static lombok.javac.handlers.JavacHandlerUtil.cloneSelfType;
import static lombok.javac.handlers.JavacHandlerUtil.toSetterName;
import static lombok.javac.handlers.JavacHandlerUtil.toAllSetterNames;
import static lombok.javac.handlers.JavacHandlerUtil.recursiveSetGeneratedBy;
import static lombok.javac.handlers.JavacHandlerUtil.methodExists;
import static lombok.javac.handlers.JavacHandlerUtil.toJavacModifier;
import static lombok.javac.handlers.JavacHandlerUtil.chainDots;
import static lombok.javac.handlers.JavacHandlerUtil.annotationTypeMatches;
import static lombok.javac.handlers.JavacHandlerUtil.getAccessorsForField;


@ProviderFor(JavacAnnotationHandler.class)
public class HandleFXProperty extends JavacAnnotationHandler<FXProperty> {

	/**
	 * For parameterized class builds map VarTypes to corresponded ClassTypes they were set.
	 * f.e. for Property<Integer> will produced E to Integer map Entry.
	 */
	final private static class TypeParameterMap {
		private Map<Name, Type> parameterMap = new HashMap<Name, Type>();

		public void mapParameters(ClassType type) {
			if (!type.isParameterized()) return;

			List<Type> parameters = type.typarams_field;		// extract parameterized types (f.e.: String, Integer, ot E ...)
			List<Type> parametersVar = ((ClassType) type.tsym.type).typarams_field; // extract given types name (f.e.: E, T ...)
			Map<Name, Type> map = new HashMap<Name, Type>();

			if (parameters.length() == parametersVar.length()) {
				for (int i = 0; i < parameters.length(); i++) {
					Name varName = parametersVar.get(i).tsym.getSimpleName();
					if (parameters.get(i) instanceof TypeVar) {							// if type parameter isn't class instance, look into the stored parameterMap for its ClassType
						Name parameterName = parameters.get(i).tsym.getSimpleName();
						if (parameterMap.containsKey(parameterName)) {
							map.put(varName, parameterMap.get(parameterName));
							continue;
						}
					}
					map.put(varName, parameters.get(i));
				}
			}

			parameterMap = map;
		}

		public List<Type> convertToClassType(List<Type> types) {		// tries to get corresponding ClassTypes that were set to corresponded VarTypes
			ListBuffer<Type> list = new ListBuffer<Type>();
			for (Type type : types) {
				Name key = type.tsym.getSimpleName();
				if (parameterMap.containsKey(key)) {
					list.add(parameterMap.get(key));
				}
			}
			return list.toList();
		}

		public Type getParameterType(Type type) {
			return parameterMap.get(type.tsym.getSimpleName());
		}
	}

	@Override
	public void handle(AnnotationValues<FXProperty> annotation, JCAnnotation ast, JavacNode annotationNode) {
		deleteAnnotationIfNeccessary(annotationNode, FXProperty.class);
		JavacNode fieldNode = annotationNode.up();
		if (fieldNode == null || fieldNode.getKind() != Kind.FIELD) {
			annotationNode.addError("@FXProperty is only supported on a field.");
			return;
		}
		if (!isProperty(fieldNode)) {
			annotationNode.addError("@FXProperty is only supported on a FXProperties.");
			return;
		}
		if (isGetterAnnotationPresent(fieldNode)) {
			annotationNode.addError("@FXProperty isn't compatible with @Getter");
			return;
		}
		AccessLevel level = annotation.getInstance().value();
		createGetterForProperty(fieldNode, annotationNode, level);
		if (isInheritedFromClass(Types.instance(fieldNode.getContext()), ((JCVariableDecl) fieldNode.get()).vartype.type, WritableValue.class.getName())) {
			createSetterForPropertyValue(fieldNode, annotationNode, level);
		}
		createGetterForPropertyValue(fieldNode, annotationNode, level);
	}

	public void createGetterForPropertyValue(JavacNode field, JavacNode annotationNode, AccessLevel level) {
		JavacTreeMaker treeMaker = field.getTreeMaker();
		String delegatingMethodName = "getValue";
		JCExpression methodType = treeMaker.Type(findType(field, delegatingMethodName));
		String methodName = toGetterName(field, methodType);

		if (methodName == null) {
			annotationNode.addWarning("Not generating getter for this field: It does not fit your @Accessors prefix list.");
			return;
		}

		if (methodExist(field, annotationNode, methodName, toAllGetterNames(field, methodType))) return;

		List<JCStatement> statements = createMethodBody(treeMaker, field, delegatingMethodName);
		JCMethodDecl methodDecl = createMethodDecl(level, field, methodType, methodName, statements, List.<JCVariableDecl>nil());
		if (methodDecl == null) return;

		injectMethod(field.up(), methodDecl);
	}

	public void createGetterForProperty(JavacNode field, JavacNode annotationNode, AccessLevel level) {
		JCVariableDecl fieldDecl = (JCVariableDecl) field.get();
		JavacTreeMaker treeMaker = field.getTreeMaker();
		HandleGetter handleGetter = new HandleGetter();

		JCExpression methodType = handleGetter.copyType(treeMaker, fieldDecl);
		String methodName = makePropertyName(field) + "Property";

		if (methodName == null) {
			annotationNode.addWarning("Not generating getter for this field: It does not fit your @Accessors prefix list.");
			return;
		}

		if (methodExist(field, annotationNode, methodName, List.of(methodName))) return;

		List<JCStatement> statements = handleGetter.createSimpleGetterBody(treeMaker, field);
		JCMethodDecl methodDecl = createMethodDecl(level, field, methodType, methodName, statements, List.<JCVariableDecl>nil());
		if (methodDecl == null) return;

		injectMethod(field.up(), methodDecl);
	}

	public void createSetterForPropertyValue(JavacNode field, JavacNode annotationNode, AccessLevel level) {
		JavacTreeMaker treeMaker = field.getTreeMaker();
		boolean returnThis = shouldReturnThis(field);

		String delegatingMethodName = "setValue";
		JCExpression methodType = returnThis ? cloneSelfType(field) : treeMaker.Type(Javac.createVoidType(treeMaker, CTC_VOID));
		String methodName = toSetterName(field);

		if (methodName == null) {
			annotationNode.addWarning("Not generating setter for this field: It does not fit your @Accessors prefix list.");
			return;
		}

		if (methodExist(field, annotationNode, methodName, toAllSetterNames(field))) return;

		Name argName = annotationNode.toName(makePropertyName(field));
		List<JCExpression> args = List.<JCExpression>of(treeMaker.Ident(argName));
		JCExpression expression = treeMaker.Apply(List.<JCExpression>nil(), JavacHandlerUtil.chainDots(field, "this", field.getName(), delegatingMethodName), args);
		ListBuffer<JCStatement> statements = new ListBuffer<JCStatement>().append(treeMaker.Exec(expression));

		if (returnThis) {
			JCReturn returnStatement = treeMaker.Return(treeMaker.Ident(field.toName("this")));
			statements.append(returnStatement);
		}

		long flags = JavacHandlerUtil.addFinalIfNeeded(Flags.PARAMETER, annotationNode.getContext());
		JCVariableDecl param = treeMaker.VarDef(treeMaker.Modifiers(flags), argName, treeMaker.Type(findType(field, "getValue")), null);

		JCMethodDecl methodDecl = createMethodDecl(level, field, methodType, methodName, statements.toList(), List.of(param));
		if (methodDecl == null) return;

		injectMethod(field.up(), recursiveSetGeneratedBy(methodDecl, annotationNode.get(), annotationNode.getContext()));
	}

	public boolean methodExist(JavacNode fieldNode, JavacNode annotationNode, String methodName, java.util.List<String> altNameList) {

		for (String altName : altNameList) {
			switch (methodExists(altName, fieldNode, false, 0)) {
				case EXISTS_BY_LOMBOK:
					return true;
				case EXISTS_BY_USER:
					String altNameExpl = "";
					if (!altName.equals(methodName)) altNameExpl = String.format(" (%s)", altName);
					annotationNode.addWarning(
							String.format("Not generating %s(): A method with that name already exists%s", methodName, altNameExpl));
					return true;
			}
		}

		return false;
	}

	public JCMethodDecl createMethodDecl(AccessLevel level, JavacNode field, JCExpression methodType, String methodName, List<JCStatement> statements, List<JCVariableDecl> parameters) {
		JavacTreeMaker treeMaker = field.getTreeMaker();
		return treeMaker.MethodDef(field.getTreeMaker().Modifiers(toJavacModifier(level)),
									field.toName(methodName),
									methodType,
									List.<JCTypeParameter>nil(),
									parameters,
									List.<JCExpression>nil(),
									treeMaker.Block(0, statements),
									null);
	}

	public List<JCStatement> createMethodBody(JavacTreeMaker treeMaker, JavacNode field, String methodName) {
		JCExpression expression = treeMaker.Apply(List.<JCExpression>nil(), chainDots(field, field.getName(), methodName), List.<JCExpression>nil());
		return List.<JCStatement>of(treeMaker.Return(expression));
	}

	public Type findType(JavacNode javacNode, String methodName) {	// tries to find type for method if it is implemented by node or its ancestors
		Type resType;
		Types types = Types.instance(javacNode.getContext());
		JCVariableDecl variableDecl = (JCVariableDecl) javacNode.get();
		TypeParameterMap map = new TypeParameterMap();
		ClassType currentType = (ClassType) variableDecl.sym.type;

		while (currentType != null) {
			for (Symbol sym : currentType.tsym.getEnclosedElements()) {
				if (sym.getSimpleName().toString().equals(methodName)) {
					MethodType methodType = sym.asType().asMethodType();
					if (methodType.getTypeArguments().length() != 0) continue;		// parameters number should be zero for this method

					resType = methodType.restype;
					if (resType instanceof TypeVar) {
						resType = map.getParameterType(resType);
						if (resType == null) return methodType.restype;
					}

					if (resType instanceof TypeVar) return resType;					// if still has no ClassType parameter return the row TypeVar

					if (resType.isParameterized()) {
						List<Type> parameters = map.convertToClassType(((ClassType) resType.tsym.type).typarams_field);
						return new ClassType(resType.getEnclosingType(), parameters, resType.tsym);
					}

					return new ClassType(resType.getEnclosingType(), List.<Type>nil(), resType.tsym);
				}
			}

			currentType = (ClassType) types.supertype(currentType);
			if (currentType.isParameterized()) map.mapParameters(currentType);		// store map types to don't loose parameters type
		}

		return null;
	}

	public boolean isBooleanClass(JCExpression methodType) {
		return methodType != null && methodType.toString().equals(Boolean.class.getName());
	}

	public boolean isGetterAnnotationPresent(JavacNode field) {
		for (JavacNode node : field.down()) {
			if (annotationTypeMatches(Getter.class, node)) return true;
		}
		return false;
	}

	public boolean isProperty(JavacNode node) {
		Types typeUtil = Types.instance(node.getContext());
		JCVariableDecl variableDecl = (JCVariableDecl) node.get();

		return isInheritedFromClass(typeUtil, variableDecl.vartype.type, ReadOnlyProperty.class.getName()) ||
				isInheritedFromClass(typeUtil, variableDecl.vartype.type, StyleableProperty.class.getName());
	}

	public boolean isInheritedFromClass(Types typesUtil, Type type, String clazz) {
		if (implementsInterface(typesUtil, type, clazz)) return true;

		Type superType = typesUtil.supertype(type);
		return superType.tsym != null
				&& (superType.tsym.flatName().toString().equals(clazz)
						|| implementsInterface(typesUtil, type, clazz)
						|| isInheritedFromClass(typesUtil, superType, clazz));
	}

	public boolean implementsInterface(Types typesUtil, Type type, String clazz) {
		for (Type interfaceType : typesUtil.interfaces(type)) {
			if (interfaceType.tsym.flatName().toString().equals(clazz)) return true;

			if (implementsInterface(typesUtil, interfaceType, clazz)) return true;
		}
		return false;
	}

	private java.util.List<String> toAllGetterNames(JavacNode field, JCExpression methodType) {
		return HandlerUtil.toAllGetterNames(field.getAst(), getAccessorsForField(field), field.getName(), isBooleanClass(methodType));
	}

	private String toGetterName(JavacNode field, JCExpression methodType) {
		return HandlerUtil.toGetterName(field.getAst(), getAccessorsForField(field), field.getName(), isBooleanClass(methodType));
	}

	private String makePropertyName(JavacNode field) {
		AnnotationValues<Accessors> accessors = JavacHandlerUtil.getAccessorsForField(field);
		if (accessors == null || !accessors.isExplicit("prefix")) return field.getName();

		java.util.List<String> prefix = Arrays.asList(accessors.getInstance().prefix());
		CharSequence name = HandlerUtil.removePrefix(field.getName(), prefix);
		return name == null ? field.getName() : name.toString();
	}
}