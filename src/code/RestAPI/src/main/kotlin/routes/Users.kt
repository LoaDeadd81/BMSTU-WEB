package api.routes

import api.RestApi
import api.dto.responces.user.UserResponse
import api.dto.responces.user.FullUserResponse
import api.dto.requests.user.UpdateUser
import api.dto.requests.user.UserCredentials
import bl.managers.AccountService
import bl.managers.UserManager
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.resources.patch
import io.ktor.server.response.*
import io.ktor.server.routing.*

@Resource("/users")
class Users {
    @Resource("{id}")
    class Id(val parent: Users = Users(), val id: Int, val full: Boolean? = false) {
        @Resource("saved-recipes")
        class SavedRecipes(val parent: Id) {
            @Resource("{recipe_id}")
            class RecipeId(val parent: SavedRecipes, val recipe_id: Int)
        }
    }
}

fun Route.users() {
    post<Users> {
        val data = call.receive<UserCredentials>()

        val id = UserManager.create(data.login, data.password)

        val host = "http://${RestApi.host}:${RestApi.port}/api/v1"
        call.respondText("$host/users/$id", status = HttpStatusCode.Created)
    }
    get<Users> {
        val users = UserManager.getAll().map { UserResponse(it) }

        call.respond(users)
    }
    get<Users.Id> { req ->
        val id = req.id
        val fullFlag = req.full

        if (fullFlag == true) {
            val user = UserManager.read(id)
            val recipes = UserManager.getPublishedRecipes(id)

            val resource = FullUserResponse(user, recipes)
            call.respond(resource)
        } else {
            val user = UserResponse(UserManager.read(id))

            call.respond(user)
        }
    }
    authenticate("auth") {
        put<Users.Id> { req ->
            AccountService.setId(getId(call))
            val data = call.receive<UpdateUser>()
            val id = req.id

            val user = data.toBLEntity(id)
            UserManager.update(user)

            call.respond(HttpStatusCode.NoContent)
        }
        delete<Users.Id> { req ->
            AccountService.setId(getId(call))
            val id = req.id

            UserManager.delete(id)

            call.respond(HttpStatusCode.NoContent)
        }
        patch<Users.Id> { req ->
            AccountService.setId(getId(call))
            val data = call.receive<UserCredentials>()
            val id = req.id

            val user = UserManager.updateCredentials(id, data.login, data.password)

            call.respond(UserResponse(user))
        }
        post<Users.Id.SavedRecipes.RecipeId> { req ->
            AccountService.setId(getId(call))
            val userId = req.parent.parent.id
            val recipeId = req.recipe_id

            UserManager.addToFavorite(userId, recipeId)

            call.respond(HttpStatusCode.NoContent)
        }
        delete<Users.Id.SavedRecipes.RecipeId> { req ->
            AccountService.setId(getId(call))
            val userId = req.parent.parent.id
            val recipeId = req.recipe_id

            UserManager.deleteFromFavorite(userId, recipeId)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}