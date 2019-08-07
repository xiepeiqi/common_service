package com.xpq.cs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfiguration {

	 @Bean
	 public Docket createRestApi() {
	     return new Docket(DocumentationType.SWAGGER_2)
	     .apiInfo(apiInfo())
	     .select()
	     //RequestHandlerSelectors.any()表示对所有api进行监控  
	     .apis(RequestHandlerSelectors.basePackage("com.xpq.cs"))
	     .paths(PathSelectors.any())
	     .build()
	     .enable(true);//这里决定开不开启
	 }

	private ApiInfo apiInfo() {
	     return new ApiInfoBuilder()
	     .title("xxx项目接口文档")
	     .description("swagger-bootstrap-ui")
	     .termsOfServiceUrl("http://localhost:8080/")
	     .contact(new Contact("Mr.x", "", "xie_pq@163.com"))
	     .version("1.0")
	     .build();
	 }
}
