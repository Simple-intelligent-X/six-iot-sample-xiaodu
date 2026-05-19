package com.six.iot.sample.skill.discover;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Check the doc:
 * https://developer.dueros.baidu.com/didp/doc/dueros-bot-platform/dbp-smart-home/protocol/discovery-message_markdown
 */
@Setter
@Getter
public class DiscoveredGroup {
    protected String groupName;

    protected List<String> applianceIds = new ArrayList<>();

    protected String groupNotes;

    protected Map additionalGroupDetails = new HashMap();
}
