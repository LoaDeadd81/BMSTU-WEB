package api.plugins

import api.dto.requests.comment.StoreComment
import api.dto.requests.comment.UpdateComment
import api.dto.requests.ingredients.IngredientRequest
import api.dto.requests.recipe.StoreRecipe
import api.dto.requests.recipe.UpdateRecipe
import api.dto.requests.recipe.UpdateRecipeInfo
import api.dto.requests.recipe.UpdateRecipeStages
import api.dto.requests.user.UpdateUser
import api.dto.requests.user.UserCredentials
import api.dto.responces.ingredients.IngredientResponse
import api.dto.responces.recipe.CommentResponse
import api.dto.responces.recipe.RecipePreviewResponse
import api.dto.responces.recipe.RecipeResponse
import api.dto.responces.user.FullUserResponse
import api.dto.responces.user.UserResponse
import api.routes.getId
import bl.entities.IngredientType
import bl.entities.ProcessingType
import bl.entities.RecipeState
import bl.managers.*
import com.apurebase.kgraphql.Context
import com.apurebase.kgraphql.GraphQL
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import kotlinx.datetime.LocalDateTime
import java.util.*

fun Application.configureGQL() {
    val jwtAudience = System.getenv("JWT_AUD") ?: throw Exception("no JWT_AUD in env")
    val jwtDomain = System.getenv("JWT_ISSUER") ?: throw Exception("no JWT_ISSUER in env")
    val jwtSecret = System.getenv("JWT_SECRET") ?: throw Exception("no JWT_SECRET in env")

    install(GraphQL) {
        playground = true
        endpoint = "/api/v2"

//        {
//            "Authorization": "Bearer YOUR_TOKEN_HERE"
//        }
        wrap {
            authenticate("auth", optional = true, build = it)
        }

        context { call ->
            +call
        }

        schema {
            stringScalar<LocalDateTime> {
                deserialize = { date: String -> LocalDateTime.parse(date) }
                serialize = LocalDateTime::toString
            }

            stringScalar<ProcessingType> {
                deserialize = { type: String -> enumValueOf<ProcessingType>(type) }
                serialize = ProcessingType::toString
            }

            stringScalar<IngredientType> {
                deserialize = { type: String -> enumValueOf<IngredientType>(type) }
                serialize = IngredientType::toString
            }

            stringScalar<RecipeState> {
                deserialize = { type: String -> enumValueOf<RecipeState>(type) }
                serialize = RecipeState::toString
            }

            data class Token(val token: String)
            mutation("logIn") {
                resolver { data: UserCredentials ->
                    val user = AccountService.logIN(data.login, data.password)
                    val token = JWT.create()
                        .withAudience(jwtAudience)
                        .withIssuer(jwtDomain)
                        .withClaim("id", user.id)
                        .withExpiresAt(Date(System.currentTimeMillis() + 600000))

                    Token(token.sign(Algorithm.HMAC256(jwtSecret)))
                }
            }

            mutation("createUser") {
                resolver { data: UserCredentials ->
                    val id = UserManager.create(data.login, data.password)

                    UserResponse(UserManager.read(id))
                }
            }
            query("users") {
                resolver { -> UserManager.getAll().map { UserResponse(it) } }
            }
            query("user") {
                resolver { id: Int -> FullUserResponse(UserManager.read(id), UserManager.getPublishedRecipes(id)) }
            }
            mutation("updateUser") {
                resolver { id: Int, data: UpdateUser, ctx: Context ->
                    AccountService.setId(getId(ctx.get<ApplicationCall>()!!))

                    val user = data.toBLEntity(id)
                    UserManager.update(user)

                    UserResponse(UserManager.read(id))
                }
            }
            mutation("deleteUser") {
                resolver { id: Int, ctx: Context ->
                    AccountService.setId(getId(ctx.get<ApplicationCall>()!!))

                    val user = UserManager.read(id)

                    UserManager.delete(id)

                    UserResponse(user)
                }
            }
            mutation("updateCredentials") {
                resolver { id: Int, data: UserCredentials, ctx: Context ->
                    AccountService.setId(getId(ctx.get<ApplicationCall>()!!))

                    UserManager.updateCredentials(id, data.login, data.password)

                    UserResponse(UserManager.read(id))
                }
            }
            mutation("saveRecipe") {
                resolver { userId: Int, recipeId: Int, ctx: Context ->
                    AccountService.setId(getId(ctx.get<ApplicationCall>()!!))

                    UserManager.addToFavorite(userId, recipeId)

                    RecipeManager.getAll(userId, true, null).map { RecipePreviewResponse(it) }
                }
            }
            mutation("delSaveRecipe") {
                resolver { userId: Int, recipeId: Int, ctx: Context ->
                    AccountService.setId(getId(ctx.get<ApplicationCall>()!!))

                    UserManager.deleteFromFavorite(userId, recipeId)

                    RecipeManager.getAll(userId, true, null).map { RecipePreviewResponse(it) }
                }
            }


            mutation("createRecipe") {
                resolver { data: StoreRecipe, ctx: Context ->
                    AccountService.setId(getId(ctx.get<ApplicationCall>()!!))

                    val id = RecipeManager.create(data.toBLEntity())

                    RecipeResponse(RecipeManager.read(id))
                }
            }
            query("recipes") {
                resolver { userId: Int?, saved: Boolean?, state: String?, ctx: Context ->
                    val uId = ctx.get<ApplicationCall>()?.principal<JWTPrincipal>()?.payload?.getClaim("id")?.asInt()
                    if (uId != null) {
                        AccountService.setId(uId)
                    }

                    RecipeManager.getAll(userId, saved, state).map { RecipePreviewResponse(it) }
                }
            }
            query("recipe") {
                resolver { id: Int ->
                    RecipeResponse(RecipeManager.read(id))
                }
            }
            mutation("updateRecipe") {
                resolver { id: Int, data: UpdateRecipe, ctx: Context ->
                    AccountService.setId(getId(ctx.get<ApplicationCall>()!!))

                    RecipeManager.update(data.toBLEntity(id))

                    RecipeResponse(RecipeManager.read(id))
                }
            }
            mutation("deleteRecipe") {
                resolver { id: Int, ctx: Context ->
                    AccountService.setId(getId(ctx.get<ApplicationCall>()!!))

                    val recipe = RecipeResponse(RecipeManager.read(id))
                    RecipeManager.delete(id)

                    recipe
                }
            }
            mutation("updateRecipeInfo") {
                resolver { id: Int, data: UpdateRecipeInfo, ctx: Context ->
                    AccountService.setId(getId(ctx.get<ApplicationCall>()!!))

                    RecipeResponse(RecipeManager.updateInfo(data.toBLEntity(id)))
                }
            }
            mutation("updateRecipeStages") {
                resolver { id: Int, data: UpdateRecipeStages, ctx: Context ->
                    AccountService.setId(getId(ctx.get<ApplicationCall>()!!))

                    RecipeResponse(RecipeManager.updateStages(id, data.toBLEntity()))
                }
            }
            mutation("createComment") {
                resolver { id: Int, data: StoreComment, ctx: Context ->
                    AccountService.setId(getId(ctx.get<ApplicationCall>()!!))

                    val cId = CommentManager.create(data.text, id)

                    CommentResponse(CommentManager.read(cId))
                }
            }
            mutation("updateComment") {
                resolver { id: Int, data: UpdateComment, ctx: Context ->
                    AccountService.setId(getId(ctx.get<ApplicationCall>()!!))

                    CommentManager.update(data.toBLEntity(id))

                    CommentResponse(CommentManager.read(id))
                }
            }
            mutation("deleteComment") {
                resolver { id: Int, ctx: Context ->
                    AccountService.setId(getId(ctx.get<ApplicationCall>()!!))
                    val comment = CommentResponse(CommentManager.read(id))

                    CommentManager.delete(id)

                    comment
                }
            }
            mutation("updateCommentText") {
                resolver { id: Int, data: StoreComment, ctx: Context ->
                    AccountService.setId(getId(ctx.get<ApplicationCall>()!!))

                    CommentManager.updateText(id, data.text)

                    CommentResponse(CommentManager.read(id))
                }
            }


            query("ingredients") {
                resolver { name: String? ->
                    val ingredients = if (name != null) {
                        IngredientManager.findByName(name).map { IngredientResponse(it) }
                    } else {
                        IngredientManager.getAll().map { IngredientResponse(it) }
                    }

                    ingredients
                }
            }
            query("ingredient") {
                resolver { id: Int ->
                    IngredientResponse(IngredientManager.read(id))
                }
            }
            mutation("createIngredient") {
                resolver { data: IngredientRequest, ctx: Context ->
                    AccountService.setId(getId(ctx.get<ApplicationCall>()!!))

                    val id = IngredientManager.create(data.toBLEntity())

                    IngredientResponse(IngredientManager.read(id))
                }
            }
            mutation("updateIngredient") {
                resolver { id: Int, data: IngredientRequest, ctx: Context ->
                    AccountService.setId(getId(ctx.get<ApplicationCall>()!!))

                    IngredientManager.update(data.toBLEntity(id))

                    IngredientResponse(IngredientManager.read(id))
                }
            }
            mutation("deleteIngredient") {
                resolver { id: Int, ctx: Context ->
                    AccountService.setId(getId(ctx.get<ApplicationCall>()!!))

                    val ingredient = IngredientResponse(IngredientManager.read(id))

                    IngredientManager.delete(id)

                    ingredient
                }
            }
        }
    }
}