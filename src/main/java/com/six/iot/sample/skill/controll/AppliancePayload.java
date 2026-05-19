package com.six.iot.sample.skill.controll;

import com.six.iot.sample.skill.SkillRequest;
import com.six.iot.sample.skill.Appliance;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AppliancePayload extends SkillRequest.Payload {
    protected Appliance appliance;
}
