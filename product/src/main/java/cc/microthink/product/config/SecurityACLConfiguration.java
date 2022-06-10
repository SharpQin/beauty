package cc.microthink.product.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import javax.sql.DataSource;
import java.io.Serializable;

@Configuration
public class SecurityACLConfiguration {

    private final DataSource dataSource;

    //private final CacheManager cacheManager;

    public SecurityACLConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
        //this.cacheManager = cacheManager;  , CacheManager cacheManager
    }

//    @Bean
//    public EhCacheBasedAclCache aclCache() {
//        return new EhCacheBasedAclCache(aclEhCacheFactoryBean().getObject(), permissionGrantingStrategy(), aclAuthorizationStrategy());
//    }

    @Bean
    public AclCache aclCache() {
        return new SpringCacheBasedAclCache(
            cacheManager().getCache("aclCache"),
            permissionGrantingStrategy(),
            aclAuthorizationStrategy());
    }

    @Bean
    public CacheManager cacheManager() {
//        org.ehcache.CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
//            .withCache("aclCache",
//                CacheConfigurationBuilder.newCacheConfigurationBuilder(Serializable.class, MutableAcl.class, ResourcePoolsBuilder.heap(10)))
//            .build();
//        cacheManager.init();
//        return new EhCacheCacheManager(cacheManager); //Need: net.sf.ehcache.CacheManager

        Iterable<CachingProvider> iterable = Caching.getCachingProviders();
        iterable.forEach(cachingProvider -> {
            System.out.println("cachingProvider=" + cachingProvider);
        });

        CachingProvider provider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
        javax.cache.CacheManager jcacheManager = provider.getCacheManager();
        MutableConfiguration<Serializable, MutableAcl> configuration =
            new MutableConfiguration<Serializable, MutableAcl>()
                .setTypes(Serializable.class, MutableAcl.class)
                .setStoreByValue(false)
                .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_HOUR));
        jcacheManager.createCache("aclCache", configuration);
        return new JCacheCacheManager(jcacheManager);
    }


//    @Bean
//    public EhCacheFactoryBean aclEhCacheFactoryBean() {
//        EhCacheFactoryBean ehCacheFactoryBean = new EhCacheFactoryBean();
//        ehCacheFactoryBean.setCacheManager(aclCacheManager().getObject());
//        ehCacheFactoryBean.setCacheName("aclCache");
//        return ehCacheFactoryBean;
//    }

//    @Bean
//    public EhCacheManagerFactoryBean aclCacheManager() {
//        return new EhCacheManagerFactoryBean();
//    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Bean
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        AclPermissionEvaluator permissionEvaluator = new AclPermissionEvaluator(aclService());
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setPermissionCacheOptimizer(new AclPermissionCacheOptimizer(aclService()));
        return expressionHandler;
    }

    @Bean
    public LookupStrategy lookupStrategy() {
        return new BasicLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), new ConsoleAuditLogger());
    }

    @Bean
    public JdbcMutableAclService aclService() {
        JdbcMutableAclService jdbcMutableAclService = new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());
        //For Postgresql
        jdbcMutableAclService.setSidIdentityQuery("SELECT currval('acl_sid_id_seq')");
        jdbcMutableAclService.setClassIdentityQuery("SELECT currval('acl_class_id_seq')");

        //For MySql
        //jdbcMutableAclService.setClassIdentityQuery("SELECT @@IDENTITY");
        //jdbcMutableAclService.setSidIdentityQuery("SELECT @@IDENTITY");

        return jdbcMutableAclService;
    }

}
