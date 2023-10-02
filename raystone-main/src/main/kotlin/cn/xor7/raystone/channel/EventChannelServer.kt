package cn.xor7.raystone.channel

import cn.hutool.log.LogFactory
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import java.net.BindException

object EventChannelServer {
    private val bossGroup = NioEventLoopGroup()
    private val workerGroup = NioEventLoopGroup()
    private val logger = LogFactory.get()
    lateinit var channel: Channel
        private set

    fun start(host: String, port: Int) {
        try {
            val bootstrap = ServerBootstrap()
            bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java)
                .childHandler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(ServerHandler)
                    }
                })

            val channelFuture: ChannelFuture = bootstrap.bind(port).sync()
            if (channelFuture.isSuccess) {
                logger.info("EventChannelServer start on $host:$port.")
            } else {
                throw BindException("Cannot bind to $host:$port.")
            }

            channel = channelFuture.channel()
            channel.closeFuture().sync()
        } finally {
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
        }
    }
}