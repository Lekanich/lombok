package lombok.javac.handlers;

import com.sun.tools.javac.tree.JCTree;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.experimental.FinalArgs;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import org.mangosdk.spi.ProviderFor;

import static lombok.javac.handlers.HandleFinal.*;
import static lombok.javac.handlers.JavacHandlerUtil.*;

/**
 * @author Suburban Squirrel
 * @version 1.14.9
 * @since 1.14.9
 */
@ProviderFor(JavacAnnotationHandler.class)
@HandlerPriority(-2048) //-2^11; as @FieldDefaults
public class HandleFinalArgs extends JavacAnnotationHandler<FinalArgs> {

	@Override
	public void handle(AnnotationValues<FinalArgs> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
		deleteAnnotationIfNeccessary(annotationNode, FinalArgs.class);

		handleFinal(annotationNode.up(), false, AST.Kind.ARGUMENT);
	}
}