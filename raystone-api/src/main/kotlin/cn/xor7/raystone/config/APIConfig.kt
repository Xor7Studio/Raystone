package cn.xor7.raystone.config

import kotlinx.serialization.Serializable

@Serializable
data class APIConfig(
    var uuid: String = "",
    var serverHost: String = "localhost",
    var serverPort: Int = 815
)
