package bl.entities

data class Stage(val id: ULong, var time: ULong, var description: String, var ingredients: List<IngredientInStage>)