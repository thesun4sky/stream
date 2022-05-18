package io.weverse.noti.stream.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReceiveLogSet {
	private final Set<String> idList;

	public ReceiveLogSet() {
		this.idList = new HashSet<>();
	}

	public ReceiveLogSet add(String id) {
		idList.add(id);
		return this;
	}
}