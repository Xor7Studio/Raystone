package cn.xor7.raystone

import cn.xor7.raystone.event.Event
import cn.xor7.raystone.event.Level
import cn.xor7.raystone.event.Listener
import cn.xor7.raystone.event.Subscribe
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.jvmErasure

object Raystone {
    private var initialized = false
    private val listeners = mutableMapOf<String, Listener>()
    private val eventHandlers = mutableMapOf<String, Map<Level, MutableSet<KFunction<*>>>>()
    fun init() {
        if (initialized) {
            return
        }
        initialized = true
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

    fun onReceiveEvent(name: String, event: Event) {
        val handlers = eventHandlers[name] ?: return
        onReceiveEvent0(handlers, Level.LOWEST, event)
        onReceiveEvent0(handlers, Level.LOW, event)
        onReceiveEvent0(handlers, Level.MEDIUM, event)
        onReceiveEvent0(handlers, Level.HIGH, event)
        onReceiveEvent0(handlers, Level.HIGHEST, event)
    }

    private fun onReceiveEvent0(allHandlers: Map<Level, MutableSet<KFunction<*>>>, level: Level, event: Event) {
        val handlers = allHandlers[level] ?: return
        for (handler in handlers) {
            handler.call(listeners[handler.parameters[0].type.jvmErasure.qualifiedName], event)
        }
    }
}