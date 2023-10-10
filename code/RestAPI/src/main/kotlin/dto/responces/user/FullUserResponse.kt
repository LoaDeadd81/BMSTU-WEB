package api.dto.responces.user

import api.dto.responces.recipe.RecipePreviewResponse
import bl.entities.RecipePreview
import bl.entities.User
import kotlinx.serialization.Serializable

@Serializable
data class FullUserResponse(
    var id: Int,
    var login: String,
    var isAdmin: Boolean,
    var recipes: List<RecipePreviewResponse>
) {
    constructor(user: User, recipes: List<RecipePreview>) : this(
        user.id,
        user.login,
        user.isAdmin,
        recipes.map { RecipePreviewResponse(it) })
}
