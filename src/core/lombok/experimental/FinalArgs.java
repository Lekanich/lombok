package lombok.experimental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adds modifier to each parameter in method with this annotation.
 * Works with lombok.experimental.NonFinal		for mark non final
 *
 * @see lombok.experimental.NonFinal
 * @see lombok.experimental.Final
 * @author Suburban Squirrel
 * @version 1.14.9
 * @since 1.14.9
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface FinalArgs {

}
