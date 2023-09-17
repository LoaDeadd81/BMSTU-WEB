package da.dao

import bl.entities.ProcessingType
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object IngredientLists : IntIdTable("ingredient_list") {
    val amount = integer("amount").check { it.greater(0) }
    val processingType = customEnumeration(
        "processing_type",
        "proc_type",
        { value -> ProcessingType.valueOf(value as String) },
        { PGEnum("proc_type", it) })
    val ingredient = reference("ingredientid", Ingredients)
    val stage = reference("stageid", Stages)
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

//data class IngredientInfo(
//    val id: ULong,
//    var type: ProcessingType,
//    var amount: UInt,
//    var ingredient: Ingredient
//)

//fun IngredientListTable.toEntity(): IngredientInfo = IngredientInfo(
//    id = this.id.value.toULong(),
//    type = this.processingType,
//    amount = this.amount.toUInt(),
//    ingredient = this.ingredient.toEntity()
//)
