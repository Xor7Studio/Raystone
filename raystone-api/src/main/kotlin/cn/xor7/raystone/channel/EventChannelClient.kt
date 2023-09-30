package cn.xor7.raystone.channel

import cn.xor7.raystone.Raystone
import cn.xor7.raystone.event.ChannelClientInitEvent
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.util.concurrent.GenericFutureListener


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
                        ch.pipeline().addLast(LengthFieldBasedFrameDecoder(Int.MAX_VALUE, 1, 4))
                        ch.pipeline().addLast(EventChannelHandler)
                    }
                })
        } finally {
            eventLoopGroup.shutdownGracefully()
        }
    }

    fun channel(): Channel {
        return channel
    }

    fun connect(host: String, port: Int) {
        bootstrap.connect(host, port).addListener(GenericFutureListener { future: ChannelFuture ->
            if (future.isSuccess) {
                channel = future.channel()
                Raystone.emitEvent(ChannelClientInitEvent(""))
            } else {
                // TODO
            }
        })
    }
}