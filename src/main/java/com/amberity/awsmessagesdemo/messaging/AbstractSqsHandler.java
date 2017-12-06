package com.amberity.awsmessagesdemo.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy.ON_SUCCESS;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractSqsHandler {

	@NonNull private final ObjectMapper mapper;
	@NonNull private final MessageSource source;

	@SqsListener(value = "${sqs.name}", deletionPolicy = ON_SUCCESS)
	private void receiveMessage(AwsMessage message) throws Exception {

		log.debug("Got SQS message: {}", message);
		
		// Take a class type of the message payload
		String subject = message.getSubject();
		String className = source.getMessage(subject, null, subject, Locale.getDefault());
		Class<?> cls = Class.forName(className);
		
		// Take a payload
		String payloadString = message.getMessage();
		Object payload = mapper.readValue(payloadString, cls);
	
		// Precess message
		processMessage(payload);
	}
	
	public abstract void processMessage(Object payload) throws Exception;
}
