package com.github.serezhka.jap2s.gst;

import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Version;
import org.freedesktop.gstreamer.glib.GLib;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:gst-player.properties")
@ComponentScan("com.github.serezhka.jap2s")
public class GstPlayerConfig {

    static {
        GstPlayerUtils.configurePaths();
        GLib.setEnv("GST_DEBUG", "3", true);
        Gst.init(Version.of(1, 10), "BasicPipeline");
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}