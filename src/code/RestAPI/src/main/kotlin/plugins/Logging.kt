package api.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.doublereceive.*
import org.slf4j.event.Level

fun Application.configureLogging(){
    install(CallId)
    install(DoubleReceive)
    install(CallLogging) {
        level = Level.TRACE
    }
}