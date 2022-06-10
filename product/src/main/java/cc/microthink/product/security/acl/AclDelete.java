package cc.microthink.product.security.acl;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AclDelete {

    int pos() default 0;

    boolean isId() default true;

    Class targetClass();
}
