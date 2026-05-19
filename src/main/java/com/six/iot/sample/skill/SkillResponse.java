package com.six.iot.sample.skill;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkillResponse {

    Header header;

    Payload payload;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Payload {
    }
}
