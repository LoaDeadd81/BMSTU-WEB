package da.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object IngredientLists : IntIdTable("ingredient_list") {
    val amount = integer("amount").check { it.greater(0) }
    val processingType = integer("processing_type")
    val ingredient = reference("ingredientid", Ingredients)
    val stage = reference("stageid", Stages, onDelete = ReferenceOption.CASCADE)
}

class IngredientListTable(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<IngredientListTable>(IngredientLists)

    var amount by IngredientLists.amount
    var processingType by IngredientLists.processingType
    var ingredientId by IngredientLists.ingredient
    var stageId by IngredientLists.stage

    var ingredient by IngredientTable referencedOn IngredientLists.ingredient
    var stage by StageTable referencedOn IngredientLists.stage
}
