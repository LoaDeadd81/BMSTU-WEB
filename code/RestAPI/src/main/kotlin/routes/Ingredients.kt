package api.routes

import api.dto.requests.ingredients.IngredientRequest
import api.dto.responces.ingredients.IngredientResponse
import bl.managers.AccountService
import bl.managers.IngredientManager
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.routing.*

@Resource("/ingredients")
class Ingredients(val name: String? = null) {
    @Resource("{id}")
    class Id(val parent: Ingredients, val id: Int)
}

fun Route.ingredients() {
    get<Ingredients> { req ->
        val name = req.name
        val ingredients = if (name != null) {
            IngredientManager.findByName(name).map { IngredientResponse(it) }
        } else {
            IngredientManager.getAll().map { IngredientResponse(it) }
        }

        call.respond(ingredients)
    }
    get<Ingredients.Id> { req ->
        val id = req.id

        val ingredient = IngredientResponse(IngredientManager.read(id))

        call.respond(ingredient)
    }
    authenticate("auth") {
        post<Ingredients> {
            AccountService.setId(getId(call))
            val ingredient = call.receive<IngredientRequest>()

            val id = IngredientManager.create(ingredient.toBLEntity())

            call.respondText("$host/ingredients/$id", status = HttpStatusCode.Created)
        }
        put<Ingredients.Id> { req ->
            AccountService.setId(getId(call))
            val ingredient = call.receive<IngredientRequest>()

            IngredientManager.update(ingredient.toBLEntity(req.id))

            call.respond(HttpStatusCode.NoContent)
        }
        delete<Ingredients.Id> { req ->
            AccountService.setId(getId(call))
            val id = req.id

            IngredientManager.delete(id)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}