package cn.xor7.raystone

import cn.xor7.raystone.channel.EventChannelServer
import cn.xor7.raystone.web.Webserver
import oshi.util.GlobalConfig

fun main() {
    // setup Raystone API
    Raystone.init(Raystone.Environment.SERVER)

    // set OSHI config
    GlobalConfig.set(GlobalConfig.OSHI_OS_WINDOWS_CPU_UTILITY, true)
    GlobalConfig.set(GlobalConfig.OSHI_OS_WINDOWS_PERFOS_DIABLED, true)

    // start Webserver
    Webserver.start("127.0.0.1", 8080)

    // start EventChannelServer
    EventChannelServer.start("127.0.0.1",815)
}