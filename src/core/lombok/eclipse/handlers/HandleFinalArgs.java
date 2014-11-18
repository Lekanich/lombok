package lombok.eclipse.handlers;

import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.eclipse.EclipseAnnotationHandler;
import lombok.eclipse.EclipseNode;
import lombok.experimental.FinalArgs;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.mangosdk.spi.ProviderFor;

import static lombok.eclipse.handlers.HandleFinal.*;

/**
 * @author Suburban Squirrel
 * @version 1.14.9
 * @since 1.14.9
 */
@ProviderFor(EclipseAnnotationHandler.class)
@HandlerPriority(-2048) //-2^11; as @FieldDefaults
public class HandleFinalArgs extends EclipseAnnotationHandler<FinalArgs> {

	@Override
	public void handle(AnnotationValues<FinalArgs> annotation, Annotation ast, EclipseNode annotationNode) {
		EclipseNode node = annotationNode.up();

		handleFinal(node, AST.Kind.ARGUMENT);
	}
}
