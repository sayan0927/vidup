package com.example.videostreamingcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableConfigurationProperties
@EnableJpaRepositories(basePackages = "com.example.videostreamingcore.core.repository")
@EnableMongoRepositories(basePackages = "com.example.videostreamingcore.notification.internal.repo")
public class VidUpCoreStartup {

	public static void main(String[] args) {
		SpringApplication.run(VidUpCoreStartup.class, args);
	}

}