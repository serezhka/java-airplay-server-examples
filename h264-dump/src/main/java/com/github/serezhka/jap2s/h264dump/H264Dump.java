package com.github.serezhka.jap2s.h264dump;

import com.github.serezhka.fdkaacjni.FdkAacLib;
import com.github.serezhka.jap2lib.rtsp.AudioStreamInfo;
import com.github.serezhka.jap2lib.rtsp.VideoStreamInfo;
import com.github.serezhka.jap2server.AirplayDataConsumer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
public class H264Dump implements AirplayDataConsumer {

    private final FileChannel videoFileChannel;
    private FileChannel audioFileChannel;

    public H264Dump(String videoDumpName, String audioDumpName) throws IOException {
        videoFileChannel = FileChannel.open(Paths.get(videoDumpName), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        if (FdkAacLib.isInitialized()) {
            audioFileChannel = FileChannel.open(Paths.get(audioDumpName), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        }
    }

    @Override
    public void onVideo(byte[] video) {
        try {
            videoFileChannel.write(ByteBuffer.wrap(video));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAudio(byte[] audio) {
        try {
            audioFileChannel.write(ByteBuffer.wrap(audio));
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*if (FdkAacLib.isInitialized()) {
            byte[] audioDecoded = new byte[480 * 4];
            FdkAacLib.decodeFrame(audio, audioDecoded);

            try {
                audioFileChannel.write(ByteBuffer.wrap(audioDecoded));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public void onVideoFormat(VideoStreamInfo videoStreamInfo) {
    }

    @Override
    public void onAudioFormat(AudioStreamInfo audioInfo) {
    }

    public void save() throws IOException {
        videoFileChannel.close();
        audioFileChannel.close();
    }
}
