package cn.xor7.raystone.channel

import cn.xor7.raystone.Raystone
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

@Sharable
object EventChannelHandler : SimpleChannelInboundHandler<ByteBuf>() {
    override fun channelRead0(ctx: ChannelHandlerContext?, msg: ByteBuf) {
        if (msg.readByte() != 8.toByte()) { // MagicNumber
            return
        }
        val bytes = ByteArray(msg.readableBytes())
        msg.readBytes(bytes)
        msg.release()
        val data = String(bytes,Charsets.UTF_8).split(Regex("@"), 1)
        if (data.size != 2) {
            return
        }
        Raystone.emitEvent(Raystone.GSON.fromJson(data[1], Class.forName(data[0])), Raystone.Channel.LOCAL)
    }
}