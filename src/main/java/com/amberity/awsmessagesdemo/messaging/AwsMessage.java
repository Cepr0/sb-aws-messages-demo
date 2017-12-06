package com.amberity.awsmessagesdemo.messaging;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"Type",
		"MessageId",
		"TopicArn",
		"Subject",
		"Message",
		"Timestamp",
		"SignatureVersion",
		"Signature",
		"SigningCertURL",
		"UnsubscribeURL",
		"MessageAttributes"
})
@Value
public class AwsMessage {

	@JsonProperty("Type") public String type;
	@JsonProperty("MessageId") public String messageId;
	@JsonProperty("TopicArn") public String topicArn;
	@JsonProperty("Subject") public String subject;
	@JsonProperty("Message") public String message;
	@JsonProperty("Timestamp") public String timestamp;
	@JsonProperty("SignatureVersion") public String signatureVersion;
	@JsonProperty("Signature") public String signature;
	@JsonProperty("SigningCertURL") public String signingCertURL;
	@JsonProperty("UnsubscribeURL") public String unsubscribeURL;
	@JsonProperty("MessageAttributes") public MessageAttributes messageAttributes;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({
			"Type",
			"Value"
	})
	@Value
	public static class ContentType {

		@JsonProperty("Type") public String type;
		@JsonProperty("Value") public String value;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({
			"Type",
			"Value"
	})
	@Value
	public static class Id {

		@JsonProperty("Type") public String type;
		@JsonProperty("Value") public String value;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({
			"NOTIFICATION_SUBJECT_HEADER",
			"id",
			"contentType",
			"timestamp"
	})
	@Value
	public static class MessageAttributes {

		@JsonProperty("NOTIFICATION_SUBJECT_HEADER") public SubjectHeader subjectHeader;
		@JsonProperty("id") public Id id;
		@JsonProperty("contentType") public ContentType contentType;
		@JsonProperty("timestamp") public Timestamp timestamp;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({
			"Type",
			"Value"
	})
	@Value
	public static class SubjectHeader {

		@JsonProperty("Type") public String type;
		@JsonProperty("Value") public String value;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({
			"Type",
			"Value"
	})
	@Value
	public static class Timestamp {

		@JsonProperty("Type") public String type;
		@JsonProperty("Value") public String value;
	}
}