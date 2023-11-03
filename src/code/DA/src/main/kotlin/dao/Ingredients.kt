package da.dao

import bl.entities.Ingredient
import bl.entities.IngredientInStage
import bl.entities.IngredientType
import bl.entities.ProcessingType
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Ingredients : IntIdTable("ingredient") {
    val name = text("name")
    val nutritionalValue = integer("nutritional_value")
    val type = integer("type")
}

class IngredientTable(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<IngredientTable>(Ingredients)

    var name by Ingredients.name
    var nutritionalValue by Ingredients.nutritionalValue
    var type by Ingredients.type

    val list by IngredientListTable referrersOn IngredientLists.ingredient
}

fun IngredientTable.toEntity(): Ingredient {
    return Ingredient(
        id = this.id.value,
        name = this.name,
        type = IngredientType.fromInt(this.type),
        nutritionalValue = this.nutritionalValue,
    )
}

fun IngredientTable.toEntityAddInfo(amount: Int, processingType: ProcessingType): IngredientInStage {
    return IngredientInStage(
        id = this.id.value,
        name = this.name,
        type = IngredientType.fromInt(this.type),
        amount = amount,
        nutritionalValue = this.nutritionalValue,
        processingType = processingType
    )
}