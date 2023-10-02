package cn.xor7.raystone.config

import kotlinx.serialization.Serializable

@Serializable
data class APIConfig(
    var uuid: String = "",
    var serverHost: String = "127.0.0.1",
    var serverPort: Int = 815,
    var maxRetryAttempts: Int = 5
)
