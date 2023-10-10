package bl.entities

data class Stage(
    var id: Int,
    var time: Int,
    var description: String,
    val orderNum: Int = 0,
    var ingredients: List<IngredientInStage>
)