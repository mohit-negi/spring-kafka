package com.ewe.solarKafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SolarKafkaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SolarKafkaApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SolarKafkaApplication.class);
	}
}
