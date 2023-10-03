package cn.xor7.raystone.event

interface Filter {
    fun pass(event: Any): Boolean
}