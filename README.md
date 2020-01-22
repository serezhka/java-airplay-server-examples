# java-airplay-server-examples

[![Build Status](https://travis-ci.com/serezhka/java-airplay-server-examples.svg?branch=master)](https://travis-ci.com/serezhka/java-airplay-server-examples) [![HitCount](http://hits.dwyl.io/serezhka/java-airplay-server-examples.svg)](http://hits.dwyl.io/serezhka/java-airplay-server-examples)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](http://opensource.org/licenses/MIT)

All examples were tested with iPhone X (iOS 13.3)

## tcp-forwarder

Forwards mirror data to TCP

Play it with [GStreamer](https://gstreamer.freedesktop.org/) or [FFmpeg](https://www.ffmpeg.org/)

```Shell
cd tcp-forwarder/

gradle bootRun

gst-launch-1.0 -v tcpclientsrc port=5002 ! h264parse ! avdec_h264 ! autovideosink

or 

ffplay -f h264 -codec:v h264 -i tcp://localhost:5002 -v debug
```

<img src="https://github.com/serezhka/java-airplay-server-examples/blob/media/gstreamer_playback.gif" width="600">

## h264-dump

Saves mirror data stream to h264 file

[H264Dump.java](https://github.com/serezhka/java-airplay-server/blob/master/h264-dump/src/main/java/com/github/serezhka/jap2s/h264dump/H264Dump.java)

## vlcj-player

Playback screen mirroring in embedded vlc

[VLCJPlayer.java](https://github.com/serezhka/java-airplay-server/blob/master/vlcj-player/src/main/java/com/github/serezhka/jap2s/vlcj/VLCJPlayer.java)

<img src="https://github.com/serezhka/java-airplay-server/blob/media/vlcj_player_demo.gif" width="600">

## jmuxer-player

Playback screen mirroring with [jmuxer](https://github.com/samirkumardas/jmuxer)

[JMuxerWebSocketServer.java](https://github.com/serezhka/java-airplay-server/blob/master/jmuxer-player/src/main/java/com/github/serezhka/jap2s/jmuxer/JMuxerWebSocketServer.java)

[index-h264.html](https://github.com/serezhka/java-airplay-server/blob/master/index-h264.html)

<img src="https://github.com/serezhka/java-airplay-server/blob/media/jmuxer_player_demo.gif" width="600">
