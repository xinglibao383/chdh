package com.mpw.model.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;

@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    public Docket api(){
        RequestParameter parameter = new RequestParameterBuilder()
                .name("authorization")
                .description("user token")
                .in(ParameterType.HEADER)
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)).defaultValue("Bearer"))
                .required(false)
                .build();
        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mpw"))
                .build()
                .globalRequestParameters(Arrays.asList(parameter));
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("内部开发")
                .version("1")
                .build();
    }
}
