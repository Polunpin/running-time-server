package com.tencent.wxcloudrun.common.security.config;

import cn.hutool.core.util.StrUtil;
import com.tencent.wxcloudrun.common.security.interceptor.AdminTokenInterceptor;
import com.tencent.wxcloudrun.common.security.interceptor.FrontTokenAuthorizer;
import com.tencent.wxcloudrun.common.security.interceptor.FrontTokenInterceptor;
import com.tencent.wxcloudrun.common.security.token.store.RedisTokenStore;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * token验证拦截器
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({SecurityProperties.class})
public class WebSecurityConfig implements WebMvcConfigurer {
    private final StringRedisTemplate stringRedisTemplate;

    @Resource
    private SecurityProperties securityProperties;

    public WebSecurityConfig(StringRedisTemplate stringRedisTemplate) {
        System.out.println("""
                
                ----------------------------------------------------
                加载认证授权模块
                """);
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Bean
    public RedisTokenStore redisTokenStore() {
        return new RedisTokenStore(stringRedisTemplate);
    }

    @Bean
    public HandlerInterceptor adminTokenInterceptor() {
        return new AdminTokenInterceptor();
    }

    @Bean
    public HandlerInterceptor frontTokenInterceptor() {
        return new FrontTokenInterceptor();
    }

    @Bean
    public HandlerInterceptor frontTokenAuthorizer() {
        return new FrontTokenAuthorizer();
    }


    /**
     * 添加Token拦截器
     * addPathPatterns添加需要拦截的命名空间
     * excludePathPatterns添加排除拦截命名空间
     *
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println(StrUtil.format("""
                管理端拦截器启动"""));
        //后台token拦截
        registry.addInterceptor(adminTokenInterceptor())
                .addPathPatterns(securityProperties.getAdminPrefix() + "/**")
                // 这里excludePathPatterns()可以忽略请求，但无法精确到请求方法。
                .excludePathPatterns();
        System.out.println(StrUtil.format("""
                用户端拦截器启动"""));
        // 前端用户登录token
        registry.addInterceptor(frontTokenAuthorizer())
                .addPathPatterns(securityProperties.getFrontPrefix() + "/**")
                // 这里excludePathPatterns()可以忽略请求，但无法精确到请求方法。
                .excludePathPatterns();
        ;
        System.out.println("""
                
                ----------------------------------------------------
                """);
    }

    /**
     * 静态资源
     * Swagger
     *
     * @param registry 资源注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
