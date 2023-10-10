package api.dto.requests.recipe

import bl.entities.Recipe
import bl.entities.RecipeState
import bl.entities.User
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class UpdateRecipe(
    var name: String,
    var description: String,
    var time: Int,
    var servingsNum: Int,
    var pfc: PFC,
    var stages: List<StoreStage>,
    var date: LocalDateTime,
    var owner_id: Int,
    var state: RecipeState
) {
    fun toBLEntity(id: Int): Recipe {
        return Recipe(
            0,
            name,
            description,
            time,
            servingsNum,
            pfc.toBLEntity(),
            stages = stages.map { it.toBLEntity() },
            date = date.toJavaLocalDateTime(),
            owner = User(owner_id),
            state = state
        )
    }
}
