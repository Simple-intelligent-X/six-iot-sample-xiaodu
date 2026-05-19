package com.six.iot.sample.web.rest;

import java.util.Map;

public interface IamRestService {
    /**
     * Get the access token
     */
    Map<String, Object> fetchAccessToken(String jwtForKey);

    Map<String, Object> sendMsg(String accessToken, String receiver, String msg, String topic, int qos);
}
