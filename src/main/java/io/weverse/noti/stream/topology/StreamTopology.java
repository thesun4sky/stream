package io.weverse.noti.stream.topology;

import java.util.function.Function;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.weverse.noti.stream.aggregator.ReceiveLogAggregator;
import lombok.RequiredArgsConstructor;

/**
 * @author teasun
 */
@RequiredArgsConstructor
@Configuration
public class StreamTopology {

	private final ReceiveLogAggregator receiveLogAggregator;

	@Bean(name = "process-receiveLog")
	public Function<KStream<Long, String>, KStream<Long, String>> processReceiveLog() {
		return receiveLogAggregator.aggregate();
	}
}