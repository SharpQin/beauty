package cc.microthink.product.security.acl;

import cc.microthink.product.domain.SerializableId;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.CumulativePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Save and Delete ACL with @AclSave and @AclDelete
 */
@Aspect
@Component
public class AclAspect {

    private Logger log = LoggerFactory.getLogger(AclAspect.class);

    private final MutableAclService mutableAclService;

    public AclAspect(MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
    }

    private String getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername();
        }
        else {
            return auth.getPrincipal().toString();
        }
    }

    private void createAcl(SerializableId serializableObj, Permission p) {
        log.info("---createAcl: serializableObj.getClass:{}", serializableObj.getClass());
        ObjectIdentity oi = new ObjectIdentityImpl(serializableObj.getClass(), serializableObj.getId());
        String currentUser = getCurrentUser();
        Sid sid = new PrincipalSid(currentUser);
        //Permission p = BasePermission.ADMINISTRATION;

        MutableAcl acl = mutableAclService.createAcl(oi);
        acl.insertAce(acl.getEntries().size(), p, sid, true);
        mutableAclService.updateAcl(acl);
    }

    private void createAcl(SerializableId serializableObj, List<Permission> permissions) {
        log.info("---createAcl: serializableObj.getClass:{}", serializableObj.getClass());
        ObjectIdentity oi = new ObjectIdentityImpl(serializableObj.getClass(), serializableObj.getId());
        String currentUser = getCurrentUser();
        Sid sid = new PrincipalSid(currentUser);
        //Permission p = BasePermission.ADMINISTRATION;

        MutableAcl acl = mutableAclService.createAcl(oi);
        for(Permission p : permissions) {
            acl.insertAce(acl.getEntries().size(), p, sid, true);
        }
        mutableAclService.updateAcl(acl);
    }

    private void delAcl(Class targetClass, Serializable id) {
        //acl_object_identity, acl_entry
        ObjectIdentity oid = new ObjectIdentityImpl(targetClass, id);
        this.mutableAclService.deleteAcl(oid, false);
    }

    @Around(value="@annotation(AclSave)")
    public Object exeAclSave(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = joinPoint.proceed();
        //getThis(), getTarget(), getArgs(), getSignature()
        if (!(proceed instanceof SerializableId)) {
            log.warn("aclSaving: Object is not implements SerializableId. Obj:{}", proceed.toString());
        }

        /*
        CumulativePermission cm = new CumulativePermission();
        cm.set(BasePermission.ADMINISTRATION)
            .set(BasePermission.DELETE)
                .set(BasePermission.CREATE)
                    .set(BasePermission.WRITE)
                        .set(BasePermission.READ);
        createAcl((SerializableId)proceed, cm);
        */

        /*
        List<Permission> list = new ArrayList<>();
        list.add(BasePermission.ADMINISTRATION);
        list.add(BasePermission.DELETE);
        list.add(BasePermission.CREATE);
        list.add(BasePermission.WRITE);
        list.add(BasePermission.READ);
        createAcl((SerializableId)proceed, list);
         */

        createAcl((SerializableId)proceed, BasePermission.ADMINISTRATION);

        return proceed;
    }

    @Around("@annotation(AclDelete)")
    public Object exeAclDelete(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        AclDelete annotation = method.getAnnotation(AclDelete.class);

        log.info("aclDeleting: isId:{}", annotation.isId());
        log.info("aclDeleting: pos:{}", annotation.pos());
        log.info("aclDeleting: isId:{}", annotation.targetClass());

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            log.info("aclDeleting: arg:{}", arg);
        }

        Object idObj = args[annotation.pos()];
        Serializable id = annotation.isId() ? (Serializable)idObj : ((SerializableId)idObj).getId();

        Object proceed = joinPoint.proceed();

        delAcl(annotation.targetClass(), id);

        return proceed;
    }



}
