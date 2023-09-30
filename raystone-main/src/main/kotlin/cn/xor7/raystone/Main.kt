package cn.xor7.raystone

import cn.hutool.log.Log
import cn.hutool.log.LogFactory
import cn.xor7.raystone.web.Webserver
import oshi.util.GlobalConfig

val logger: Log = LogFactory.get()
fun main() {
    // setup Raystone API
    Raystone.init(Raystone.Environment.SERVER)

    // set OSHI config
    GlobalConfig.set(GlobalConfig.OSHI_OS_WINDOWS_CPU_UTILITY, true)
    GlobalConfig.set(GlobalConfig.OSHI_OS_WINDOWS_PERFOS_DIABLED, true)

    // start Webserver
    Webserver()
}