package lombok.eclipse.handlers;

import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.eclipse.EclipseAnnotationHandler;
import lombok.eclipse.EclipseNode;
import lombok.experimental.Final;
import lombok.experimental.NonFinal;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ForStatement;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.mangosdk.spi.ProviderFor;

import static lombok.eclipse.handlers.EclipseHandlerUtil.*;

/**
 * @author Suburban Squirrel
 * @version 1.14.9
 * @since 1.14.9
 */
@ProviderFor(EclipseAnnotationHandler.class)
@HandlerPriority(-2048) //-2^11; as @FieldDefaults
public class HandleFinal extends EclipseAnnotationHandler<Final> {

	@Override
	public void handle(AnnotationValues<Final> annotation, Annotation ast, EclipseNode annotationNode) {
		EclipseNode node = annotationNode.up();

		handleFinal(node, true, AST.Kind.LOCAL, AST.Kind.ARGUMENT);
	}

	public static void handleFinal(EclipseNode node, boolean checkDeep, AST.Kind... validKinds) {
		if (!(node.get() instanceof MethodDeclaration)) return;

		for (EclipseNode child : node.down()) {
			for (AST.Kind kind : validKinds) {
				if (checkDeep && child.getKind() == AST.Kind.STATEMENT) handleFinal(child, true, validKinds);
				if (child.getKind() != kind || node.get() instanceof ForStatement) continue;

				FieldDeclaration localDecl = (FieldDeclaration) child.get();
				if (!hasAnnotation(NonFinal.class, child)) {
					localDecl.modifiers |= ClassFileConstants.AccFinal;
					child.rebuild();
				}
			}
		}
	}
}
