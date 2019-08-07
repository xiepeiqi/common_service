package com.xpq.cs.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.xpq.cs.config.interceptor.RequestInterceptor;

@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {
	
	@Bean
    public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor())
                .addPathPatterns("/**")
                /**去除测试模块拦截*/
                .excludePathPatterns("/index/**")
                /**去除swagger-doc 拦截*/
                .excludePathPatterns("/**/doc.html")
                /**去除对swagger api doc的拦截*/
                .excludePathPatterns("/**/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html", "/csrf/**", "/error");
      
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/static/");
    }


}
