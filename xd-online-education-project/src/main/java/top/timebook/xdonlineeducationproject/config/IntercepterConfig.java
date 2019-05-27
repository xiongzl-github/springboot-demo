package top.timebook.xdonlineeducationproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.timebook.xdonlineeducationproject.intercept.LoginIntercepter;


/**
 * @ClassName IntercepterConfig
 * @Description 登陆拦截器配置
 * @Author xiongzl
 * @Date 2019/5/8 18:25
 * @Version 1.0
 **/
@Configuration
public class IntercepterConfig implements WebMvcConfigurer{

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginIntercepter()).addPathPatterns("/user/api/v1/*/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
