package lombok.experimental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adds modifier to each local variable or parameter in method with this annotation (exclude for-variable-declaration).
 * Works with lombok.experimental.NonFinal		for mark non final
 * Equivalent to {@code FinalArgs} with additional for local variables
 *
 * @see lombok.experimental.NonFinal
 * @see lombok.experimental.FinalArgs
 * @author Suburban Squirrel
 * @version 1.14.9
 * @since 1.14.9
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface Final {

}
