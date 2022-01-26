package com.magician.web.mvc.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Verification {
    /**
     * 是否可为空
     * @return
     */
    boolean notNull() default false;

    /**
     * 最小值
     * @return
     */
    String min() default "";

    /**
     * 最大值
     * @return
     */
    String max() default "";

    /**
     * 正则
     * @return
     */
    String reg() default "";

    /**
     * 提示语
     * @return
     */
    String msg() default "请填写正确的数据";

    /**
     * 作用域
     * @return
     */
    String[] apis() default {};
}
