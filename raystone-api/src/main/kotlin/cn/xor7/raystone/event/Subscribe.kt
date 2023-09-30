package cn.xor7.raystone.event

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Subscribe(val level: Level = Level.MEDIUM)


