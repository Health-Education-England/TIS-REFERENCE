package com.transformuk.hee.tis.reference.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to JHipster.
 * <p>
 * <p>
 * Properties are configured in the application.yml file.
 * </p>
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
	@Value("${application.relaxSqlInjectionSanitiser}")
	private boolean relaxSqlInjectionSanitiser;

	public boolean isSqlInjectionSanitiserRelaxed() {
		return relaxSqlInjectionSanitiser;
	}

	public void setRelaxSqlInjectionSanitiser(boolean relaxSqlInjectionSanitiser) {
		this.relaxSqlInjectionSanitiser = relaxSqlInjectionSanitiser;
	}
}
