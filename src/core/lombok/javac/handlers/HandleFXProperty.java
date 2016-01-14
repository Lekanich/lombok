package lombok.javac.handlers;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
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
import static lombok.javac.handlers.JavacHandlerUtil.toGetterName;


@ProviderFor(JavacAnnotationHandler.class)
public class HandleFXProperty extends JavacAnnotationHandler<FXProperty> {

	@Override
	public void handle(AnnotationValues<FXProperty> annotation, JCAnnotation ast, JavacNode annotationNode) {
		deleteAnnotationIfNeccessary(annotationNode, FXProperty.class);

		JavacNode node = annotationNode.up();
		if (node == null) return;

		if (node.getKind() != Kind.FIELD) {
			annotationNode.addError("@Getter is only supported on a field.");
			return;
		}

		FXProperty annotationInstance = annotation.getInstance();
		AccessLevel level = annotationInstance.value();

		long localDecl = ((JCTree.JCVariableDecl) node.get()).mods.flags;
		if (annotationInstance.makeFinal()) {
			if ((localDecl & Flags.FINAL) == 0) {
				((JCTree.JCVariableDecl) node.get()).mods.flags |= Flags.FINAL;
				node.rebuild();
			}
		} else {
			if ((localDecl & Flags.FINAL) != 0) {
				annotationNode.addWarning("final in field is more prioritize than in @Getter field");		// TODO change sentence
			}
		}

		createGetterForProperty(node, annotationNode, level);
	}

	public void createGetterForProperty(JavacNode fieldNode, JavacNode source, AccessLevel level) {
		String methodName = toGetterName(fieldNode);

		if (methodName == null) {
			source.addWarning("Not generating getter for this field: It does not fit your @Accessors prefix list.");
			return;
		}

		// TODO make check if there any same field with such name

		injectMethod(fieldNode.up(), createGetter(level, fieldNode, fieldNode.getTreeMaker()));
	}

	public JCMethodDecl createGetter(AccessLevel level, JavacNode field, JavacTreeMaker treeMaker) {
		JCVariableDecl fieldNode = (JCVariableDecl) field.get();

		Name methodName = field.toName(toGetterName(field));
		JCExpression methodType = copyType(treeMaker, fieldNode);
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
				JavacHandlerUtil.chainDots(field, field.getName(), "get"), List.<JCExpression>nil());
		return List.<JCStatement>of(treeMaker.Return(expression));
	}

	public JCExpression copyType(JavacTreeMaker treeMaker, JCVariableDecl fieldNode) {
		Type resType;
		ClassType type = (ClassType) fieldNode.vartype.type;
		if (type.interfaces_field != null) {
			ClassType parent = (ClassType) type.interfaces_field.head;
			resType = parent.typarams_field.head;
		} else {
			resType = type.typarams_field.head;
		}

		return treeMaker.Type(resType);
	}
}