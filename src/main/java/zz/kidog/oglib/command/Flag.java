package zz.kidog.oglib.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.PARAMETER})
public @interface Flag {

    Pattern FLAG_PATTERN = Pattern.compile("(-)([a-zA-Z])([\\w]*)?");

    String[] value();

    boolean defaultValue() default false;

    String description() default "";
}

