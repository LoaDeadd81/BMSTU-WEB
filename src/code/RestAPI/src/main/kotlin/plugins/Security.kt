package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity() {
    // Please read the jwt property from the config file if you are using EngineMain
    val jwtAudience = System.getenv("JWT_AUD") ?: throw Exception("no JWT_AUD in env")
    val jwtDomain = System.getenv("JWT_ISSUER") ?: throw Exception("no JWT_ISSUER in env")
    val jwtRealm = System.getenv("JWT_REALM") ?: throw Exception("no JWT_REALM in env")
    val jwtSecret = System.getenv("JWT_SECRET") ?: throw Exception("no JWT_SECRET in env")
    authentication {
        jwt("auth") {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience) &&
                    credential.payload.getClaim("id").asInt() > 0
                ) JWTPrincipal(credential.payload)
                else null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}
