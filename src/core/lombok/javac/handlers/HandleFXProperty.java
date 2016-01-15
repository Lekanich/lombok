package lombok.javac.handlers;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.model.JavacTypes;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import lombok.AccessLevel;
import lombok.core.AST.Kind;
import lombok.core.AnnotationValues;
import lombok.experimental.FXProperty;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import org.mangosdk.spi.ProviderFor;
import static lombok.javac.handlers.JavacHandlerUtil.deleteAnnotationIfNeccessary;
import static lombok.javac.handlers.JavacHandlerUtil.injectMethod;
import static lombok.javac.handlers.JavacHandlerUtil.methodExists;
import static lombok.javac.handlers.JavacHandlerUtil.toAllGetterNames;
import static lombok.javac.handlers.JavacHandlerUtil.toGetterName;


@ProviderFor(JavacAnnotationHandler.class)
public class HandleFXProperty extends JavacAnnotationHandler<FXProperty> {

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

		createGetterForPropertyValue(fieldNode, annotationNode, level);
		createAccessor(fieldNode, annotationNode, level, fieldNode.getName() + "Property");
//		createGetterForProperty(node, annotationNode, level);
	}

	public void createAccessor(JavacNode fieldNode, JavacNode annotationNode, AccessLevel level, String accesorName) {
		if (accesorName == null) {
			annotationNode.addWarning(
					String.format("Not generating %s() for this field: It does not fit your @Accessors prefix list."));
			return;
		}

		if (methodExist(fieldNode, annotationNode, accesorName, new java.util.ArrayList<String>() { })) return;

//		injectMethod(fieldNode.up(), createAccessor(););
	}

	public void createGetterForPropertyValue(JavacNode fieldNode, JavacNode annotationNode, AccessLevel level) {
		String methodName = toGetterName(fieldNode);

		if (methodName == null) {
			annotationNode.addWarning("Not generating getter for this field: It does not fit your @Accessors prefix list.");
			return;
		}

		if (methodExist(fieldNode, annotationNode, methodName, toAllGetterNames(fieldNode))) return;

		injectMethod(fieldNode.up(), createGetter(level, fieldNode, fieldNode.getTreeMaker(), annotationNode));
	}

	public boolean methodExist(JavacNode fieldNode, JavacNode annotationNode, String methodName, java.util.List<String> methodNames) {
		for (String altName : methodNames) {
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

	public JCMethodDecl createGetter(AccessLevel level, JavacNode field, JavacTreeMaker treeMaker, JavacNode annotationNode) {
		JCVariableDecl fieldNode = (JCVariableDecl) field.get();

		Name methodName = field.toName(toGetterName(field));
		JCExpression methodType = findType(treeMaker, fieldNode, annotationNode.getTypesUtil());
		List<JCStatement> statements = createGetterBody(treeMaker, field);
		List<JCTypeParameter> methodGenericParams = List.nil();
		List<JCVariableDecl> parameters = List.nil();
		List<JCExpression> throwsClauses = List.nil();
		JCBlock methodBody = treeMaker.Block(0, statements);
		JCExpression annotationMethodDefaultValue = null;

		JCMethodDecl decl = treeMaker.MethodDef(treeMaker.Modifiers(JavacHandlerUtil.toJavacModifier(level)),
									methodName,
									methodType,
									methodGenericParams,
									parameters,
									throwsClauses,
									methodBody,
									annotationMethodDefaultValue);

//		copyJavadoc(field, decl, CopyJavadoc.GETTER);
		return decl;
	}

	public List<JCStatement> createGetterBody(JavacTreeMaker treeMaker, JavacNode field) {
//		boolean lookForGetter = lookForGetter(field, FieldAccess.ALWAYS_FIELD);

		JCExpression expression = treeMaker.Apply(List.<JCExpression>nil(),
				JavacHandlerUtil.chainDots(field, field.getName(), "getValue"), List.<JCExpression>nil());
		return List.<JCStatement>of(treeMaker.Return(expression));
	}

	public JCExpression findType(JavacTreeMaker treeMaker, JCVariableDecl fieldNode, JavacTypes types) {
		Type resType = null;

		ClassType superType = (ClassType) fieldNode.sym.type;
		if (superType.supertype_field == null) {
			superType = (ClassType) ((ClassType) superType.tsym.type).supertype_field;
		} else {
			superType = (ClassType) superType.supertype_field;
		}

		while (superType != null && resType == null) {
			for (Symbol sym : superType.tsym.getEnclosedElements()) {
//				ExecutableElement element = (ExecutableElement) sym;
				if (sym.getSimpleName().toString().equals("getValue")) {
//					if (superType.isParameterized()) {
//						resType = ((ClassType) fieldNode.vartype.type).typarams_field.head;
//					} else {
						ExecutableType type = (ExecutableType) types.asMemberOf(superType, sym);
						resType = sym.asType().asMethodType().restype;
//					}
					break;
				}
			}

			superType = (ClassType)((ClassType) superType.tsym.type).supertype_field;
		}

		return treeMaker.Type(resType);
	}
}