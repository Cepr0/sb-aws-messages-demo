package com.amberity.awsmessagesdemo;

import com.amazonaws.services.sns.AmazonSNS;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadArgumentResolver;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Collections;

@EnableAsync
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public NotificationMessagingTemplate snsTemplate(AmazonSNS amazonSNS) {
		return new NotificationMessagingTemplate(amazonSNS);
	}

	@Bean
	public QueueMessageHandlerFactory queueMessageHandlerFactory() {

		QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();

		MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();

		//set strict content type match to false
		messageConverter.setStrictContentTypeMatch(false);
		PayloadArgumentResolver argumentResolver = new PayloadArgumentResolver(messageConverter);

		factory.setArgumentResolvers(Collections.singletonList(argumentResolver));
		return factory;
	}
}
