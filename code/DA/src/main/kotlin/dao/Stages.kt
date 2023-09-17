package da.dao

import bl.entities.IngredientInStage
import bl.entities.Stage
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


object Stages : IntIdTable("stage") {
    val time = integer("time").check { it.greaterEq(0) }
    val description = text("description")
    val recipe = reference("recipeid", Recipes)
}

class StageTable(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StageTable>(Stages)

    var time by Stages.time
    var description by Stages.description
    var recipeId by Stages.recipe

    var recipe by RecipeTable referencedOn Stages.recipe

    val list by IngredientListTable referrersOn IngredientLists.stage

    var ingredients by IngredientTable via IngredientLists
}

fun StageTable.toEntity(): Stage = Stage(
    id = this.id.value.toULong(),
    time = this.time.toULong(),
    description = this.description,
    ingredients = listOf()
)

fun StageTable.findIngredients(): List<IngredientInStage> {
    val res = mutableListOf<IngredientInStage>()

    val lst = this.list
    for (it in lst) {
        val amount = it.amount
        val type = it.processingType

        res += it.ingredient.toEntityAddInfo(amount.toUInt(), type)
    }

    return res
}