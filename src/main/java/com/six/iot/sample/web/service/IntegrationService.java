package com.six.iot.sample.web.service;

import java.util.Map;

public interface IntegrationService {
    Map<String, Object> getAccessToken(String inboundMsgKey, String keyId, int expireInSeconds);
    Map<String, Object> sendMsg(String accessToken, String receiver, String msg, String topic, int qos);
}
