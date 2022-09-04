package com.magician.web.mvc.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Verification {
    /**
     * Is it nullable
     * @return
     */
    boolean notNull() default false;

    /**
     * minimum
     * @return
     */
    String min() default "";

    /**
     * maximum
     * @return
     */
    String max() default "";

    /**
     * regular expression
     * @return
     */
    String reg() default "";

    /**
     * error message
     * @return
     */
    String msg() default "Please fill in the correct data";

    /**
     * scope
     * @return
     */
    String[] apis() default {};
}
