package da.data.mother

import bl.entities.Stage

object StageDataMother {
    fun regularStage(): Stage {
        return Stage(1, 1, "desc1", 1, listOf(IngredientInStageDataMother.regularIngredient()))
    }

    fun updatedStage(): Stage {
        return Stage(6, 1, "desc1", 1, listOf(IngredientInStageDataMother.regularIngredient()))
    }
}