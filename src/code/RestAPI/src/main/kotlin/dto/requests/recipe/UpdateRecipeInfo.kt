package api.dto.requests.recipe

import bl.entities.Recipe
import bl.entities.RecipeState
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRecipeInfo(
    var name: String,
    var description: String,
    var time: Int,
    var servingsNum: Int,
    var pfc: PFC,
    var state: RecipeState
) {
    fun toBLEntity(id: Int): Recipe {
        return Recipe(
            id,
            name,
            description,
            time,
            servingsNum,
            pfc.toBLEntity(),
            state = state
        )
    }
}