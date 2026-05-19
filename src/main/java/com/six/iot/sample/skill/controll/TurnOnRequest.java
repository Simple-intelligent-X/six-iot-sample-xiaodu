package com.six.iot.sample.skill.controll;

import com.six.iot.sample.skill.SkillRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TurnOnRequest extends SkillRequest {
    protected AppliancePayload payload;
}
