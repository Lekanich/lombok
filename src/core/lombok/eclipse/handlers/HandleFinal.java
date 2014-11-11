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
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.mangosdk.spi.ProviderFor;

import static lombok.eclipse.handlers.EclipseHandlerUtil.hasAnnotation;

/**
 * @author Suburban Squirrel
 * @version <version>
 * @since <version>
 */
@ProviderFor(EclipseAnnotationHandler.class)
@HandlerPriority(-2048) //-2^11; as @FieldDefaults
public class HandleFinal extends EclipseAnnotationHandler<Final> {

	@Override
	public void handle(AnnotationValues<Final> annotation, Annotation ast, EclipseNode annotationNode) {
		EclipseNode node = annotationNode.up();

		if (!(node.get() instanceof MethodDeclaration)) return;

		for (EclipseNode child : node.down()) {
			if (child.getKind() == AST.Kind.LOCAL || child.getKind() == AST.Kind.ARGUMENT) {
				FieldDeclaration localDecl = (FieldDeclaration) child.get();

				if (!hasAnnotation(NonFinal.class, child)) {
					localDecl.modifiers |= ClassFileConstants.AccFinal;
					child.rebuild();
				}
			}
		}
	}
}
