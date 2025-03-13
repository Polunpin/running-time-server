package com.tencent.wxcloudrun.common.swagger;

import cn.hutool.core.util.RandomUtil;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/***
 * 创建Swagger配置
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String env;

    /**
     * 基础配置
     *
     * @return the global open api customizer
     */
    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        String host = "";
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            System.out.println("-");
        }
        System.out.println("" +
                "\n----------------------------------------------------" +
                "\n\nSwagger open api 载入..." +
                "\n接口文档地址：http://" + host + ":" + env + "/doc.html " +
                "\n\n----------------------------------------------------");
        return openApi -> {
            if (openApi.getTags() != null) {
                openApi.getTags().forEach(tag -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("x-order", RandomUtil.randomInt(0, 100));
                    tag.setExtensions(map);
                });
            }
            if (openApi.getPaths() != null) {
//                openApi.addExtension("x-test123","333");
//                openApi.getPaths().addExtension("x-abb",RandomUtil.randomInt(1,100));
            }

        };
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HOUCLOUD快速开发基础套件 API")
                        .version("2024.1 开源版")
                        .contact(new Contact().name("HOUCLOUD").url("https://houcloud.com").email("yunhouhuang@gmail.com"))
                        .description("HOUCLOUD快速开发基础套件 VUE + JAVA + MYSQL + REDIS")
                        .termsOfService("https://houcloud.com")
                        .license(new License().name("PRO版本")
                                .url("https://houcloud.com/")));
    }


}
