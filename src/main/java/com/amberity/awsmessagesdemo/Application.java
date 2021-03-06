package com.amberity.awsmessagesdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(4);
		executor.setMaxPoolSize(8);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("TaskRunner-");
		executor.initialize();
		return executor;
	}
}
