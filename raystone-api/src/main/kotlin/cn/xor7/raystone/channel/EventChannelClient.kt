package cn.xor7.raystone.channel

import cn.xor7.raystone.Raystone
import cn.xor7.raystone.Raystone.LOG_PREFIX
import cn.xor7.raystone.event.lifecycle.ChannelInitializeEvent
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import java.util.concurrent.TimeUnit
import kotlin.math.pow


object EventChannelClient {
    private var retryAttempts = 0
    private lateinit var eventLoopGroup: EventLoopGroup
    private lateinit var bootstrap: Bootstrap
    lateinit var channel: Channel
        private set

    fun connect(host: String, port: Int, delay: Long = 0) {
        eventLoopGroup = NioEventLoopGroup()
        eventLoopGroup.schedule({ connect0(host, port) }, delay, TimeUnit.SECONDS)
    }

    private fun connect0(host: String, port: Int) {
        try {
            bootstrap = Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel::class.java)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
                .handler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(LengthFieldBasedFrameDecoder(Int.MAX_VALUE, 1, 4))
                        ch.pipeline().addLast(EventChannelHandler)
                    }
                })

            val channelFuture: ChannelFuture = bootstrap.connect(host, port)

            channelFuture.addListener { future ->
                if (future.isSuccess) {
                    channel = channelFuture.channel()
                    Raystone.emitEvent(ChannelInitializeEvent(Raystone.apiConfig.uuid), Raystone.Channel.REMOTE)
                    println("$LOG_PREFIX Connection successful")
                } else {
                    retryAttempts++
                    if (retryAttempts <= Raystone.apiConfig.maxRetryAttempts) {
                        val delaySeconds = 2.0.pow(retryAttempts.toDouble()).toLong()
                        println("$LOG_PREFIX Connection attempt $retryAttempts failed. Retrying in $delaySeconds seconds...")
                        connect(host, port, delaySeconds)
                    } else {
                        println("$LOG_PREFIX Max retry attempts reached. Connection failed.")
                    }
                }
            }.sync()
        } finally {
            eventLoopGroup.shutdownGracefully()
        }

    }
}