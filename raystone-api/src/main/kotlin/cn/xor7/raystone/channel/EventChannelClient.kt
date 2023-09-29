package cn.xor7.raystone.channel

import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder


object EventChannelClient {
    private val eventLoopGroup: EventLoopGroup = NioEventLoopGroup()
    private val bootstrap: Bootstrap = Bootstrap()
    private lateinit var channel: Channel

    init {
        try {
            bootstrap
                .group(eventLoopGroup)
                .channel(NioSocketChannel::class.java)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(LengthFieldBasedFrameDecoder(Int.MAX_VALUE,1,4))
                        ch.pipeline().addLast(EventChannelHandler)

                    }
                })
        } finally {
            eventLoopGroup.shutdownGracefully()
        }
    }

    fun connect() {
        connect("localhost", 815)
    }

    fun connect(host: String, port: Int) {
        channel = bootstrap.connect(host, port).sync().channel()
    }
}