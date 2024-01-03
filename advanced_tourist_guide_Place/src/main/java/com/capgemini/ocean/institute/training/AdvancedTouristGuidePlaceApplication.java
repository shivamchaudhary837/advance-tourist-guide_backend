package com.capgemini.ocean.institute.training;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;




@SpringBootApplication
//@EnableSwagger2
public class AdvancedTouristGuidePlaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdvancedTouristGuidePlaceApplication.class, args);
	}

//
//	@Bean
//	public Docket api() {                
//	    return new Docket(DocumentationType.SWAGGER_2)          
//	      .select()
//	      .apis(RequestHandlerSelectors.basePackage("com.capgemini.ocean.institute.training.controller"))
//	      .paths(PathSelectors.ant("/place/*"))
//	      .build()
//	      .apiInfo(apiInfo());
//	}
//
//	private ApiInfo apiInfo() {
//	    return new ApiInfo(
//	      "Advanced Intelligent Tourist guide", 
//	      "Case Study Post My Training", 
//	      "Manish Kushwaha", 
//	      "Terms of service", 
//	      new Contact("Manish Kushwaha", "kushwahamanish311@gmail.com", "Capgemini.com"), 
//	      "Free to use", "API license URL", Collections.emptyList());
//	}
}
