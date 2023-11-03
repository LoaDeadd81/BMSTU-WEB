package api.plugins

import bl.exceptions.*
import exceptions.IllegalArguments
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusCodes() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            val message = cause.message ?: ""
            when (cause) {
                is RequestValidationException -> {
                    call.respond(HttpStatusCode.BadRequest, message)
                }

                is ValidationException -> {
                    call.respond(HttpStatusCode.BadRequest, message)
                }

                is NotFoundException -> {
                    call.respond(HttpStatusCode.NotFound, message)
                }

                is AccessDeniedException -> {
                    call.respond(HttpStatusCode.Forbidden, message)
                }

                is AlreadyExistException -> {
                    call.respond(HttpStatusCode.Conflict, message)
                }

                is LogInFailedException -> {
                    call.respond(HttpStatusCode.Unauthorized, message)
                }

                is NotAuthorizedException -> {
                    call.respond(HttpStatusCode.Unauthorized, message)
                }

                is IllegalArguments -> {
                    call.respond(HttpStatusCode.BadRequest, message)
                }

                else -> {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}