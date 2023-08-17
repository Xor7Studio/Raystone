import io.javalin.Javalin
import io.javalin.config.JavalinConfig
import java.util.function.Consumer

class Webserver(config: Consumer<JavalinConfig>) {
    private val app: Javalin

    init {
        app = Javalin.create(config)
    }
}