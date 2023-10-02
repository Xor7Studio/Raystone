package cn.xor7.raystone

import cn.xor7.raystone.Raystone.Channel.*
import cn.xor7.raystone.channel.EventChannelClient
import cn.xor7.raystone.config.APIConfig
import cn.xor7.raystone.event.Level
import cn.xor7.raystone.event.Listener
import cn.xor7.raystone.event.Subscribe
import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import io.netty.buffer.ByteBuf
import net.peanuuutz.tomlkt.Toml
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.*
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.jvmErasure

object Raystone {
    private var initialized = false
    private val listeners = mutableMapOf<String, Listener>()
    private val eventHandlers = mutableMapOf<String, Map<Level, MutableSet<KFunction<*>>>>()
    val GSON = GsonBuilder()
        .setNumberToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
        .create()!!
    val TOML = Toml {
        ignoreUnknownKeys = true
    }
    lateinit var environment: Environment
        private set
    lateinit var apiConfig: APIConfig
        private set

    const val API_CONFIG_FILE_PATH = ".raystone/api.toml"

    fun init(environment: Environment = Environment.CLIENT) {
        this.environment = environment
        if (initialized) {
            return
        }
        initialized = true

        rereadApiConfig()
        if (apiConfig.uuid == "") {
            apiConfig.uuid = UUID.randomUUID().toString()
        }
        overwriteApiConfig()

        if (environment == Environment.CLIENT) {
            println("[Raystone API] Init EventChannelClient.")
            EventChannelClient.connect(apiConfig.serverHost, apiConfig.serverPort)
        }
    }

    fun rereadApiConfig() {
        apiConfig = Toml.decodeFromString(APIConfig.serializer(), getPath(API_CONFIG_FILE_PATH).readText())
    }

    fun overwriteApiConfig() {
        getPath(API_CONFIG_FILE_PATH).writeText(TOML.encodeToString(APIConfig.serializer(), apiConfig))
    }

    private fun getPath(path: String): Path {
        val result: Path = Paths.get(path)
        if (!result.exists() || result.isDirectory()) {
            result.createFile()
        }
        return result
    }

    fun registerListener(listener: Listener) {
        listeners[listener.javaClass.name] = listener
        val functions = listener::class.declaredMemberFunctions
        for (function in functions) {
            val annotations = function.annotations
            for (annotation in annotations) {
                if (annotation is Subscribe) {
                    val parameters = function.parameters
                    if (parameters.size != 2) {
                        return
                    }
                    val eventName = parameters[1].type.jvmErasure.qualifiedName ?: return
                    if (!eventHandlers.containsKey(eventName)) {
                        eventHandlers[eventName] = mapOf(
                            Pair(Level.LOWEST, mutableSetOf()),
                            Pair(Level.LOW, mutableSetOf()),
                            Pair(Level.MEDIUM, mutableSetOf()),
                            Pair(Level.HIGH, mutableSetOf()),
                            Pair(Level.HIGHEST, mutableSetOf()),
                        )
                    }
                    eventHandlers[eventName]?.get(annotation.level)?.add(function)
                }
            }
        }
    }

    fun emitEvent(event: Any, channel: Channel = BOTH) {
        when (channel) {
            LOCAL -> emitEventLocal(event)
            REMOTE -> emitEventRemote(event)
            BOTH -> {
                emitEventLocal(event)
                emitEventRemote(event)
            }
        }
    }

    private fun emitEventRemote(event: Any) {
        val data = "${event.javaClass.name}@${GSON.toJson(event)}"
        val byteBuf: ByteBuf = EventChannelClient.channel.alloc().buffer()
        byteBuf.writeByte(8)
        byteBuf.writeInt(data.length)
        byteBuf.writeBytes(data.toByteArray(Charsets.UTF_8))
        EventChannelClient.channel.writeAndFlush(byteBuf)
    }

    private fun emitEventLocal(event: Any) {
        val handlers = eventHandlers[event.javaClass.name] ?: return
        emitEventLocal(handlers, Level.LOWEST, event)
        emitEventLocal(handlers, Level.LOW, event)
        emitEventLocal(handlers, Level.MEDIUM, event)
        emitEventLocal(handlers, Level.HIGH, event)
        emitEventLocal(handlers, Level.HIGHEST, event)
    }

    private fun emitEventLocal(allHandlers: Map<Level, MutableSet<KFunction<*>>>, level: Level, event: Any) {
        val handlers = allHandlers[level] ?: return
        for (handler in handlers) {
            handler.call(listeners[handler.parameters[0].type.jvmErasure.qualifiedName], event)
        }
    }

    enum class Environment {
        CLIENT,
        SERVER
    }

    enum class Channel {
        LOCAL,
        REMOTE,
        BOTH
    }
}