package lombok.javac.handlers;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.experimental.Final;
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
		Context context = annotationNode.getContext();
		Symtab symbolTable = annotationNode.getSymbolTable();
		JavacNode top = annotationNode.top();

		JCTree tree = node.get();
		JCTree.JCMethodDecl methodDecl;
		if (tree instanceof JCTree.JCMethodDecl) methodDecl = (JCTree.JCMethodDecl) tree;

		for (JavacNode child : node.down()) {
			if (child.getKind() == AST.Kind.LOCAL || child.getKind() == AST.Kind.ARGUMENT) {
				JCTree.JCVariableDecl localDecl = (JCTree.JCVariableDecl) child.get();
				localDecl.mods.flags |= Flags.FINAL;
				child.rebuild();
			}
		}



		int x = 0;

	}
}
