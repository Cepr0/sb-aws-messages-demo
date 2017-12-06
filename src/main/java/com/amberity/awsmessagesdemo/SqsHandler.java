package com.amberity.awsmessagesdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy.ON_SUCCESS;

@Slf4j
@RequiredArgsConstructor
@Component
public class SqsHandler {

	@NonNull private final ObjectMapper mapper;
	@NonNull private final MessageSource source;

	@SqsListener(value = "${sqs.name}", deletionPolicy = ON_SUCCESS)
	private void receiveMessage(AwsMessage message) throws ClassNotFoundException, IOException, InterruptedException {

//		log.info("<<<M>>> MESSAGE: {}", message);
		String subject = message.getSubject();
		String className = source.getMessage(subject, null, subject, Locale.getDefault());

		Class<?> cls = Class.forName(className);
		
		String payload = message.getMessage();
		Object value = mapper.readValue(payload, cls);
		
		TimeUnit.MILLISECONDS.sleep(1000);
		
		log.info(">>> RECEIVED: {}", value);
	}
}
