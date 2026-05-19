package com.six.iot.sample.web.service;

import com.six.iot.sample.skill.SkillRequest;
import com.six.iot.sample.skill.controll.TurnOffRequest;
import com.six.iot.sample.skill.controll.TurnOnRequest;

import java.util.Map;

public interface SkillService {
    Map<String, Object> getUserDevices(String userAccessToken);
    SkillRequest parseSkillRequest(Map<String, Object> skillReq);
    SkillRequest parseSkillRequest(String skillReqStr);
    TurnOnRequest parseTurnOnRequest(String skillReqStr);
    TurnOffRequest parseTurnOffRequest(String skillReqStr);
}
