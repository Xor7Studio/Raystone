package cn.xor7.raystone.web.api.system

import cn.hutool.system.oshi.OshiUtil
import cn.xor7.raystone.web.api.API
import com.alibaba.fastjson.annotation.JSONField
import com.alibaba.fastjson2.JSON
import io.javalin.http.Context
import java.text.DecimalFormat


@API("/info")
class Info {
    fun get(ctx: Context) {
        val cpuInfo = OshiUtil.getCpuInfo()
        val processor = OshiUtil.getProcessor()
        var totalMemory = 0.00
        var memorySpeed = 0
        var memoryType = ""
        for (physicalMemory in OshiUtil.getMemory().physicalMemory) { // 因为直接调用API获取的内存容量错误，所以使用理论内存容量计算
            memoryType = physicalMemory.memoryType
            memorySpeed = (physicalMemory.clockSpeed / 1e6).toInt()
            totalMemory += physicalMemory.capacity / 1073741824 // 1073741824 = 1024 * 1024 * 1024 ，下同
        }
        val result = JSON.toJSONString(
            Data(
                cpu = CPUData(
                    name = cpuInfo.cpuModel.split("\n")[0].trim(), // 去除多余信息
                    used = DecimalFormat("#.00") // 保留两位小数，下同
                        .format(100.0 - cpuInfo.free)
                        .toDouble(),
                    coreNum = processor.physicalProcessorCount,
                    threadNum = processor.logicalProcessorCount
                ),
                memory = MemoryData(
                    total = totalMemory,
                    used = DecimalFormat("#.00")
                        .format(totalMemory - OshiUtil.getMemory().available / 1073741824)
                        .toDouble(),
                    type = memoryType,
                    speed = memorySpeed
                )
            )
        )
        ctx.result(result)
    }

    private data class CPUData(
        val name: String,
        val used: Double,
        @JSONField(name = "core_num")
        val coreNum: Int,
        @JSONField(name = "thread_num")
        val threadNum: Int
    )

    private data class MemoryData(
        val total: Double,
        val used: Double,
        val type: String,
        val speed: Int
    )

    private data class Data(
        val cpu: CPUData,
        val memory: MemoryData
    )
}