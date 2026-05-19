package com.six.iot.sample.skill;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Check the doc:
 * https://developer.dueros.baidu.com/didp/doc/dueros-bot-platform/dbp-smart-home/protocol/discovery-message_markdown
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplianceAttribute {

    protected String name;

    protected Object value;

    protected String scale = "";

    protected long timestampOfSample;

    protected int uncertaintyInMilliseconds;

    protected String legalValue;

    public ApplianceAttribute() {
    }

    public ApplianceAttribute(String name, Object value) {
        this.name = name;
        this.value = value;
        this.timestampOfSample = System.currentTimeMillis();
        this.uncertaintyInMilliseconds = 10;
    }

    public ApplianceAttribute(String name, Object value, String legalValue) {
        this.name = name;
        this.value = value;
        this.legalValue = legalValue;
    }

    public ApplianceAttribute(String name, Object value, int uncertaintyInMilliseconds) {
        this.name = name;
        this.value = value;
        this.timestampOfSample = System.currentTimeMillis();
        this.uncertaintyInMilliseconds = uncertaintyInMilliseconds;
    }

    public ApplianceAttribute(String name, Object value, long timestampOfSample, int uncertaintyInMilliseconds) {
        this.name = name;
        this.value = value;
        this.timestampOfSample = timestampOfSample;
        this.uncertaintyInMilliseconds = uncertaintyInMilliseconds;
    }

    public ApplianceAttribute(String name, Object value, String scale, long timestampOfSample, int uncertaintyInMilliseconds) {
        this.name = name;
        this.value = value;
        this.scale = scale;
        this.timestampOfSample = timestampOfSample;
        this.uncertaintyInMilliseconds = uncertaintyInMilliseconds;
    }
}
