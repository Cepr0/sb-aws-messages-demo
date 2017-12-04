package com.amberity.awsmessagesdemo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
@Async
public class Runner implements ApplicationRunner {

	@NonNull private final NotificationMessagingTemplate snsTemplate;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		TestMessage message = new TestMessage("Test Message", 1, 0.5);
		send("testEvent", message, "TestMessage");
		TimeUnit.SECONDS.sleep(2);
	}

	private <T> void send(String topic, T object, String subject) {
		snsTemplate.sendNotification(topic, object, subject);
	}
}
