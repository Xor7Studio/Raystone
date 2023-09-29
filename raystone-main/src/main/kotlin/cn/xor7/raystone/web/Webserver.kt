package cn.xor7.raystone.web

import cn.hutool.log.Log
import cn.hutool.log.LogFactory
import cn.xor7.raystone.web.api.API
import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import org.reflections.Reflections
import java.util.*

class Webserver {
    private val logger: Log = LogFactory.get()
    private val app: Javalin = Javalin.create { config -> // 设置SPA
        config.staticFiles.add { staticFiles ->
            staticFiles.hostedPath = "/"
            staticFiles.directory = "/public"
        }
        config.plugins.enableCors { cors -> // 设置CORS规则以允许任何地址的调用
            cors.add {
                it.anyHost()
            }
        }
    }.after("/api/*") { ctx -> // 处理API请求
        logger.info("Received request: ${ctx.method().name} ${ctx.path()}")
        val path = ctx.path().removePrefix("/api")
        handle(ctx, path, ctx.method().name)
    }
    private val handlers = mutableMapOf<String, Any>()

    init {
        val classes = Reflections("cn.xor7.raystone.web.api")
            .getTypesAnnotatedWith(API::class.java)
        for (cls in classes) {
            logger.info("Apply API class: ${cls.`package`}")
            val path = cls
                .`package`
                .toString()
                .removePrefix("package cn.xor7.raystone.web.api")
                .lowercase(Locale.getDefault())
                .replace(".", "/") +
                    cls.getAnnotation(API::class.java).path
            handlers[path] = cls.getConstructor().newInstance()
        }
        app.start(8080)
    }

    private fun handle(ctx: Context, path: String, method: String) {
        if (path == "/i-want-a-cup-of-coffee") { // 彩蛋
            ctx.status(HttpStatus.IM_A_TEAPOT)
            return
        }
        val instance = handlers[path] ?: return // 由于使用了After方法进行处理，所以Status默认就是NOT_FOUND
        val callable = instance::class.members.find { it.name == method.lowercase(Locale.getDefault()) }
        if (callable == null) {
            ctx.status(HttpStatus.METHOD_NOT_ALLOWED)
            return
        }
        try {
            ctx.status(HttpStatus.OK) // Status默认设置为OK
            callable.call(instance, ctx)
        } catch (e: Exception) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}