package com.onlineServicesForEthnic.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    /**
     * 根据@Tag 上的排序，写入x-order
     *
     * @return the global open api customizer
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("智能园区服务端")
                .version("1.0")
                .description("基于springboot的智能园区服务端")
                .termsOfService("https://app.apifox.com/invite?token=E-BiwuxhGGHSsQOCOxfA1")
                .license(new License().name("Apache 2.0")
                    .url("https://app.apifox.com/invite?token=E-BiwuxhGGHSsQOCOxfA1")));
    }
}
