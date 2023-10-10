package api

import api.plugins.configureStatusCodes
import api.plugins.configureValidation
import com.example.plugins.configureHTTP
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


class RestApi {
    fun run() {
        embeddedServer(Netty, port = 8089, host = "0.0.0.0", module = Application::module)
            .start(wait = true)
    }
}


fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureRouting()
    configureValidation()
    configureStatusCodes()
}
