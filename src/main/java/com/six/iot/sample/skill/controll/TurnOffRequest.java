package com.six.iot.sample.skill.controll;

import com.six.iot.sample.skill.SkillRequest;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TurnOffRequest extends SkillRequest {
    protected AppliancePayload payload;
}
