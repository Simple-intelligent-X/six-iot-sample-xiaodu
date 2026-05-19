package com.six.iot.sample.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/health")
public class ProbeController {

    private static Logger Log = LoggerFactory.getLogger(ProbeController.class);

    @RequestMapping(value = "/probe", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> probe() {
        Map<String, Object> map = new HashMap<>();
        return map;
    }
}
