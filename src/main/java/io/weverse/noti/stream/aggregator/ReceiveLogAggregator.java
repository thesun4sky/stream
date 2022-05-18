package io.weverse.noti.stream.aggregator;

import java.time.Duration;
import java.util.function.Function;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.weverse.noti.stream.model.CustomSerdes;
import io.weverse.noti.stream.model.ReceiveLog;
import io.weverse.noti.stream.model.ReceiveLogSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author teasun
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ReceiveLogAggregator {

	private final ObjectMapper objectMapper;

	public Function<KStream<Long, String>, KStream<Long, String>> aggregate() {

		Materialized<Long, ReceiveLogSet, WindowStore<Bytes, byte[]>> materialized = Materialized
			.<Long, ReceiveLogSet, WindowStore<Bytes, byte[]>>as("rla")
			.withKeySerde(Serdes.Long())
			.withValueSerde(CustomSerdes.receiveLogSetSerde());
		Duration windowDuration = Duration.ofSeconds(10);

		return input -> input
			.map((key, value) -> {
				ReceiveLog receiveLog = null;
				try {
					receiveLog = objectMapper.readValue(value, ReceiveLog.class);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				log.info("receiveLog : {}", receiveLog);  // leave a log here
				return KeyValue.pair(key, receiveLog.getId());

			})
			.groupByKey(org.apache.kafka.streams.kstream.Grouped.with(Serdes.Long(), Serdes.String()))
			.windowedBy(TimeWindows.of(windowDuration).grace(Duration.ZERO))
			.aggregate(ReceiveLogSet::new,
				(key, value, aggregate) -> {
					log.info("value aggregate : {}", value); // never leave a log here (blocked before)
					return aggregate.add(value);
				},
				materialized
			)
			.suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
			.toStream()
			.map((key, value) -> {
				log.info("new record : {}", value); // never leave a log here (blocked before)
				try {
					return KeyValue.pair(key.key(), objectMapper.writeValueAsString(value));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					return KeyValue.pair(key.key(), "error");
				}
			});
	}
}