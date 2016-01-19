package lombok.javac.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.code.Type.TypeVar;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import lombok.AccessLevel;
import lombok.core.AST.Kind;
import lombok.core.AnnotationValues;
import lombok.core.handlers.HandlerUtil;
import lombok.experimental.FXProperty;
import lombok.javac.Javac;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import org.mangosdk.spi.ProviderFor;
import static lombok.javac.Javac.CTC_VOID;
import static lombok.javac.handlers.JavacHandlerUtil.*;


@ProviderFor(JavacAnnotationHandler.class)
public class HandleFXProperty extends JavacAnnotationHandler<FXProperty> {

	final private static class TypeParameterMap {
		private Map<Name, Type> parameterMap = new HashMap<Name, Type>();

		public void mapParameters(ClassType type) {
			List<Type> parameters = type.typarams_field;
			List<Type> parametersVar = ((ClassType) type.tsym.type).typarams_field;

			if (parameters.length() == parametersVar.length()) {
				for (int i = 0; i < parameters.length(); i++) {
					Name parameterName = parametersVar.get(i).tsym.getSimpleName();
					if (!parameterMap.containsKey(parameterName)) {
						parameterMap.put(parameterName, parameters.get(i));
					}
				}
			}
		}

		public List<Type> getParametersType(List<Type> types) {
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
		if (fieldNode == null) return;

		if (fieldNode.getKind() != Kind.FIELD) {
			annotationNode.addError("@Getter is only supported on a field.");
			return;
		}

		FXProperty annotationInstance = annotation.getInstance();
		AccessLevel level = annotationInstance.value();

		long localDecl = ((JCTree.JCVariableDecl) fieldNode.get()).mods.flags;
		if (annotationInstance.makeFinal()) {
			if ((localDecl & Flags.FINAL) == 0) {
				((JCTree.JCVariableDecl) fieldNode.get()).mods.flags |= Flags.FINAL;
				fieldNode.rebuild();
			}
		} else {
			if ((localDecl & Flags.FINAL) != 0) {
				annotationNode.addWarning("final in field is more prioritize than in @Getter field");		// TODO change sentence
			}
		}

//		createGetterForPropertyValue(fieldNode, annotationNode, level);
//		createGetterForProperty(fieldNode, annotationNode, level);
		createSetterForPropertyValue(fieldNode, annotationNode, level, true);
	}

	public void createGetterForPropertyValue(JavacNode field, JavacNode annotationNode, AccessLevel level) {
		JCVariableDecl fieldDecl = (JCVariableDecl) field.get();
		JavacTreeMaker treeMaker = field.getTreeMaker();

		String delegatingMethodName = "getValue";
		JCExpression methodType = treeMaker.Type(findType(fieldDecl, delegatingMethodName));
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

	public void createSetterForPropertyValue(JavacNode field, JavacNode annotationNode, AccessLevel level, boolean returnThis) {
		JCVariableDecl fieldDecl = (JCVariableDecl) field.get();
		JavacTreeMaker treeMaker = field.getTreeMaker();

		String delegatingMethodName = "setValue";
		JCExpression methodType = returnThis ? cloneSelfType(field) : treeMaker.Type(Javac.createVoidType(treeMaker, CTC_VOID));
		String methodName = toSetterName(field);

		if (methodName == null) {
			annotationNode.addWarning("Not generating getter for this field: It does not fit your @Accessors prefix list.");
			return;
		}

		if (methodExist(field, annotationNode, methodName, toAllSetterNames(field))) return;

		Type type = findMethod(fieldDecl, delegatingMethodName).type;
		List<Type> types = type.getParameterTypes();
		List<JCExpression> args = List.of(treeMaker.Type(types.head));
		JCExpression expression = treeMaker.Apply(List.<JCExpression>nil(), JavacHandlerUtil.chainDots(field, field.getName(), delegatingMethodName), args);
		ListBuffer<JCStatement> statements = new ListBuffer<JCStatement>().append(treeMaker.Exec(expression));

		if (returnThis) {
			JCReturn returnStatement = treeMaker.Return(treeMaker.Ident(field.toName("this")));
			statements.append(returnStatement);
		}

		long flags = JavacHandlerUtil.addFinalIfNeeded(Flags.PARAMETER, field.getContext());
		JCVariableDecl param = treeMaker.VarDef(treeMaker.Modifiers(flags), fieldDecl.name, treeMaker.Type(findType(fieldDecl, "getValue")), null);

//		JCMethodDecl methodDecl = createMethodDecl(level, field, methodType, methodName, statements, List.of(param));
		JCMethodDecl methodDecl = createMethodDecl(level, field, methodType, methodName, statements.toList(), List.<JCVariableDecl>nil());
		if (methodDecl == null) return;

		injectMethod(field.up(), methodDecl);
	}

	public void createGetterForProperty(JavacNode field, JavacNode annotationNode, AccessLevel level) {
		JCVariableDecl fieldDecl = (JCVariableDecl) field.get();
		JavacTreeMaker treeMaker = field.getTreeMaker();
		HandleGetter handleGetter = new HandleGetter();

		JCExpression methodType = handleGetter.copyType(treeMaker, fieldDecl);
		String methodName = field.getName() + "Property";

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
		List<JCTypeParameter> methodGenericParams = List.nil();
		List<JCExpression> throwsClauses = List.nil();
		JCBlock methodBody = treeMaker.Block(0, statements);

		JCMethodDecl decl = treeMaker.MethodDef(treeMaker.Modifiers(toJavacModifier(level)),
									field.toName(methodName),
									methodType,
									methodGenericParams,
									parameters,
									throwsClauses,
									methodBody,
									null);

		return decl;
	}

	public List<JCStatement> createMethodBody(JavacTreeMaker treeMaker, JavacNode field, String methodName) {

		JCExpression expression = treeMaker.Apply(List.<JCExpression>nil(),
				chainDots(field, field.getName(), methodName), List.<JCExpression>nil());
		return List.<JCStatement>of(treeMaker.Return(expression));
	}

	public Type findType(JCVariableDecl fieldNode, String methodName) {
		Type resType;
		TypeParameterMap map = new TypeParameterMap();

		ClassType currentType = (ClassType) fieldNode.sym.type;
		if (currentType.isParameterized()) {
			map.mapParameters(currentType);
			currentType = takeSuperType(currentType);
		} else {
			currentType = (ClassType) currentType.supertype_field;
		}

		while (currentType != null) {
			for (Symbol sym : currentType.tsym.getEnclosedElements()) {
				if (sym.getSimpleName().toString().equals(methodName)) {
					resType = sym.asType().asMethodType().restype;

					if (resType instanceof TypeVar) {
						resType = map.getParameterType(resType);
					}

					if (resType.isParameterized()) {
						List<Type> parameters = map.getParametersType(((ClassType) resType.tsym.type).typarams_field);
						ClassType type = new ClassType(resType, parameters, resType.tsym);
						return type;
					}

					return resType;
				}
			}

			currentType = takeSuperType(currentType);
			if (currentType.isParameterized()) {
				map.mapParameters(currentType);
			}
		}

		return null;
	}

	public Symbol findMethod(JCVariableDecl fieldNode, String methodName) {

		ClassType currentType = (ClassType) fieldNode.sym.type;
		if (currentType.isParameterized()) {
			currentType = takeSuperType(currentType);
		} else {
			currentType = (ClassType) currentType.supertype_field;
		}

		while (currentType != null) {
			for (Symbol sym : currentType.tsym.getEnclosedElements()) {
				if (sym.getSimpleName().toString().equals(methodName)) {
					return sym;
				}
			}

			currentType = takeSuperType(currentType);
		}

		return null;
	}

	private ClassType takeSuperType(ClassType type) {
		return (ClassType)((ClassType) type.tsym.type).supertype_field;
	}

	private boolean isBooleanClass(JCExpression methodType) {
		return methodType != null && methodType.toString().equals("java.lang.Boolean");
	}

	private java.util.List<String> toAllGetterNames(JavacNode field, JCExpression methodType) {
		return HandlerUtil.toAllGetterNames(field.getAst(), getAccessorsForField(field), field.getName(), isBooleanClass(methodType));
	}

	private String toGetterName(JavacNode field, JCExpression methodType) {
		return HandlerUtil.toGetterName(field.getAst(), getAccessorsForField(field), field.getName(), isBooleanClass(methodType));
	}
}