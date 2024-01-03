package com.capgemini.ocean.institute.training.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {

	@Bean
	public Docket api() {
	    return new Docket(DocumentationType.OAS_30)
	            .select()
	            .apis(RequestHandlerSelectors.basePackage("com.capgemini.ocean.institute.training.controller"))
	            .paths(PathSelectors.ant("/api/admins/**"))
	            .build()
	            .apiInfo(apiInfo());
	}


    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Advanced Intelligent Tourist Guide",
                "Case Study Post My Training",
                "2.0",
                "Terms of service",
                new Contact("Manish Kushwaha", "kushwahamanish311@gmail.com", "Capgemini.com"),
                "Free to use",
                "API license URL",
                Collections.emptyList());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/swagger-ui/index.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        // Additionally, you can add a handler for the Swagger UI static content
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");

        // Swagger UI in Spring Boot 2.x
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

}
