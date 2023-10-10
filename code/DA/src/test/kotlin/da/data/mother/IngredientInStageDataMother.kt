package da.data.mother

import bl.entities.IngredientInStage
import bl.entities.IngredientType
import bl.entities.ProcessingType

object IngredientInStageDataMother {
    fun regularIngredient(): IngredientInStage {
        return IngredientInStage(1, "name1", IngredientType.MEAT, 1, 1, ProcessingType.WASH)
    }
}