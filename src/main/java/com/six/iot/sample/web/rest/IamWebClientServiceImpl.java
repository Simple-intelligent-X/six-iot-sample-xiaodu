package com.six.iot.sample.web.rest;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Getter
public class IamWebClientServiceImpl implements IamWebClientService {

    private WebClient webClient;

    @PostConstruct
    private void initWebClients() {
        webClient = WebClient.create();
    }

    @Override
    public String getAuthorizationHeader(String accessToken) {
        if (null == accessToken) {
            return null;
        }
        if (accessToken.startsWith(AUTHORIZATION_HEADER_VALUE_PREFIX)) {
            return accessToken;
        }
        return AUTHORIZATION_HEADER_VALUE_PREFIX + accessToken;
    }

    @Override
    public String getAuthorizationHeaderName() {
        return AUTHORIZATION_HEADER_NAME;
    }
}
