package api.dto.responces.recipe

import api.dto.responces.pfc.PFCResponse
import api.dto.responces.user.UserResponse
import bl.entities.*
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime

@Serializable
data class RecipeResponse(
    var id: Int,
    var name: String,
    var description: String,
    var time: Int,
    var servingsNum: Int,
    var pfc: PFCResponse,
    var date: LocalDateTime,
    var state: RecipeState,
    var stages: List<StageResponse>,
    var comments: List<CommentResponse>,
    var owner: UserResponse,
    var ingredients: List<IngredientInStageResponse>
) {
    constructor(recipe: Recipe) : this(
        recipe.id,
        recipe.name,
        recipe.description,
        recipe.time,
        recipe.servingsNum,
        PFCResponse(recipe.pfc),
        recipe.date.toKotlinLocalDateTime(),
        recipe.state,
        recipe.stages.map { StageResponse(it) },
        recipe.comments.map { CommentResponse(it) },
        UserResponse(recipe.owner),
        recipe.ingredients.map { IngredientInStageResponse(it) }
    )
}

@Serializable
data class StageResponse(
    var id: Int,
    var time: Int,
    var description: String,
    var ingredients: List<IngredientInStageResponse>
) {
    constructor(stage: Stage) : this(
        stage.id,
        stage.time,
        stage.description,
        stage.ingredients.map { IngredientInStageResponse(it) })
}

@Serializable
data class IngredientInStageResponse(
    var id: Int,
    var name: String,
    var type: IngredientType,
    var amount: Int,
    var nutritionalValue: Int,
    var processingType: ProcessingType
) {
    constructor(ingredient: IngredientInStage) : this(
        ingredient.id,
        ingredient.name,
        ingredient.type,
        ingredient.amount,
        ingredient.nutritionalValue,
        ingredient.processingType
    )
}

@Serializable
data class CommentResponse(var id: Int, var date: LocalDateTime, var text: String, var autor: UserResponse) {
    constructor(comment: Comment) : this(
        comment.id,
        comment.date.toKotlinLocalDateTime(),
        comment.text,
        UserResponse(comment.autor)
    )
}
