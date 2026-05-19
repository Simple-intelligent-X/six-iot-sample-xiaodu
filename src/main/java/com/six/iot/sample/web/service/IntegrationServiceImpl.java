package com.six.iot.sample.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.six.iam.jwt.KeyJwt;
import com.six.iot.sample.web.rest.IamRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IntegrationServiceImpl implements IntegrationService {

    private static Logger Log = LoggerFactory.getLogger(IntegrationServiceImpl.class);

    @Autowired
    private IamRestService iamRestService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Map<String, Object> getAccessToken(String inboundMsgKey, String keyId, int expireInSeconds) {
        try {
            String jwtForKey = KeyJwt.createJwtBearerTokenWithRsaPrivateKey(inboundMsgKey, keyId, expireInSeconds);
            Map<String, Object> tokenResp = iamRestService.fetchAccessToken(jwtForKey);
            return tokenResp;
        } catch (Exception e) {
            Log.error("Get access token for the key fail", e);
        }
        return null;
    }

    @Override
    public Map<String, Object> sendMsg(String accessToken, String receiver, String msg, String topic, int qos) {
        try {
            Map<String, Object> sendMsgResp = iamRestService.sendMsg(accessToken, receiver, msg, topic, qos);
            return sendMsgResp;
        } catch (Exception e) {
            Log.error("Send msg fail", e);
        }
        return null;
    }

}
