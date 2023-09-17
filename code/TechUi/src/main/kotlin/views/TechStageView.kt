package techUi.views

import bl.entities.Stage

class TechStageView {
    private val techIngredientView = TechIngredientView()
    fun print(stage: Stage, tab: String = "") {
        println(tab + "id: " + stage.id)
        println(tab + "Описание: " + stage.description)
        println(tab + "Время: " + stage.time)
        println("Ингредиенты: ")
        for (item in stage.ingredients)
            techIngredientView.printIngredientInStage(item, tab + "\t")
    }
}