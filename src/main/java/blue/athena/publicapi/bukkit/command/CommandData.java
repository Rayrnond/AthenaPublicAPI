package blue.athena.publicapi.bukkit.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandData {

    String name();
    String desc() default "&bCommand made with the Athena Public API.";
    String usage() default "&bPlease use: /<command>";

    String perm() default "null";
    String[] aliases() default {};

    boolean hidden() default false;
    boolean enabled() default true;
    boolean console() default true;

    String checkEnabled() default "&cThe command is currently disabled.";
    String checkPermission() default "&cYou may not use that command.";
    String checkConsole() default "&cThe command may only be used by players.";
    String commandFail() default "&cThere was an issue executing the command.";

}
