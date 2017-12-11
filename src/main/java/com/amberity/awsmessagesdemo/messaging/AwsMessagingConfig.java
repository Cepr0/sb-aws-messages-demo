package com.amberity.awsmessagesdemo.messaging;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NonNull;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.cloud.aws.messaging.support.NotificationMessageArgumentResolver;
import org.springframework.cloud.aws.messaging.support.converter.NotificationRequestConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.SimpleMessageConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		NotificationMessagingTemplate template = new NotificationMessagingTemplate(amazonSNS);

		MappingJackson2MessageConverter jacksonMessageConverter = new MappingJackson2MessageConverter();
		jacksonMessageConverter.setSerializedPayloadClass(String.class);
		jacksonMessageConverter.setObjectMapper(objectMapper());
		jacksonMessageConverter.setStrictContentTypeMatch(false);

		template.setMessageConverter(jacksonMessageConverter);
		return template;
	}

	private ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}
	
	@Bean
	public QueueMessageHandlerFactory queueMessageHandlerFactory(@NonNull BeanFactory beanFactory) {
		
//		MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
		
		//set strict content type match to false
//		messageConverter.setStrictContentTypeMatch(false);
//		PayloadArgumentResolver argumentResolver = new PayloadArgumentResolver(messageConverter);
//		factory.setArgumentResolvers(Collections.singletonList(argumentResolver));

		// https://stackoverflow.com/a/46863444

		MappingJackson2MessageConverter jacksonMessageConverter = new MappingJackson2MessageConverter();
		jacksonMessageConverter.setSerializedPayloadClass(String.class);
		jacksonMessageConverter.setObjectMapper(objectMapper());
		jacksonMessageConverter.setStrictContentTypeMatch(false);

		List<MessageConverter> payloadArgumentConverters = new ArrayList<>();
		payloadArgumentConverters.add(jacksonMessageConverter);

		// This is the converter that is invoked on SNS messages on SQS listener
		NotificationRequestConverter notificationRequestConverter = new NotificationRequestConverter(jacksonMessageConverter);

		payloadArgumentConverters.add(notificationRequestConverter);
		payloadArgumentConverters.add(new SimpleMessageConverter());

		CompositeMessageConverter compositeMessageConverter = new CompositeMessageConverter(payloadArgumentConverters);

		QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
		factory.setBeanFactory(beanFactory);

		// The factory has this method for custom resolvers (can be read in the question)
		factory.setArgumentResolvers(Collections.singletonList(new NotificationMessageArgumentResolver(compositeMessageConverter)));

		return factory;
	}
	
	@Bean
	public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(@NonNull AmazonSQSAsync amazonSqs) {
		
		SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
		
		factory.setAmazonSqs(amazonSqs);
//		factory.setAutoStartup(false);
		factory.setVisibilityTimeout(visibilityTimeout);     // in sec
		factory.setMaxNumberOfMessages(maxNumberOfMessages); // 1-10
		factory.setWaitTimeOut(waitTimeout);                 // 1-20 in sec
		
		return factory;
	}
}
