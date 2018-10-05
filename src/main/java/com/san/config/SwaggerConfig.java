//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: ass-common
//Module						: ass-common
//Package Name					: com.san.common.config
//File Name						: SwaggerConfig.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 9 Dec, 2016 10:00:15 PM
//Description					:  
//-----------------------------------------------------------------------------------------------------------
//					CHANGE HISTORY
//-----------------------------------------------------------------------------------------------------------
//Date			Change By		Modified Function 			Change Description (Bug No. (If Any))
//(DD MMM YYYY)
//9 Dec, 2016   	anil			N/A							File Created
//-----------------------------------------------------------------------------------------------------------
package com.san.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	Logger logger = Logger.getLogger(this.getClass());

	@Bean
	public Docket api() {
		ApiSelectorBuilder apiSelectorBuilder = new Docket(DocumentationType.SWAGGER_2).select();
		apiSelectorBuilder = apiSelectorBuilder.apis(RequestHandlerSelectors.any());
		apiSelectorBuilder = apiSelectorBuilder.paths(PathSelectors.any());
		Docket docket = apiSelectorBuilder.build();
		docket = docket.apiInfo(apiInfo()); // Optional
		logger.info("Swagger URL : http://localhost:8080/api/swagger-ui.html");
		return docket;
	}

	private ApiInfo apiInfo() {
		@SuppressWarnings("deprecation")
		ApiInfo apiInfo = new ApiInfo("Student REST API", "Public APIs", "1.0", "", "studentAdmin", "", "");
		return apiInfo;
	}
	// Url(Rest) for remote swagger :
	// http://localhost:8080/api/v1/api-docs
	// http://localhost:8080/api/swagger-ui.html
}
