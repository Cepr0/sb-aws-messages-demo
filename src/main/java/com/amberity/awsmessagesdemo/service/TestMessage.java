package com.amberity.awsmessagesdemo.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class TestMessage {
	String strValue;
	Integer intValue;
	Double doubleValue;

	@JsonCreator
	public TestMessage(@JsonProperty("strValue") String strValue, @JsonProperty("intValue") Integer intValue, @JsonProperty("doubleValue") Double doubleValue) {
		this.strValue = strValue;
		this.intValue = intValue;
		this.doubleValue = doubleValue;
	}
}
