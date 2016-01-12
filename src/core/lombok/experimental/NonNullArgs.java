package lombok.experimental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Lombok will insert null-check at the start of the method? throwing a {@code NullPointerException}
 * Works with javax.annotation.Nullable
 *
 * @see javax.annotation.Nullable
 * @see lombok.NonNull
 * @author Suburban Squirrel
 * @version 1.14.11
 * @since 1.14.11
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface NonNullArgs {
}
