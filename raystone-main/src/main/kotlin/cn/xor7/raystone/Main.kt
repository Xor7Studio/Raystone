package cn.xor7.raystone

import cn.hutool.log.Log
import cn.hutool.log.LogFactory
import cn.xor7.raystone.web.Webserver
import oshi.util.GlobalConfig

val logger: Log = LogFactory.get()
fun main() {
    GlobalConfig.set(GlobalConfig.OSHI_OS_WINDOWS_CPU_UTILITY, true)
    GlobalConfig.set(GlobalConfig.OSHI_OS_WINDOWS_PERFOS_DIABLED, true)
    Webserver()
}