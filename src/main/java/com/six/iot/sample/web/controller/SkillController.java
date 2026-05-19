package com.six.iot.sample.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.six.iot.sample.skill.*;
import com.six.iot.sample.skill.controll.TurnOffConfirmationPayload;
import com.six.iot.sample.skill.controll.TurnOffRequest;
import com.six.iot.sample.skill.controll.TurnOnConfirmationPayload;
import com.six.iot.sample.skill.controll.TurnOnRequest;
import com.six.iot.sample.skill.discover.DiscoverPayload;
import com.six.iot.sample.web.service.IntegrationService;
import com.six.iot.sample.web.service.SkillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Check the doc:
 * https://developer.dueros.baidu.com/didp/doc/dueros-bot-platform/dbp-smart-home/protocol/discovery-message_markdown
 */
@RestController
public class SkillController {

    private static Logger Log = LoggerFactory.getLogger(SkillController.class);

    @Value("${iot.integration.inboundMsg.key}")
    private String inboundMsgKey;

    @Value("${iot.integration.inboundMsg.keyId}")
    private String keyId;

    @Value("${iot.integration.productId}")
    private String productId;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IntegrationService integrationService;

    @Autowired
    private SkillService skillService;

    /*
     * Convert the devices to the XiaoDu appliances
     * */
    private static Appliance getAppliance(List<?> devices) {
        //For demo purpose, only reply the first device
        Map<String, Object> device = (Map) devices.get(0);
        //Set the appliance info
        Appliance appliance = new Appliance();
        //Set the device unique id as the appliance id
        appliance.setApplianceId(device.get("deviceGuid") + "");

        //Define the appliance types
        List<String> types = new ArrayList<>();
        types.add("LIGHT");
        appliance.setApplianceTypes(types);

        //Define the actions that the appliance support
        List<String> actions = new ArrayList<>();
        actions.add("turnOn");
        actions.add("turnOff");
        appliance.setActions(actions);

        //The name that user use to trigger the actions
        appliance.setFriendlyName("电灯");
        appliance.setFriendlyDescription("您可以用小度进行电灯的打开和关闭");
        //Set the appliance is reachable
        appliance.setReachable(true);
        appliance.setModelName("LED");
        appliance.setVersion("1.0.0-Firmware-Debug");
        //Set the appliance details info, the info will be delivered back for some of the following voice trigger requests
        appliance.setAdditionalApplianceDetails(new HashMap<>());
        appliance.setManufacturerName("SiX Innovation");
        return appliance;
    }

    private static TurnOnConfirmationPayload getTurnOnConfirmationPayload() {
        TurnOnConfirmationPayload payload = new TurnOnConfirmationPayload();
        List<ApplianceAttribute> attributes = new ArrayList<>();
        ApplianceAttribute attribute = new ApplianceAttribute();
        long timestamp = new Date().getTime();
        attribute.setName("turnOnState");
        attribute.setValue("ON");
        attribute.setScale("");
        attribute.setTimestampOfSample(timestamp);
        attribute.setUncertaintyInMilliseconds(10);
        attribute.setLegalValue("(ON, OFF)");
        attributes.add(attribute);

        attribute = new ApplianceAttribute();
        attribute.setName("connectivity");
        attribute.setValue("REACHABLE");
        attribute.setScale("");
        attribute.setTimestampOfSample(timestamp);
        attribute.setUncertaintyInMilliseconds(10);
        attribute.setLegalValue("(UNREACHABLE, REACHABLE)");
        attributes.add(attribute);
        payload.setAttributes(attributes);
        return payload;
    }

    private static TurnOffConfirmationPayload getTurnOffConfirmationPayload() {
        TurnOffConfirmationPayload payload = new TurnOffConfirmationPayload();
        List<ApplianceAttribute> attributes = new ArrayList<>();
        ApplianceAttribute attribute = new ApplianceAttribute();
        long timestamp = new Date().getTime();
        attribute.setName("turnOffState");
        attribute.setValue("OFF");
        attribute.setScale("");
        attribute.setTimestampOfSample(timestamp);
        attribute.setUncertaintyInMilliseconds(10);
        attribute.setLegalValue("(ON, OFF)");
        attributes.add(attribute);

        attribute = new ApplianceAttribute();
        attribute.setName("connectivity");
        attribute.setValue("REACHABLE");
        attribute.setScale("");
        attribute.setTimestampOfSample(timestamp);
        attribute.setUncertaintyInMilliseconds(10);
        attribute.setLegalValue("(UNREACHABLE, REACHABLE)");
        attributes.add(attribute);
        payload.setAttributes(attributes);
        return payload;
    }

    /**
     * This endpoint will be configured in XiaoDu Skill setting(Cloud to Cloud integration).
     */
    @RequestMapping(value = "/skill/handler", method = RequestMethod.POST)
    public @ResponseBody Object skillHandler(@RequestBody Map<String, Object> skillReqBody) {
        Map<String, Object> resp = new HashMap<>();
        try {
            String skillReqStr = objectMapper.writeValueAsString(skillReqBody);
            Log.debug("Skill request from XiaoDu: {}", skillReqStr);
            SkillRequest skillRequest = skillService.parseSkillRequest(skillReqStr);
            if (null == skillRequest) {
                throw new IllegalStateException("Can't parse the Skill Request from XiaoDu!");
            }
            if (skillRequest.isDiscoverAppliancesRequest()) {
                return discoverAppliances(skillRequest);
            } else if (skillRequest.isTurnOnRequest()) {
                //Parse the request again to TurnOnRequest according to its specific data model, then handle this request
                return turnOnAppliance(skillService.parseTurnOnRequest(skillReqStr));
            } else if (skillRequest.isTurnOffRequest()) {
                //Parse the request again to TurnOffRequest according to its specific data model, then handle this request
                return turnOffAppliance(skillService.parseTurnOffRequest(skillReqStr));
            }
        } catch (Exception e) {
            Log.error("Fail to handle Skill Request", e);
        }
        return resp;
    }

    private SkillResponse turnOnAppliance(TurnOnRequest skillRequest) throws JsonProcessingException {
        SkillResponse response = new SkillResponse();
        //Set the response header
        response.setHeader(Header.builder().name("TurnOnConfirmation")
                .namespace("DuerOS.ConnectedHome.Control")
                .messageId(UUID.randomUUID().toString())
                .payloadVersion(skillRequest.getPayloadVersion()).build());

        if (null != skillRequest.getPayload() && null != skillRequest.getPayload().getAppliance()) {
            Map<String, Object> tokenResp = integrationService.getAccessToken(inboundMsgKey, keyId, 60000);
            if (null == tokenResp || null == tokenResp.get("access_token")) {
                throw new IllegalStateException("Can't get the access token for integration key");
            }
            Log.debug("turnOnAppliance.TokenResponse from IAM: {}", objectMapper.writeValueAsString(tokenResp));
            String msg = "{ \"light\": \"on\" }";
            //{productId}/{deviceGuid}/xxx as topic, its also the ACLs that enforced on
            String topic = productId + "/" + skillRequest.getPayload().getAppliance().getApplianceId() + "/light";
            //Send msg to specific device(defined in the topic path)
            Map<String, Object> sendMsgResp = integrationService.sendMsg(tokenResp.get("access_token") + "", skillRequest.getPayload().getAppliance().getApplianceId(), msg, topic, 0);
            Log.debug("turnOnAppliance.sendMsgResponse: {}", objectMapper.writeValueAsString(sendMsgResp));
            TurnOnConfirmationPayload payload = getTurnOnConfirmationPayload();
            response.setPayload(payload);
        }
        return response;
    }

    private SkillResponse turnOffAppliance(TurnOffRequest skillRequest) throws JsonProcessingException {
        SkillResponse response = new SkillResponse();
        response.setHeader(Header.builder().name("TurnOffConfirmation")
                .namespace("DuerOS.ConnectedHome.Control")
                .messageId(UUID.randomUUID().toString())
                .payloadVersion(skillRequest.getPayloadVersion()).build());
        if (null != skillRequest.getPayload() && null != skillRequest.getPayload().getAppliance()) {
            Map<String, Object> tokenResp = integrationService.getAccessToken(inboundMsgKey, keyId, 60000);
            if (null == tokenResp || null == tokenResp.get("access_token")) {
                throw new IllegalStateException("Can't get the access token for integration key");
            }
            Log.debug("turnOffAppliance.TokenResponse from IAM: {}", objectMapper.writeValueAsString(tokenResp));
            String msg = "{ \"light\": \"off\" }";
            String topic = productId + "/" + skillRequest.getPayload().getAppliance().getApplianceId() + "/light";
            Map<String, Object> sendMsgResp = integrationService.sendMsg(tokenResp.get("access_token") + "", skillRequest.getPayload().getAppliance().getApplianceId(), msg, topic, 0);
            Log.debug("turnOffAppliance.sendMsgResponse: {}", objectMapper.writeValueAsString(sendMsgResp));
            TurnOffConfirmationPayload payload = getTurnOffConfirmationPayload();
            response.setPayload(payload);
        }
        return response;
    }

    private SkillResponse discoverAppliances(SkillRequest skillRequest) throws JsonProcessingException {
        SkillResponse response = new SkillResponse();
        response.setHeader(Header.builder().name("DiscoverAppliancesResponse")
                .namespace("DuerOS.ConnectedHome.Discovery")
                .messageId(UUID.randomUUID().toString())
                .payloadVersion(skillRequest.getPayloadVersion()).build());

        DiscoverPayload payload = new DiscoverPayload();
        Map<String, Object> userDevices = null;
        //Use the access token of the federated user to access its device list
        if (null != skillRequest.getAccessToken()) {
            userDevices = skillService.getUserDevices(skillRequest.getAccessToken());
            Log.debug("UserDevicesResp from IoT: {}", objectMapper.writeValueAsString(userDevices));
        }
        payload.setDiscoveredAppliances(parseDiscoveredAppliances(userDevices));
        response.setPayload(payload);
        return response;
    }

    private List<Appliance> parseDiscoveredAppliances(Map<String, Object> userDevices) {
        if (null == userDevices) {
            return null;
        }
        if (null == userDevices.get("content") || !(userDevices.get("content") instanceof List<?>)) {
            return null;
        }
        List<?> devices = (List) (userDevices.get("content"));
        if (devices.isEmpty()) {
            return null;
        }
        Appliance appliance = getAppliance(devices);
        List<Appliance> appliances = new ArrayList<>();
        appliances.add(appliance);
        return appliances;
    }
}
