package io.weverse.noti.stream.model;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class CustomSerdes {

	public static Serde<ReceiveLogSet> receiveLogSetSerde() {
		JsonSerializer<ReceiveLogSet> serializer = new JsonSerializer<>();
		JsonDeserializer<ReceiveLogSet> deserializer = new JsonDeserializer<>(ReceiveLogSet.class);
		return Serdes.serdeFrom(serializer, deserializer);
	}
}
