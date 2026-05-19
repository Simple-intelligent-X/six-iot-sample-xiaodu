package com.six.iot.sample.skill;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SkillRequest {

    protected Header header;
    protected Payload payload;

    public boolean isDiscoverAppliancesRequest() {
        return "DuerOS.ConnectedHome.Discovery".equals(getNamespace()) && "DiscoverAppliancesRequest".equals(getName());
    }

    public boolean isTurnOnRequest() {
        return "DuerOS.ConnectedHome.Control".equals(getNamespace()) && "TurnOnRequest".equals(getName());
    }

    public boolean isTurnOffRequest() {
        return "DuerOS.ConnectedHome.Control".equals(getNamespace()) && "TurnOffRequest".equals(getName());
    }

    public String getNamespace() {
        if (null == header) return null;
        return header.namespace;
    }

    public String getName() {
        if (null == header) return null;
        return header.name;
    }

    public String getPayloadVersion() {
        if (null == header) return null;
        return header.payloadVersion;
    }

    public String getMessageId() {
        if (null == header) return null;
        return header.messageId;
    }

    public String getAccessToken() {
        if (null == payload) return null;
        return payload.accessToken;
    }

    public String getOpenUid() {
        if (null == payload) return null;
        return payload.openUid;
    }

    @Setter
    @Getter
    public static class Payload {
        String accessToken;
        String openUid;
    }
}
