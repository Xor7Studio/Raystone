package cn.xor7.raystone.channel

import cn.xor7.raystone.Raystone
import cn.xor7.raystone.event.GameInitEvent
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
        val data = String(bytes).split(Regex("@"), 1)
        if (data.size != 2) {
            return
        }
        val name = data[0]
        val eventData = data[1]
        // TODO 反序列化
        Raystone.onReceiveEvent(name,GameInitEvent("fff"))
    }
}