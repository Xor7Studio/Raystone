package cn.xor7.raystone.config

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import java.io.File
import java.io.IOException

class TomlEx<T>(filePath: String, private val clazz: Class<T>) : Toml() {
    private val file: File
    private val tomlWriter: TomlWriter = TomlWriter()
    private var data: T? = null
        get() = field

    init {
        file = File(filePath)
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs()
                if (!file.createNewFile()) {
                    throw Error("Can not create config file: $filePath")
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
        read()
        save()
    }

    fun read(): TomlEx<T> {
        data = read(file).to(clazz)
        return this
    }

    fun save(): TomlEx<T> {
        try {
            tomlWriter.write(data, file)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        return this
    }
}