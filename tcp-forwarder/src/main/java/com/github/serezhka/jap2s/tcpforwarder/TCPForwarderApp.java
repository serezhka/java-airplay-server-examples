package com.github.serezhka.jap2s.tcpforwarder;

import com.github.serezhka.jap2lib.rtsp.AudioStreamInfo;
import com.github.serezhka.jap2lib.rtsp.VideoStreamInfo;
import com.github.serezhka.jap2server.AirPlayServer;
import com.github.serezhka.jap2server.AirplayDataConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@SpringBootApplication
public class TCPForwarderApp {

    private final AirPlayServer airPlayServer;

    @Autowired
    public TCPForwarderApp(@Value("${server.name}") String serverName,
                           @Value("${airplay.port}") int airPlayPort,
                           @Value("${airtunes.port}") int airTunesPort,
                           TCPForwarderAudioServer tcpForwarderAudioServer,
                           TCPForwarderVideoServer tcpForwarderVideoServer) {
        AirplayDataConsumer mirrorDataConsumer = new AirplayDataConsumer() {
            @Override
            public void onVideo(byte[] video) {
                tcpForwarderVideoServer.onVideoData(video);
            }

            @Override
            public void onAudio(byte[] audio) {
                tcpForwarderAudioServer.onAudioData(audio);
            }

            @Override
            public void onVideoFormat(VideoStreamInfo videoStreamInfo) {
            }

            @Override
            public void onAudioFormat(AudioStreamInfo audioInfo) {
            }
        };

        airPlayServer = new AirPlayServer(serverName, airPlayPort, airTunesPort, mirrorDataConsumer);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(TCPForwarderApp.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .run(args);
    }

    @PostConstruct
    private void postConstruct() throws Exception {
        airPlayServer.start();
        log.info("AirPlay server started!");
    }

    @PreDestroy
    private void preDestroy() {
        airPlayServer.stop();
        log.info("AirPlay server stopped!");
    }
}
