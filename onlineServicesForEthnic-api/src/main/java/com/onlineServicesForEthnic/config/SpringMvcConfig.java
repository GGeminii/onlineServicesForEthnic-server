package com.onlineServicesForEthnic.config;

import com.onlineServicesForEthnic.utils.JacksonObjectMapperUtil;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ServletComponentScan
@EnableAspectJAutoProxy
public class SpringMvcConfig implements WebMvcConfigurer {
    //配置静态资源的访问路径
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html")
            .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/favicon.ico")
            .addResourceLocations("classpath:/static/");
    }

    //    注册一个自定义的消息转换器
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        构建一个将实体数据转为json数据的转化器
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new JacksonObjectMapperUtil());//在消息转换器中，添加自定义的转化器
        converters.add(0,converter);
        //解决swagger文档异常
        converters.add(0,new ByteArrayHttpMessageConverter());
    }
}