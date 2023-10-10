package api.routes

import api.dto.requests.user.UserCredentials
import bl.exceptions.NotAuthorizedException
import bl.managers.AccountService
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import java.util.*

@Resource("/auth")
class Auth {
    @Resource("login")
    class Login(val parent: Auth = Auth())

    @Resource("register")
    class Register(val parent: Auth = Auth())

    @Resource("logout")
    class Logout(val parent: Auth = Auth())
}

fun Route.auth() {
    val dotenv = dotenv()
    val jwtAudience = dotenv["JWT_AUD"]
    val jwtDomain = dotenv["JWT_ISSUER"]
    val jwtSecret = dotenv["JWT_SECRET"]

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
        val data = call.receive<UserCredentials>()

        call.respond(HttpStatusCode.Created)
    }
}

fun getId(call: ApplicationCall): Int {
    return call.principal<JWTPrincipal>()?.payload?.getClaim("id")?.asInt()
        ?: throw NotAuthorizedException("Problem with extraction data from token")
}

val host = "http://0.0.0.0:8089"