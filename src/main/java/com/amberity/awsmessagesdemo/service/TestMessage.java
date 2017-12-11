package com.amberity.awsmessagesdemo.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
public class TestMessage {
	String strValue;
	Integer intValue;
	Double doubleValue;
	LocalDate localDate;
	LocalDateTime localDateTime;

	@JsonCreator
	public TestMessage(@JsonProperty("strValue") String strValue,
	                   @JsonProperty("intValue") Integer intValue,
	                   @JsonProperty("doubleValue") Double doubleValue,
	                   @JsonProperty("localDate") LocalDate localDate,
	                   @JsonProperty("localDateTime") LocalDateTime localDateTime) {

		this.strValue = strValue;
		this.intValue = intValue;
		this.doubleValue = doubleValue;
		this.localDate = localDate;
		this.localDateTime = localDateTime;
	}
}
