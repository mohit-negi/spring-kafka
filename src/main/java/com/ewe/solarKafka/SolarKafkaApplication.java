package com.ewe.solarKafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication
public class SolarKafkaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SolarKafkaApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SolarKafkaApplication.class);
	}
	@Bean
	public Collection<String> dynamicTopics() {
		// This could be loaded from a database or configuration file
		return Arrays.asList("topic1", "topic2", "topic3");
	}
}
