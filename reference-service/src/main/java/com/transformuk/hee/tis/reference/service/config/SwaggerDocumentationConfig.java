package com.transformuk.hee.tis.reference.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration for swagger to auto generate our REST API documentation.
 * For more info please {@see http://swagger.io/getting-started/}
 */
@Configuration
@EnableSwagger2
public class SwaggerDocumentationConfig {

	ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("TIS Reference API")
				.description("Reference Service REST API")
				.license("")
				.licenseUrl("")
				.termsOfServiceUrl("")
				.version("1.0.0")
				.contact(new Contact("Transform","http://transformuk.com/", "info@transformuk,com"))
				.build();
	}

	@Bean
	public Docket customImplementation(){
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.transformuk.hee.tis.reference.service.api"))
				.build()
				.apiInfo(apiInfo());
	}
}
