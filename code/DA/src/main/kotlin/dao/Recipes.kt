package da.dao

import bl.entities.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime


object Recipes : IntIdTable("recipe") {
    val name = text("name")
    val description = text("description")
    val time = integer("time").check { it.greaterEq(0) }
    val servingsNum = integer("servings_num")
    val protein = integer("protein").check { it.greaterEq(0) }
    val fat = integer("fat").check { it.greaterEq(0) }
    val carbon = integer("carbon").check { it.greaterEq(0) }
    val date = datetime("date")
    val state = integer("state")
    val owner = reference("ownerid", Users, onDelete = ReferenceOption.CASCADE)
}

class RecipeTable(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RecipeTable>(Recipes)

    var name by Recipes.name
    var description by Recipes.description
    var time by Recipes.time
    var servingsNum by Recipes.servingsNum
    var protein by Recipes.protein
    var fat by Recipes.fat
    var carbon by Recipes.carbon
    var date by Recipes.date
    var state by Recipes.state
    var ownerId by Recipes.owner

    var owner by UserTable referencedOn Recipes.owner

    val comments by CommentTable referrersOn Comments.recipe
    val stages by StageTable referrersOn Stages.recipe
    val savedRecipes by SavedRecipeTable referrersOn SavedRecipes.recipe

}

class RecipePreviewTable(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RecipePreviewTable>(Recipes)

    var name by Recipes.name
    var description by Recipes.description
    var time by Recipes.time
    var servingsNum by Recipes.servingsNum
    var date by Recipes.date
    var protein by Recipes.protein
    var fat by Recipes.fat
    var carbon by Recipes.carbon
}

fun RecipeTable.toEntity(): Recipe {
    val stages = this.stages.map { it.toEntity() }

    val recipeIngredients = mutableListOf<IngredientInStage>()
    for (stage in stages) {
        for (ingredient in stage.ingredients) {
            val tmp = recipeIngredients.find { it.id == ingredient.id }
            if (tmp == null) {
                recipeIngredients += ingredient.copy()
            } else tmp.amount += ingredient.amount
        }
    }

    return Recipe(
        id = this.id.value,
        name = this.name,
        description = this.description,
        time = this.time,
        servingsNum = this.servingsNum,
        pfc = PFC(this.protein, this.fat, this.carbon),
        date = this.date,
        state = RecipeState.fromInt(this.state),
//        state = this.state,
        stages = stages,
        owner = this.owner.toEntity(),
        comments = this.comments.map { it.toEntity() },
        ingredients = recipeIngredients
    )

}

fun RecipePreviewTable.toEntity(): RecipePreview = RecipePreview(
    id = this.id.value,
    name = this.name,
    description = this.description,
    time = this.time,
    servingsNum = this.servingsNum,
    date = this.date,
    pfc = PFC(this.protein, this.fat, this.carbon)
)