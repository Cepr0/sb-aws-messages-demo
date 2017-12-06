package com.amberity.awsmessagesdemo;

import com.amberity.awsmessagesdemo.messaging.AbstractSqsHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Sergei Poznanski, 2017-12-06
 */
@Slf4j
@Component
public class SqsHandler extends AbstractSqsHandler {
	
	public SqsHandler(ObjectMapper mapper, @Qualifier("messageSource") MessageSource source) {
		super(mapper, source);
	}
	
	@Override
	public void processMessage(Object payload) throws Exception {
		TimeUnit.MILLISECONDS.sleep(1000);
		log.info(">>> RECEIVED: {}", payload);
	}
}
