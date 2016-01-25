package lombok.experimental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adds methods and access level(optional).
 *
 *
 * @author Suburban Squirrel
 * @version 2.16.7.14
 * @since 2.16.7.14
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface FXProperty {
	/**
	 * If you want your method to be non-public, you can specify an alternate access level here.
	 */
	lombok.AccessLevel value() default lombok.AccessLevel.PUBLIC;
}
