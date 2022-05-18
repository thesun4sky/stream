package io.weverse.noti.stream.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * https://bighitcorp.atlassian.net/wiki/spaces/communityDevBE/pages/3115155969/Global+Log+Push+Editing
 *
 * @author teasun.kim
 */
@NoArgsConstructor
@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiveLog {
	private String id;
}

