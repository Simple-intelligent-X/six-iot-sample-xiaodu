package com.six.iot.sample.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.six.iot.sample.skill.SkillRequest;
import com.six.iot.sample.skill.controll.TurnOffRequest;
import com.six.iot.sample.skill.controll.TurnOnRequest;
import com.six.iot.sample.web.rest.SkillRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SkillServiceImpl implements SkillService {

    private static Logger Log = LoggerFactory.getLogger(SkillServiceImpl.class);

    @Autowired
    private SkillRestService skillRestService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Map<String, Object> getUserDevices(String userAccessToken) {
        try {
            Map<String, Object> userDevicesResp = skillRestService.getUserDevices(userAccessToken);
            ;
            return userDevicesResp;
        } catch (Exception e) {
            Log.error("Get user devices fail", e);
        }
        return null;
    }

    @Override
    public SkillRequest parseSkillRequest(Map<String, Object> skillReq) {
        if (null == skillReq) {
            return null;
        }
        String skillReqStr = null;
        try {
            skillReqStr = objectMapper.writeValueAsString(skillReq);
            return objectMapper.readValue(skillReqStr, SkillRequest.class);
        } catch (Exception e) {
            Log.error("fail to parse SkillRequest: {}", skillReqStr);
        }
        return null;
    }

    @Override
    public SkillRequest parseSkillRequest(String skillReqStr) {
        if (null == skillReqStr) {
            return null;
        }
        try {
            return objectMapper.readValue(skillReqStr, SkillRequest.class);
        } catch (Exception e) {
            Log.error("fail to parse SkillRequestStr: {}", skillReqStr);
        }
        return null;
    }

    @Override
    public TurnOnRequest parseTurnOnRequest(String skillReqStr) {
        if (null == skillReqStr) {
            return null;
        }
        try {
            return objectMapper.readValue(skillReqStr, TurnOnRequest.class);
        } catch (Exception e) {
            Log.error("fail to parse TurnOnRequest: {}", skillReqStr);
        }
        return null;
    }

    @Override
    public TurnOffRequest parseTurnOffRequest(String skillReqStr) {
        if (null == skillReqStr) {
            return null;
        }
        try {
            return objectMapper.readValue(skillReqStr, TurnOffRequest.class);
        } catch (Exception e) {
            Log.error("fail to parse TurnOffRequest: {}", skillReqStr);
        }
        return null;
    }
}
