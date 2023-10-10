package api.dto.requests.recipe

import bl.entities.Recipe
import bl.entities.Stage
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRecipeStages(var stages: List<StoreStage>) {
    fun toBLEntity(): List<Stage> {
        return stages.map { it.toBLEntity() }
    }
}