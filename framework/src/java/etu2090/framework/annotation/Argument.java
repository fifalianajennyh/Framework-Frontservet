package etu2090.framework.annotation;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)// Modifier le type cible de METHOD à TYPE_PARAMETER
public @interface Argument {
    String value() default "test";
}
