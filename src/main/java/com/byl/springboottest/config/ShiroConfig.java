package com.byl.springboottest.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.byl.springboottest.shiro.PermissionRealm;
import com.byl.springboottest.shiro.RolesAuthorizationFilter;
import com.byl.springboottest.utils.SaltUtil;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.timeout}")
    private int redisTimeout;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${shiro.session.expireTime}")
    private int expireTime;

    @Value("${shiro.jessionid}")
    private String jessionId;


    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> map = new HashMap<>();
        //对所有用户认证
        map.put("/**", "authc");
        //允许访问
        map.put("/css/**", "anon");
        map.put("/images/**", "anon");
        map.put("/js/**", "anon");
        map.put("/lib/**", "anon");
        map.put("/login.html", "anon");
        map.put("/reg.html", "anon");
        map.put("/user/login", "anon");
        map.put("/user/reg", "anon");
        //登录
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/error/403.html");
        //页面角色权限控制
        map.put("/level1/**", "anyRoleFilter[user,admin,superadmin]");
        map.put("/level2/**", "anyRoleFilter[admin,superadmin]");
        map.put("/level3/**", "anyRoleFilter[superadmin]");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("anyRoleFilter", new RolesAuthorizationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        return shiroFilterFactoryBean;
    }

    /**
     * 自定义密码校验器
     *
     * @return
     */
    @Bean
    public CredentialsMatcher credentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName(SaltUtil.HASHALGORITHMNAME);
        credentialsMatcher.setHashIterations(SaltUtil.HASHITERATIONS);
        return credentialsMatcher;
    }

    //将自己的验证方式加入容器
    @Bean
    public PermissionRealm permissionRealm(CredentialsMatcher credentialsMatcher) {
        PermissionRealm customRealm = new PermissionRealm();
        customRealm.setCredentialsMatcher(credentialsMatcher);
        return customRealm;
    }

    /**
     * 配置SecurityManager
     *
     * @return
     */
    @Bean
    public SecurityManager securityManager(CredentialsMatcher credentialsMatcher) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(permissionRealm(credentialsMatcher));  // 设置realm
        securityManager.setSessionManager(sessionManager());    // 设置sessionManager
        securityManager.setCacheManager(myRedisCacheManager()); // 设置cacheManager
        return securityManager;
    }


    /**
     * redisCacheManager 缓存 redis实现
     * shiro-redis
     * We need a field to identify this Cache Object in Redis. So you need to defined an id field which you can get unique id to identify this principal.
     * For example, if you use UserInfo as Principal class, the id field maybe userId, userName, email, etc. For example, getUserId(), getUserName(), getEmail(), etc.
     * Default value is "id", that means your principal object has a method called "getId()"
     *
     * @return
     */
    @Bean
    public RedisCacheManager myRedisCacheManager() {
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisManager(redisManager());
        cacheManager.setPrincipalIdFieldName("username");//主键名称（默认id）
        return cacheManager;
    }

    /**
     * 配置shiro redisManager
     * shiro-redis
     *
     * @return
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisHost + ":" + redisPort);
        redisManager.setTimeout(redisTimeout);
        redisManager.setPassword(redisPassword);
        return redisManager;
    }

    /**
     * SessionManager
     * shiro-redis
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(expireTime * 60 * 1000);
        sessionManager.setSessionIdUrlRewritingEnabled(false);//禁用url重写，否则浏览器中会在url后面自动加上：xx/login;JSESSIONID=xxx
        sessionManager.setSessionIdCookie(getSessionIdCookie());
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    /**
     * 给shiro的sessionId默认的JSSESSIONID名字改掉
     *
     * @return
     */
    @Bean
    public SimpleCookie getSessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie(jessionId);
        return simpleCookie;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * shiro-redis
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setRedisManager(redisManager());
        sessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return sessionDAO;
    }

    /**
     * Session ID 生成器
     *
     * @return
     */
    @Bean
    public JavaUuidSessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /***
     * 使授权注解起作用
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    //配置ShiroDialect:用于thymeleaf和shiro标签配合使用
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

}
