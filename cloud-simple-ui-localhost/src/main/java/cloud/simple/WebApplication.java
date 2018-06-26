/*
 * Copyright 2012-2020 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * @author lzhoumail@126.com/zhouli
 * Git http://git.oschina.net/zhou666/spring-cloud-7simple
 */

package cloud.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableSwagger2

public class WebApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication springApplication =new SpringApplication(WebApplication.class);

		springApplication.addListeners(new ApplicationStartup());
		
		springApplication.run(args);
		System.out.println("");
		
		
	}
	public static UserAgentInterceptor userAgentInterceptor = new UserAgentInterceptor();

	@Bean
	RestTemplate restTemplate() {
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(2000);
        httpRequestFactory.setConnectTimeout(2000);
        httpRequestFactory.setReadTimeout(5000);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Collections.<ClientHttpRequestInterceptor>singletonList(userAgentInterceptor));
		return restTemplate;
	}

}
