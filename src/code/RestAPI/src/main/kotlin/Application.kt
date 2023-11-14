package api

import api.plugins.configureGQL
import api.plugins.configureLogging
import api.plugins.configureStatusCodes
import api.plugins.configureValidation
import com.example.plugins.configureHTTP
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


class RestApi {
    companion object {
        val host = "0.0.0.0";
        var port = System.getenv("PORT")?.toInt() ?: throw Exception("no PORT in env")
    }

    fun run() {
        val port = System.getenv("PORT")?.toInt() ?: throw Exception("no PORT in env")

        embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module).start(wait = true)
    }
}


fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureRouting()
    configureValidation()
    configureStatusCodes()
    configureLogging()
    configureGQL()
}
