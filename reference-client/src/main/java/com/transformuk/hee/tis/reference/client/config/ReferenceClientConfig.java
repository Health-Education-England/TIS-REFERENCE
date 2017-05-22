package com.transformuk.hee.tis.reference.client.config;

import com.transformuk.hee.tis.security.client.KeycloakClientRequestFactory;
import com.transformuk.hee.tis.security.client.KeycloakRestTemplate;
import com.transformuk.hee.tis.security.config.KeycloakClientConfig;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class to define a rest template to be used by the Reference Client
 *
 * This rest template is to only be used when a service needs to communicate with the reference service
 * on behalf of itself, where there is no interaction if a browser or end client.
 * e.g. during an ETL, some batch processing etc.
 *
 * This rest template uses talks to keycloak directly to get a jwt key using the services credentials
 */
@Configuration
@Import(KeycloakClientConfig.class)
public class ReferenceClientConfig {

	@Bean
	public ClientHttpRequestFactory keycloackClientRequestFactory(Keycloak keycloak){
		return new KeycloakClientRequestFactory(keycloak);
	}

	@Bean
	public RestTemplate referenceRestTemplate(ClientHttpRequestFactory factory) {
		return new KeycloakRestTemplate(factory);
	}


}
