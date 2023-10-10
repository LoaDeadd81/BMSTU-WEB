package com.example.plugins

import api.routes.auth
import api.routes.ingredients
import api.routes.recipes
import api.routes.users
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.configureRouting() {
    install(Resources)
    routing {
        route("/api/v1") {
            users()
            ingredients()
            recipes()
            auth()
        }
    }
}
