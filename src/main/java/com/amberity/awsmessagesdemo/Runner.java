package com.amberity.awsmessagesdemo;

import com.amberity.awsmessagesdemo.service.TestMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.IntStreamEx;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class Runner implements ApplicationRunner {

	@NonNull private final NotificationMessagingTemplate snsTemplate;
	
	@Async
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("STARTING RUNNER...");
		IntStreamEx.range(3).parallel(new ForkJoinPool(2)).forEach(this::sendTask);
		log.info("RUNNER STOPPED...");
	}

	private <T> void send(String topic, T object, String subject) {
		snsTemplate.sendNotification(topic, object, subject);
	}
	
	@SneakyThrows
	public void sendTask(int taskNum) {
		
		log.info("SEND TASK #{} STARTED", taskNum);
		TimeUnit.SECONDS.sleep(1 + new Random().nextInt(5));
		
		for (int i = 1; i <= 10; ++i) {
			TestMessage message = new TestMessage(Thread.currentThread().getName() +" : Task #" + taskNum, i, i + 0.5);
			send("testEvent", message, "TestMessage");
			log.info("<<< SENT: {}", message);
			TimeUnit.MILLISECONDS.sleep(500);
		}
		
		log.info("SEND TASK #{} COMPLETED", taskNum);
	}
}
