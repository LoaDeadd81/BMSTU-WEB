package da.dao

import bl.entities.IngredientInStage
import bl.entities.ProcessingType
import bl.entities.Stage
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption


object Stages : IntIdTable("stage") {
    val time = integer("time").check { it.greaterEq(0) }
    val description = text("description")
    val recipe = reference("recipeid", Recipes, onDelete = ReferenceOption.CASCADE)
    val order_num = integer("order_num").check { it.greater(0) }
}

class StageTable(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StageTable>(Stages)

    var time by Stages.time
    var description by Stages.description
    var recipeId by Stages.recipe
    var orderNum by Stages.order_num

    var recipe by RecipeTable referencedOn Stages.recipe

    val list by IngredientListTable referrersOn IngredientLists.stage
}

fun StageTable.toEntity(): Stage {
    val ingredients = this.findIngredients()
    return Stage(
        id = this.id.value,
        time = this.time,
    description = this.description,
        ingredients = ingredients,
        orderNum = this.orderNum
)
}


fun StageTable.findIngredients(): List<IngredientInStage> {
    val res = mutableListOf<IngredientInStage>()

    val lst = this.list
    for (it in lst) {
        val amount = it.amount
        val type = it.processingType

        res += it.ingredient.toEntityAddInfo(amount, ProcessingType.fromInt(type))
    }

    return res
}