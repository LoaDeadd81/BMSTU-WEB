package api.plugins

import bl.exceptions.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusCodes() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            if (cause is RequestValidationException) {
                call.respond(HttpStatusCode.BadRequest, cause.message ?: "")
            } else if (cause is ValidationException) {
                call.respond(HttpStatusCode.BadRequest, cause.message ?: "")
            } else if (cause is NotFoundException) {
                call.respond(HttpStatusCode.NotFound, cause.message ?: "")
            } else if (cause is AccessDeniedException) {
                call.respond(HttpStatusCode.Forbidden, cause.message ?: "")
            } else if (cause is AlreadyExistException) {
                call.respond(HttpStatusCode.Conflict, cause.message ?: "")
            } else if (cause is LogInFailedException) {
                call.respond(HttpStatusCode.Unauthorized, cause.message ?: "")
            } else if (cause is NotAuthorizedException) {
                call.respond(HttpStatusCode.Unauthorized, cause.message ?: "")
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}