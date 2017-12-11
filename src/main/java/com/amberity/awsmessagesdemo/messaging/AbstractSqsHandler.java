package com.amberity.awsmessagesdemo.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.MessageSource;

import java.io.IOException;
import java.util.Locale;

import static org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy.NEVER;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractSqsHandler {

	@NonNull private final ObjectMapper mapper;
	@NonNull private final MessageSource source;

	@SqsListener(value = "${sqs.name}", deletionPolicy = NEVER)
	private void receiveMessage(String message, Acknowledgment acknowledgment) throws Exception {

		AwsMessage awsMessage;
		try {
			awsMessage = mapper.readValue(message, AwsMessage.class);
		} catch (Exception e) {
			log.error("Cannot convert received message to AwsMessage: {}. Cause: {}, Exception: {}", message, e.getMessage(), e.getClass());
			return;
		}

		log.debug("Got SQS message: {}", awsMessage);

		// Take a class type of the message payload
		String subject = awsMessage.getSubject();
		String className = source.getMessage(subject, null, subject, Locale.getDefault());

		Class<?> cls;
		try {
			cls = Class.forName(className);
		} catch (Exception e) {
			log.warn("Unexpected type ({}) of the received message: {}", subject, awsMessage);
			return;
		}

		// Take a payload
		String payloadString = awsMessage.getMessage();

		Object payload;
		try {
			payload = mapper.readValue(payloadString, cls);
		} catch (IOException e) {
			log.error("Cannot deserialize the payload ({}) of the received message ({}). Cause: {}", payloadString, awsMessage, e.getMessage());
			return;
		}

		// Precess message
		processMessage(payload, acknowledgment);
	}
	
	public abstract void processMessage(Object payload, Acknowledgment acknowledgment) throws Exception;
}
