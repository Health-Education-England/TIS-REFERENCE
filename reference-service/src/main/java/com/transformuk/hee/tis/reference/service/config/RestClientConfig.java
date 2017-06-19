package com.transformuk.hee.tis.reference.service.config;

import com.transformuk.hee.tis.security.config.HeeProfileClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig extends HeeProfileClientConfig {

	@Bean
	public RestTemplate profileRestTemplate() {
		return super.createProfileRestTemplate();
	}
}
