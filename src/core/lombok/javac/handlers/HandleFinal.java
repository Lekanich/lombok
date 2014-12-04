package lombok.javac.handlers;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.experimental.Final;
import lombok.experimental.NonFinal;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import org.mangosdk.spi.ProviderFor;

import static lombok.javac.handlers.JavacHandlerUtil.*;

/**
 * @author Suburban Squirrel
 * @version 1.14.9
 * @since 1.14.9
 */
@ProviderFor(JavacAnnotationHandler.class)
@HandlerPriority(-2048) //-2^11; as @FieldDefaults
public class HandleFinal extends JavacAnnotationHandler<Final> {

	@Override
	public void handle(AnnotationValues<Final> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
		deleteAnnotationIfNeccessary(annotationNode, Final.class);
		JavacNode node = annotationNode.up();

		handleFinal(node, true, AST.Kind.LOCAL, AST.Kind.ARGUMENT);
	}

	public static void handleFinal(JavacNode node, boolean checkDeep, AST.Kind... validKinds) {
		for (JavacNode child : node.down()) {
			for (AST.Kind kind : validKinds) {
				if (checkDeep && child.getKind() == AST.Kind.STATEMENT) handleFinal(child, true, validKinds);
				if (child.getKind() != kind || node.get() instanceof JCTree.JCForLoop) continue;			// skip variable init in 'for'

				JCTree.JCVariableDecl localDecl = (JCTree.JCVariableDecl) child.get();
				if (!hasAnnotationAndDeleteIfNeccessary(NonFinal.class, child)) {
					localDecl.mods.flags |= Flags.FINAL;
					child.rebuild();
				}
			}
		}
	}
}
