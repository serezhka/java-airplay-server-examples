# java-airplay-server-examples

[![Build Status](https://travis-ci.com/serezhka/java-airplay-server-examples.svg?branch=master)](https://travis-ci.com/serezhka/java-airplay-server-examples) [![HitCount](http://hits.dwyl.io/serezhka/java-airplay-server-examples.svg)](http://hits.dwyl.io/serezhka/java-airplay-server-examples)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](http://opensource.org/licenses/MIT)

All examples were tested with iPhone X (iOS 14.0.1)

## tcp-forwarder

Forwards video and audio data to TCP

Play it with [GStreamer](https://gstreamer.freedesktop.org/) or [FFmpeg](https://www.ffmpeg.org/)

```Shell
cd tcp-forwarder/

gradle bootRun

gst-launch-1.0 -v tcpclientsrc port=5002 ! h264parse ! avdec_h264 ! autovideosink

or 

ffplay -f h264 -codec:v h264 -i tcp://localhost:5002 -v debug

ffplay -autoexit -f s16le -ar 44100 -ac 2 tcp://localhost:5003
```

You need to compile [lib-fdk-aac](https://github.com/serezhka/fdk-aac-jni) for aac-eld decoding

<img src="https://github.com/serezhka/java-airplay-server-examples/blob/media/gstreamer_playback.gif" width="600">

## h264-dump

Saves video data stream to .h264 file, decoded audio to .pcm file

```Shell
cd h264-dump/

gradle bootRun

ffplay -autoexit -f s16le -ar 44100 -ac 2 dump.pcm
```

You need to compile [lib-fdk-aac](https://github.com/serezhka/fdk-aac-jni) for aac-eld decoding

## vlcj-player

Playback screen mirroring in embedded vlc

cd vlcj-player/

gradle bootRun

<img src="https://github.com/serezhka/java-airplay-server/blob/media/vlcj_player_demo.gif" width="600">

## jmuxer-player

Playback screen mirroring with [jmuxer](https://github.com/samirkumardas/jmuxer)

cd vlcj-player/

gradle bootRun

open index-h264.html in browser

<img src="https://github.com/serezhka/java-airplay-server/blob/media/jmuxer_player_demo.gif" width="600">
