package com.six.iot.sample.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class IamRestServiceImpl implements IamRestService {

    private static Logger Log = LoggerFactory.getLogger(IamRestServiceImpl.class);

    @Value("${iam.token.paramName.grantType}")
    private String paramNameGrantType;

    @Value("${iam.token.paramValue.grantType}")
    private String paramValueGrantType;

    @Value("${iam.token.paramName.assertion}")
    private String paramNameAssertion;

    @Value("${iam.token.endpoint}")
    private String tokenEndpoint;

    @Value("${iot.integration.inboundMsg.endpoint}")
    private String inboundMsgEndpoint;

    @Value("${iot.integration.productId}")
    private String productId;

    @Autowired
    private IamWebClientService webClientService;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Map<String, Object> fetchAccessToken(String jwtForKey) {
        WebClient webClient = webClientService.getWebClient();
        String uri = tokenEndpoint;
        Log.debug("token uri: {}", uri);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(paramNameGrantType, paramValueGrantType);
        map.add(paramNameAssertion, jwtForKey);
        try {
            Log.debug("token request: {}", objectMapper.writeValueAsString(map));
        } catch (JsonProcessingException e) {
            //throw new RuntimeException(e);
        }
        Mono<Map> device = webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(map))
                /*.header(
                        webClientService.getAuthorizationHeaderName(),
                        webClientService.getAuthorizationHeader(accessToken)
                )*/
                .retrieve().bodyToMono(Map.class);
        Map<String, Object> resp = device.block();
        return resp;
    }

    // topic: 655dcb820b3ad61dc370f697/0dc0c454-e4e1-4e3d-b2e6-6f57cbb85dbe/light
    @Override
    public Map<String, Object> sendMsg(String accessToken, String receiver, String msg, String topic, int qos) {
        WebClient webClient = webClientService.getWebClient();
        String uri = inboundMsgEndpoint;
        Log.debug("inboundMsgEndpoint uri: {}", uri);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("productId", productId);
        map.add("receiver", receiver);
        map.add("msg", msg);
        map.add("topic", topic);
        map.add("qos", qos + "");
        try {
            Log.debug("sendMsg request: {}", objectMapper.writeValueAsString(map));
        } catch (JsonProcessingException e) {
            //throw new RuntimeException(e);
        }
        Mono<Map> device = webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(map))
                .header(
                        webClientService.getAuthorizationHeaderName(),
                        webClientService.getAuthorizationHeader(accessToken)
                )
                .retrieve().bodyToMono(Map.class);
        Map<String, Object> resp = device.block();
        return resp;
    }
}
