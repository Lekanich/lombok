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
import lombok.core.HandlerPriority;
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


/**
 * Handles the {@code lombok.experimental.FXProperty} annotation for javac.
 */
@ProviderFor(JavacAnnotationHandler.class)
@HandlerPriority(-512)	// has to run before @Getter and @Data
public class HandleFXProperty extends JavacAnnotationHandler<FXProperty> {
	private static final String GET_VALUE = "getValue";
	private static final String SET_VALUE = "setValue";
	private static final String PROPERTY = "Property";
	private static final String PREFIX = "prefix";

	/**
	 * For parameterized class builds map VarTypes to corresponded ClassTypes they were set.
	 * f.e.: for Property<Integer> will produced T to Integer map Entry.
	 */
	final private static class TypeParameterMap {
		private Map<Name, Type> parameterMap = new HashMap<Name, Type>();

		public void mapParameters(ClassType type) {
			if (!type.isParameterized()) return;

		// init
			List<Type> parameters = type.typarams_field;									// extract parameterized types (f.e.: String, Integer, or E ...)
			List<Type> parametersVar = ((ClassType) type.tsym.type).typarams_field;			// extract given types name (f.e.: E, T ...)
			Map<Name, Type> map = new HashMap<Name, Type>();

		// maps VarType to its Type that was set
			if (parameters.length() == parametersVar.length()) {
				for (int i = 0; i < parameters.length(); i++) {
					Name varName = parametersVar.get(i).tsym.getSimpleName();

				// -- if type parameter isn't class instance, look into the stored parameterMap for its ClassType
					if (parameters.get(i) instanceof TypeVar) {
						Name parameterName = parameters.get(i).tsym.getSimpleName();
						if (parameterMap.containsKey(parameterName)) {
							map.put(varName, parameterMap.get(parameterName));
							continue;
						}
					}
					map.put(varName, parameters.get(i));
				}
			}

		// store new map
			parameterMap = map;
		}

		/**
		 * Tries to get ClassTypes that were set to corresponded VarTypes.
		 */
		public List<Type> convertToClassType(List<Type> types) {
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
		AccessLevel accessLevel = annotation.getInstance().value();
		if (accessLevel == AccessLevel.NONE) return;

		JavacTreeMaker treeMaker = fieldNode.getTreeMaker();
		long level = toJavacModifier(accessLevel) | (((JCVariableDecl) fieldNode.get()).mods.flags & Flags.STATIC);
		createGetterForProperty(fieldNode, annotationNode, treeMaker, level);
		if (isInheritedFromClass(Types.instance(fieldNode.getContext()), ((JCVariableDecl) fieldNode.get()).vartype.type, WritableValue.class.getName())) {
			createSetterForPropertyValue(fieldNode, annotationNode, treeMaker, level);
		}
		createGetterForPropertyValue(fieldNode, annotationNode, treeMaker, level);
	}

	/**
	 * Generates getter for property, calling getValue() implementation of property field
	 */
	public void createGetterForPropertyValue(JavacNode field, JavacNode annotationNode, JavacTreeMaker treeMaker, long level) {
	// init
		JCExpression methodType = treeMaker.Type(findMethodType(field, GET_VALUE));
		String methodName = toGetterName(field, methodType);

	// check if method name hasn't generated or method with such signature exist
		if (methodName == null) {
			annotationNode.addWarning("Not generating getter for this field: It does not fit your @Accessors prefix list.");
			return;
		}
		if (methodExist(field, annotationNode, methodName, toAllGetterNames(field, methodType))) return;

	// define method body and tries to build its declaration
		JCExpression expression = treeMaker.Apply(List.<JCExpression>nil(), chainDots(field, field.getName(), GET_VALUE), List.<JCExpression>nil());
		List<JCStatement> statements = List.<JCStatement>of(treeMaker.Return(expression));
		JCMethodDecl methodDecl = createMethodDecl(level, field, methodType, methodName, statements, List.<JCVariableDecl>nil());
		if (methodDecl == null) return;

	// create method if its declaration building was successful
		injectMethod(field.up(), methodDecl);
	}

	/**
	 * Returns property field itself
	 */
	public void createGetterForProperty(JavacNode field, JavacNode annotationNode, JavacTreeMaker treeMaker, long level) {
	// init
		HandleGetter handleGetter = new HandleGetter();
		JCExpression methodType = handleGetter.copyType(treeMaker, (JCVariableDecl) field.get());
		String methodName = makePropertyName(field);

	// check if method with such signature exist or can generate method name
		if (methodName == null) {
			annotationNode.addWarning("Not generating accessor for this field: It does not fit your @Accessors prefix list.");
			return;
		}
		methodName += PROPERTY;
		if (methodExist(field, annotationNode, methodName, List.of(methodName))) return;

	// define method body and tries to build its declaration
		List<JCStatement> statements = handleGetter.createSimpleGetterBody(treeMaker, field);
		JCMethodDecl methodDecl = createMethodDecl(level, field, methodType, methodName, statements, List.<JCVariableDecl>nil());
		if (methodDecl == null) return;

	// create method if its declaration building was successful
		injectMethod(field.up(), methodDecl);
	}

	/**
	 * Generates getter for property, calling setValue(type propertyName) implementation of property field
	 * where the type is the type returned by getter
	 */
	public void createSetterForPropertyValue(JavacNode field, JavacNode annotationNode, JavacTreeMaker treeMaker, long level) {
	// init
		boolean returnThis = shouldReturnThis(field);
		boolean isStatic = (((JCVariableDecl) field.get()).mods.flags & Flags.STATIC) != 0;
		JCExpression methodType = returnThis ? cloneSelfType(field) : treeMaker.Type(Javac.createVoidType(treeMaker, CTC_VOID));
		String methodName = toSetterName(field);

	// checks if method name hasn't generated or method with such signature exist
		if (methodName == null) {
			annotationNode.addWarning("Not generating setter for this field: It does not fit your @Accessors prefix list.");
			return;
		}
		if (methodExist(field, annotationNode, methodName, toAllSetterNames(field))) return;

	// creates expressions, their types and args for method body
		Name argName = annotationNode.toName(makePropertyName(field));
		List<JCExpression> args = List.<JCExpression>of(treeMaker.Ident(argName));
		String receiver = isStatic ? field.up().getName() : "this";
		JCExpression expression = treeMaker.Apply(List.<JCExpression>nil(), JavacHandlerUtil.chainDots(field, receiver, field.getName(), SET_VALUE), args);
		ListBuffer<JCStatement> statements = new ListBuffer<JCStatement>().append(treeMaker.Exec(expression));
		if (returnThis && !isStatic) {
			JCReturn returnStatement = treeMaker.Return(treeMaker.Ident(field.toName("this")));
			statements.append(returnStatement);
		}

	// define method body, its modifiers and parameters, and tries to build its declaration
		long flags = JavacHandlerUtil.addFinalIfNeeded(Flags.PARAMETER, annotationNode.getContext());
		JCVariableDecl param = treeMaker.VarDef(treeMaker.Modifiers(flags), argName, treeMaker.Type(findMethodType(field, GET_VALUE)), null);
		JCMethodDecl methodDecl = createMethodDecl(level, field, methodType, methodName, statements.toList(), List.of(param));
		if (methodDecl == null) return;

	// creates method if its method declaration building was successful
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

					annotationNode.addWarning(String.format("Not generating %s(): A method with that name already exists%s", methodName, altNameExpl));
					return true;
			}
		}

		return false;
	}

	public JCMethodDecl createMethodDecl(long level, JavacNode field, JCExpression methodType, String methodName, List<JCStatement> statements, List<JCVariableDecl> parameters) {
		JavacTreeMaker treeMaker = field.getTreeMaker();
		return treeMaker.MethodDef(treeMaker.Modifiers(level), field.toName(methodName), methodType, List.<JCTypeParameter>nil(), parameters, List.<JCExpression>nil(), treeMaker.Block(0, statements), null);
	}

	/**
	 * Tries to find type for method with 0 args if it is implemented by node or its ancestors
	 */
	private Type findMethodType(JavacNode javacNode, String methodName) {
	// init
		Type resType;
		Types types = Types.instance(javacNode.getContext());
		JCVariableDecl variableDecl = (JCVariableDecl) javacNode.get();
		TypeParameterMap map = new TypeParameterMap();
		ClassType currentType = (ClassType) variableDecl.sym.type;

	// search while parent not null
		while (currentType != null) {
			for (Symbol sym : currentType.tsym.getEnclosedElements()) {
				if (!sym.getSimpleName().toString().equals(methodName)) continue;

			// -- gets methods and checks its parameters number
				MethodType methodType = sym.asType().asMethodType();
				if (methodType.getTypeArguments().length() != 0) continue;

			// -- tries to eject TypeVar Type, if can't return raw
				resType = methodType.restype;
				if (resType instanceof TypeVar) {
					resType = map.getParameterType(resType);
					if (resType == null) return methodType.restype;
				}
				if (resType instanceof TypeVar) return resType;						// if ejected Type still has no ClassType parameter return the row TypeVar

			// -- if Type parameterized ejects its parameters
				if (resType.isParameterized()) {
					List<Type> parameters = map.convertToClassType(((ClassType) resType.tsym.type).typarams_field);
					return new ClassType(resType.getEnclosingType(), parameters, resType.tsym);
				}

			// -- builds type having its ClassType and Parameters
				return new ClassType(resType.getEnclosingType(), List.<Type>nil(), resType.tsym);
			}

		// - stores map types to don't loose parameters type
			currentType = (ClassType) types.supertype(currentType);
			if (currentType.isParameterized()) map.mapParameters(currentType);
		}

	// if type wasn't found returns null
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
		return superType.tsym != null && (superType.tsym.flatName().toString().equals(clazz)
						|| implementsInterface(typesUtil, type, clazz)
						|| isInheritedFromClass(typesUtil, superType, clazz));
	}

	public boolean implementsInterface(Types typesUtil, Type type, String clazz) {
		for (Type interfaceType : typesUtil.interfaces(type)) {
			if (interfaceType.tsym.flatName().toString().equals(clazz) || implementsInterface(typesUtil, interfaceType, clazz)) return true;
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
		if (accessors == null || !accessors.isExplicit(PREFIX)) return field.getName();

		java.util.List<String> prefix = Arrays.asList(accessors.getInstance().prefix());
		CharSequence name = HandlerUtil.removePrefix(field.getName(), prefix);
		return name == null ? null : name.toString();
	}
}