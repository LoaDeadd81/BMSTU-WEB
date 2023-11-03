package api.routes

import api.RestApi
import api.dto.requests.comment.StoreComment
import api.dto.requests.comment.UpdateComment
import api.dto.requests.recipe.StoreRecipe
import api.dto.requests.recipe.UpdateRecipe
import api.dto.requests.recipe.UpdateRecipeInfo
import api.dto.requests.recipe.UpdateRecipeStages
import api.dto.responces.recipe.CommentResponse
import api.dto.responces.recipe.RecipePreviewResponse
import api.dto.responces.recipe.RecipeResponse
import bl.managers.AccountService
import bl.managers.CommentManager
import bl.managers.RecipeManager
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.patch
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*

@Resource("/recipes")
class Recipes(val user_id: Int? = null, val saved: Boolean? = null, val state: String? = null) {
    @Resource("{id}")
    class Id(val parent: Recipes, val id: Int) {
        @Resource("info")
        class Info(val parent: Id)

        @Resource("stages")
        class Stages(val parent: Id)

        @Resource("comments")
        class Comments(val parent: Id) {
            @Resource("{comment_id}")
            class CommentId(val parent: Comments, val comment_id: Int)
        }
    }
}

fun Route.recipes() {
    authenticate("auth") {
        post<Recipes> {
            AccountService.setId(getId(call))
            val data = call.receive<StoreRecipe>()

            val id = RecipeManager.create(data.toBLEntity())

            val host = "http://${RestApi.host}:${RestApi.port}/api/v1"
            call.respondText("$host/recipes/$id", status = HttpStatusCode.Created)
        }
    }
    authenticate("auth", optional = true) {
        get<Recipes> { req ->
            val userId = req.user_id
            val saved = req.saved
            val state = req.state

            val uId = call.principal<JWTPrincipal>()?.payload?.getClaim("id")?.asInt()
            if (uId != null)
                AccountService.setId(uId)

            val recipes = RecipeManager.getAll(userId, saved, state).map { RecipePreviewResponse(it) }

            call.respond(recipes)
        }
    }
    get<Recipes.Id> { req ->
        val id = req.id

        val recipe = RecipeResponse(RecipeManager.read(id))

        call.respond(recipe)
    }
    authenticate("auth") {
        put<Recipes.Id> { req ->
            AccountService.setId(getId(call))
            val data = call.receive<UpdateRecipe>()

            RecipeManager.update(data.toBLEntity(req.id))

            call.respond(HttpStatusCode.NoContent)
        }
        delete<Recipes.Id> { req ->
            AccountService.setId(getId(call))
            val id = req.id

            RecipeManager.delete(id)

            call.respond(HttpStatusCode.NoContent)
        }
        patch<Recipes.Id.Info> { req ->
            AccountService.setId(getId(call))
            val data = call.receive<UpdateRecipeInfo>()

            val recipeBL = RecipeManager.updateInfo(data.toBLEntity(req.parent.id))

            call.respond(RecipeResponse(recipeBL))
        }
        patch<Recipes.Id.Stages> { req ->
            AccountService.setId(getId(call))
            val id = req.parent.id
            val data = call.receive<UpdateRecipeStages>()
            val stages = data.toBLEntity()

            val recipe = RecipeManager.updateStages(id, stages)

            call.respond(RecipeResponse(recipe))
        }
        post<Recipes.Id.Comments> { req ->
            val uid = getId(call)
            AccountService.setId(uid)
            val id = req.parent.id
            val data = call.receive<StoreComment>()

            val cId = CommentManager.create(uid, data.text, id)

            val host = "http://${RestApi.host}:${RestApi.port}/api/v1"
            call.respondText("$host/comments/$cId", status = HttpStatusCode.Created)
        }
        put<Recipes.Id.Comments.CommentId> { req ->
            AccountService.setId(getId(call))
            val data = call.receive<UpdateComment>()

            CommentManager.update(data.toBLEntity(req.comment_id))

            call.respond(HttpStatusCode.NoContent)
        }
        delete<Recipes.Id.Comments.CommentId> { req ->
            AccountService.setId(getId(call))
            val id = req.comment_id

            CommentManager.delete(id)

            call.respond(HttpStatusCode.NoContent)
        }
        patch<Recipes.Id.Comments.CommentId> { req ->
            AccountService.setId(getId(call))
            val id = req.comment_id
            val data = call.receive<StoreComment>()

            val comment = CommentManager.updateText(id, data.text)

            call.respond(CommentResponse(comment))
        }
    }
}