package com.amberity.awsmessagesdemo.messaging;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadArgumentResolver;

import java.util.Collections;

/**
 * @author Sergei Poznanski, 2017-12-06
 */
@Configuration
public class AwsMessagingConfig {
	
	@Value("${sqs.maxNumberOfMessages:3}") private Integer maxNumberOfMessages; // default - 3 sec
	@Value("${sqs.waitTimeout:20}") private Integer waitTimeout;                // default - 20 sec
	@Value("${sqs.visibilityTimeout:5}") private Integer visibilityTimeout;     // default - 5 sec
	
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
	
	@Bean
	public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSqs) {
		
		SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
		
		factory.setAmazonSqs(amazonSqs);
//		factory.setAutoStartup(false);
		factory.setVisibilityTimeout(visibilityTimeout);     // in sec
		factory.setMaxNumberOfMessages(maxNumberOfMessages); // 1-10
		factory.setWaitTimeOut(waitTimeout);                 // 1-20 in sec
		
		return factory;
	}
}
