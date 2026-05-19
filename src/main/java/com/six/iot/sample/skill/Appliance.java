package com.six.iot.sample.skill;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Check the doc:
 * <a href="https://developer.dueros.baidu.com/didp/doc/dueros-bot-platform/dbp-smart-home/protocol/discovery-message_markdown">...</a>
 */

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Appliance {

    protected List<String> actions;

    protected List<String> applianceTypes;

    protected Map<String, String> additionalApplianceDetails;

    protected String friendlyDescription;

    protected String friendlyName;

    @JsonProperty("isReachable")
    protected boolean isReachable;

    protected String manufacturerName;

    protected String modelName;

    protected String version;

    protected String applianceId;

    protected List<ApplianceAttribute> attributes;
}
