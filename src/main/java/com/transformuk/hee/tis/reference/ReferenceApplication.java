package com.transformuk.hee.tis.reference;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import static org.springframework.boot.SpringApplication.run;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
public class ReferenceApplication {

	public static void main(String[] args) {
		run(ReferenceApplication.class, args);
	}

	/**
	 * Allows to throw exception when no exception handler found
	 * @return DispatcherServlet
	 */
	@Bean
	public DispatcherServlet dispatcherServlet() {
		DispatcherServlet ds = new DispatcherServlet();
		ds.setThrowExceptionIfNoHandlerFound(true);
		return ds;
	}
}
