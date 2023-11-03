package api.dto.responces.ingredients

import bl.entities.Ingredient
import bl.entities.IngredientType
import kotlinx.serialization.Serializable

@Serializable
data class IngredientResponse(var id: Int, var name: String, var type: IngredientType, var nutritionalValue: Int) {
    constructor(ingredient: Ingredient) : this(
        ingredient.id,
        ingredient.name,
        ingredient.type,
        ingredient.nutritionalValue
    )
}
