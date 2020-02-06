package com.github.serezhka.jap2s.h264dump;

import com.github.serezhka.jap2server.AirPlayServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@SpringBootApplication
public class H264DumpApp {

    private final AirPlayServer airPlayServer;
    private final H264Dump h264Dump;

    @Autowired
    public H264DumpApp(@Value("${server.name}") String serverName,
                       @Value("${airplay.port}") int airPlayPort,
                       @Value("${airtunes.port}") int airTunesPort,
                       H264Dump h264Dump) {
        this.h264Dump = h264Dump;
        this.airPlayServer = new AirPlayServer(serverName, airPlayPort, airTunesPort, h264Dump);
    }

    public static void main(String[] args) {
        SpringApplication.run(H264DumpApp.class, args);
    }

    @PostConstruct
    private void postConstruct() throws Exception {
        airPlayServer.start();
    }

    @PreDestroy
    private void preDestroy() throws IOException {
        h264Dump.save();
        airPlayServer.stop();
    }
}
