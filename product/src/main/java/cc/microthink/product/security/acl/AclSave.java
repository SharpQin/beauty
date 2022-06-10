package cc.microthink.product.security.acl;

import org.springframework.security.acls.model.Permission;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AclSave {

    String value() default "";

    String permission() default "";
}
