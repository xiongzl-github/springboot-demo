package top.timebook.xdonlineeducationproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @ClassName CrossDomainConfig
 * @Description 跨域配置
 * @Author xiongzl
 * @Date 2019/5/16 17:25
 * @Version 1.0
 **/


@Configuration
public class CrossDomainConfig extends WebMvcConfigurerAdapter {

    /**
     * @Author xiongzl
     * @Description 跨域配置
     * @Date 2019/5/16 17:43
     * @Param [registry]
     * @Return void
     **/
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
                .allowCredentials(true).maxAge(3600);
    }
}
