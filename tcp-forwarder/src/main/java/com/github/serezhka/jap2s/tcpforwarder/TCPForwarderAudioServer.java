package com.github.serezhka.jap2s.tcpforwarder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
@Component
public class TCPForwarderAudioServer extends SimpleChannelInboundHandler<ByteBuf> {

    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private final Class<? extends ServerSocketChannel> serverSocketChannelClass;

    private ChannelHandlerContext ctx;

    @Value("${tcp.forwarder.audio.port}")
    private int port;

    @Autowired
    public TCPForwarderAudioServer(EventLoopGroup bossGroup,
                                   EventLoopGroup workerGroup,
                                   Class<? extends ServerSocketChannel> serverSocketChannelClass) throws IOException {
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
        this.serverSocketChannelClass = serverSocketChannelClass;
    }

    @PostConstruct
    public void postConstruct() {
        /*if (!FdkAacLib.isInitialized()) {
            return;
        }*/
        new Thread(() -> {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            try {
                serverBootstrap.group(bossGroup, workerGroup)
                        .channel(serverSocketChannelClass)
                        .localAddress(new InetSocketAddress(port))
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(final SocketChannel ch) {
                                ch.pipeline().addLast(TCPForwarderAudioServer.this);
                            }
                        });
                log.info("Starting TCP socket audio server on port {}", port);
                serverBootstrap.bind().sync().channel().closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        log.info("TCP audio receiver connected!");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        this.ctx = null;
        log.info("TCP audio receiver disconnected!");
    }

    public void onAudioData(byte[] data) {
        /*if (FdkAacLib.isInitialized()) {
            byte[] audioDecoded = new byte[480 * 4];
            FdkAacLib.decodeFrame(data, audioDecoded);
            sendData(Unpooled.wrappedBuffer(audioDecoded));
        }*/
        sendData(Unpooled.wrappedBuffer(data));
    }

    private void sendData(ByteBuf message) {
        if (ctx != null) {
            ctx.executor().execute(() -> ctx.writeAndFlush(message.retain()));
            //ctx.writeAndFlush(message.retain());
        } else message.release();
    }
}

