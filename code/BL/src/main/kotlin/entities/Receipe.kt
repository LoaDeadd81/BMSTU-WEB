package bl.entities

import java.time.LocalDateTime

data class Recipe(
    val id: ULong,
    var name: String,
    var description: String,
    var time: ULong,
    var servingsNum: UInt,
    var pfc: PFC,
    var date: LocalDateTime,
    var published: Boolean,
    var stages: List<Stage>,
    var comments: List<Comment>,
    var owner: User,
    var ingredients: List<IngredientInStage>
)


data class RecipePreview(
    val id: ULong, var name: String, var description: String, var time: ULong, var servingsNum: UInt
)

fun Recipe.toRecipePreview(): RecipePreview =
    RecipePreview(this.id, this.name, this.description, this.time, this.servingsNum)