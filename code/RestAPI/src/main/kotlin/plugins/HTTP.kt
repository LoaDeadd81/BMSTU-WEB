package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.configureHTTP() {
//    routing {
//        openAPI(path = "openapi", swaggerFile = "RestAPI/resources/openapi/documentation.yaml")
//    }
    routing {
        swaggerUI(path = "swagger", swaggerFile = "RestAPI/resources/openapi/documentation.yaml")
    }
}