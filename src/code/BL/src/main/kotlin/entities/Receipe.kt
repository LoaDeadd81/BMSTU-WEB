package bl.entities

import java.time.LocalDateTime

enum class RecipeState(val value: Int) {
    CLOSE(1), READY_TO_PUBLISH(2), PUBLISHED(3);

    fun toString(x: RecipeState): String = when (x) {
        CLOSE -> "Скрыт"
        READY_TO_PUBLISH -> "Ожидает публикации"
        PUBLISHED -> "Опубликован"
    }

    companion object {
        fun fromInt(value: Int) = entries.first { it.value == value }
    }
}

data class Recipe(
    var id: Int,
    var name: String,
    var description: String,
    var time: Int,
    var servingsNum: Int,
    var pfc: PFC,
    var date: LocalDateTime = LocalDateTime.now(),
    var state: RecipeState = RecipeState.CLOSE,
    var stages: List<Stage> = listOf(),
    var comments: List<Comment> = listOf(),
    var owner: User = User(0, "", "", false),
    var ingredients: List<IngredientInStage> = listOf()
)


data class RecipePreview(
    var id: Int,
    var name: String,
    var description: String,
    var time: Int,
    var servingsNum: Int,
    var date: LocalDateTime,
    var pfc: PFC,
)