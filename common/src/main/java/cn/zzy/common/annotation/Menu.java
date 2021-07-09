package cn.zzy.common.annotation;

import java.lang.annotation.*;

/**
 * 菜单类型注解
 * 
 * @author zhaozuoyu
 * @date 2020/11/27
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Menu {

    int id() default -1;

    String type() default "";

}
