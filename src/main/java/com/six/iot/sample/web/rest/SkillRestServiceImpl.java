package com.six.iot.sample.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class SkillRestServiceImpl implements SkillRestService {

    private static Logger Log = LoggerFactory.getLogger(SkillRestServiceImpl.class);

    @Value("${skill.device.userDevicesEndpoint}")
    private String userDevicesEndpoint;

    @Autowired
    private IamWebClientService webClientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Map<String, Object> getUserDevices(String userAccessToken) {
        WebClient webClient = webClientService.getWebClient();
        String uri = userDevicesEndpoint;
        Log.debug("uri: {}", uri);
        Mono<Map> device = webClient.get()
                .uri(uri)
                .header(
                        webClientService.getAuthorizationHeaderName(),
                        webClientService.getAuthorizationHeader(userAccessToken)
                )
                .retrieve().bodyToMono(Map.class);
        Map<String, Object> resp = device.block();
        return resp;
    }


}
