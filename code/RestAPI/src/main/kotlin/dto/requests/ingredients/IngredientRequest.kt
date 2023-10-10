package api.dto.requests.ingredients

import bl.entities.Ingredient
import bl.entities.IngredientType
import kotlinx.serialization.Serializable

@Serializable
data class IngredientRequest(val name: String, val type: IngredientType, val nutritionalValue: Int) {
    fun toBLEntity(id: Int = 0): Ingredient {
        return Ingredient(id, name, type, nutritionalValue)
    }
}
