package cn.xor7.raystone

import cn.xor7.raystone.web.Webserver
import oshi.util.GlobalConfig

fun main() {
    // setup Raystone API
    Raystone.init(Raystone.Environment.SERVER)

    // set OSHI config
    GlobalConfig.set(GlobalConfig.OSHI_OS_WINDOWS_CPU_UTILITY, true)
    GlobalConfig.set(GlobalConfig.OSHI_OS_WINDOWS_PERFOS_DIABLED, true)

    // start Webserver
    Webserver()
}