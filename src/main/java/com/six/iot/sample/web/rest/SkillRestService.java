package com.six.iot.sample.web.rest;

import java.util.Map;

public interface SkillRestService {
    Map<String, Object> getUserDevices(String userAccessToken);
}
