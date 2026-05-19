package com.six.iot.sample.skill.discover;

import com.six.iot.sample.skill.Appliance;
import com.six.iot.sample.skill.SkillResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Check the doc:
 * https://developer.dueros.baidu.com/didp/doc/dueros-bot-platform/dbp-smart-home/protocol/discovery-message_markdown
 */
@Setter
@Getter
public class DiscoverPayload extends SkillResponse.Payload {

    protected List<Appliance> discoveredAppliances;

    protected List<DiscoveredGroup> discoveredGroups;
}
