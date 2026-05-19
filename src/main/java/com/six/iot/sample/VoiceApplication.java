package com.six.iot.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VoiceApplication {

    private final static Logger Log = LoggerFactory.getLogger(VoiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(VoiceApplication.class, args);
        Log.info("VoiceApplication for XiaoDu integration is started.");
    }
}




