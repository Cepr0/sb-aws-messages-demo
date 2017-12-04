package com.amberity.awsmessagesdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy.ALWAYS;

@Slf4j
@RequiredArgsConstructor
@Component
public class AwsHandler {

	@NonNull private final ObjectMapper mapper;

	private final static String SNS_TOPIC = "requests";

	@SqsListener(value = SNS_TOPIC, deletionPolicy = ALWAYS)
	public void receiveMessage(AwsMessage message) throws ClassNotFoundException, IOException {

		log.info("Received SQS message {}", message);

		String subject = message.getSubject();
		String packagePath = getClass().getPackage().getName();
		String className = packagePath + "." + subject;
		Class<?> cls = Class.forName(className);

		String payload = message.getMessage();
		Object value = mapper.readValue(payload, cls);

		log.info("Payload: {}", value);
	}
}
