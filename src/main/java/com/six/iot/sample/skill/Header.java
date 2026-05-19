package com.six.iot.sample.skill;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Header {
    protected String payloadVersion;
    protected String name;
    protected String namespace;
    protected String messageId;
}
