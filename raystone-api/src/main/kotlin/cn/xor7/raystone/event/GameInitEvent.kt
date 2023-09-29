package cn.xor7.raystone.event

import cn.xor7.raystone.event.Event

data class GameInitEvent(
    val gameName: String
) : Event()
