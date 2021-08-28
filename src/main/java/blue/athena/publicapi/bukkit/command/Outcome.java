package blue.athena.publicapi.bukkit.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Outcome {

    String pattern();

    String perm() default "null";
    boolean opBypass() default true;

    boolean enabled() default true;
    boolean console() default false;

    String checkEnabled() default "&cThe subcommand is currently disabled.";
    String checkPermission() default "&cYou may not use that subcommand.";
    String checkConsole() default "&cThe subcommand may only be used by players.";

}
