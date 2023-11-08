package api.routes

import api.dto.requests.user.UserCredentials
import bl.exceptions.NotAuthorizedException
import bl.managers.AccountService
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import java.util.*


@Resource("/auth")
class Auth {
    @Resource("login")
    class Login(val parent: Auth = Auth())

    @Resource("register")
    class Register(val parent: Auth = Auth())
}

fun Route.auth() {
    val jwtAudience = System.getenv("JWT_AUD") ?: throw Exception("no JWT_AUD in env")
    val jwtDomain = System.getenv("JWT_ISSUER") ?: throw Exception("no JWT_ISSUER in env")
    val jwtSecret = System.getenv("JWT_SECRET") ?: throw Exception("no JWT_SECRET in env")

    post<Auth.Login> {
        val data = call.receive<UserCredentials>()

        val user = AccountService.logIN(data.login, data.password)
        val token = JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtDomain)
            .withClaim("id", user.id)
            .withExpiresAt(Date(System.currentTimeMillis() + 600000))


        call.respond(hashMapOf("token" to token.sign(Algorithm.HMAC256(jwtSecret))))
    }
    post<Auth.Register> {
        call.receive<UserCredentials>()

        call.respond(HttpStatusCode.Created)
    }
}

fun getId(call: ApplicationCall): Int {
    return call.principal<JWTPrincipal>()?.payload?.getClaim("id")?.asInt()
        ?: throw NotAuthorizedException("Problem with extraction data from token")
}