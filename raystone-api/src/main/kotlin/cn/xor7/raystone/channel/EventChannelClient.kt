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
import java.util.concurrent.TimeUnit
import kotlin.math.pow


object EventChannelClient {
    private var eventLoopGroup: EventLoopGroup = NioEventLoopGroup()
    private var retryAttempts = 0
    private lateinit var bootstrap: Bootstrap
    lateinit var channel: Channel
        private set

    init {
        initBootstrap()
    }

    fun initBootstrap() {
        bootstrap = Bootstrap()
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
    }

    fun connect(host: String, port: Int) {
        try {
            bootstrap.connect(host, port).addListener(GenericFutureListener { future: ChannelFuture ->
                if (future.isSuccess) {
                    channel = future.channel()
                    Raystone.emitEvent(ChannelClientInitEvent(Raystone.apiConfig.uuid))
                } else {
                    retryAttempts++
                    if (retryAttempts <= Raystone.apiConfig.maxRetryAttempts) {
                        val delaySeconds = 2.0.pow(retryAttempts.toDouble()).toLong()
                        println("Connection attempt $retryAttempts failed. Retrying in $delaySeconds seconds...")
                        eventLoopGroup.shutdownGracefully()
                        eventLoopGroup = NioEventLoopGroup()
                        initBootstrap()
                        eventLoopGroup.schedule({ connect(host, port) }, delaySeconds, TimeUnit.SECONDS)
                    } else {
                        println("Max retry attempts reached. Connection failed.")
                    }
                }
            })
        } finally {
            eventLoopGroup.shutdownGracefully()
        }
    }
}