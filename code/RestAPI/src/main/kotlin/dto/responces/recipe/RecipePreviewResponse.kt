package api.dto.responces.recipe

import api.dto.responces.pfc.PFCResponse
import bl.entities.RecipePreview
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class RecipePreviewResponse(
    var id: Int,
    var name: String,
    var description: String,
    var time: Int,
    var servingsNum: Int,
    var date: LocalDateTime,
    var pfc: PFCResponse,
) {
    constructor(recipe: RecipePreview) : this(
        recipe.id,
        recipe.name,
        recipe.description,
        recipe.time,
        recipe.servingsNum,
        recipe.date.toKotlinLocalDateTime(),
        PFCResponse(recipe.pfc)
    )
}
