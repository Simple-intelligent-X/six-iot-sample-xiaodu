package com.six.iot.sample.web.rest;

import org.springframework.web.reactive.function.client.WebClient;

public interface IamWebClientService {

    String AUTHORIZATION_HEADER_NAME = "Authorization";
    String AUTHORIZATION_HEADER_VALUE_PREFIX = "Bearer ";
    
    WebClient getWebClient();

    String getAuthorizationHeader(String accessToken);

    String getAuthorizationHeaderName();
}
