package com.six.iot.sample.skill.controll;

import com.six.iot.sample.skill.SkillResponse;
import com.six.iot.sample.skill.ApplianceAttribute;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TurnOnConfirmationPayload extends SkillResponse.Payload {
    List<ApplianceAttribute> attributes;
}
