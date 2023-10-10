package api.dto.requests.recipe

import bl.entities.IngredientInStage
import bl.entities.ProcessingType
import bl.entities.Recipe
import bl.entities.Stage
import kotlinx.serialization.Serializable

@Serializable
class StoreRecipe(
    var name: String,
    var description: String,
    var time: Int,
    var servingsNum: Int,
    var pfc: PFC,
    var stages: List<StoreStage>,
) {
    fun toBLEntity(): Recipe {
        return Recipe(
            0,
            name,
            description,
            time,
            servingsNum,
            pfc.toBLEntity(),
            stages = stages.map { it.toBLEntity() })
    }
}

@Serializable
data class StoreStage(var time: Int, var description: String, var ingredients: List<StoreIngredientInStage>) {
    fun toBLEntity(): Stage {
        return Stage(0, time, description, ingredients = ingredients.map { it.toBLEntity() })
    }
}

@Serializable
data class StoreIngredientInStage(
    var id: Int,
    var amount: Int,
    var processingType: ProcessingType
) {
    fun toBLEntity(): IngredientInStage {
        return IngredientInStage(id, amount = amount, processingType = processingType)
    }
}