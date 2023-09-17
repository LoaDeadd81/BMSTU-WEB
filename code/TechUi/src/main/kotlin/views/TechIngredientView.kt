package techUi.views

import bl.entities.IngredientInStage

class TechIngredientView {
    fun printIngredientInRecipe(ingredient: IngredientInStage, tab: String = "") {
        printBaseInfo(ingredient, tab)
    }

    fun printIngredientInStage(ingredient: IngredientInStage, tab: String = "") {
        printBaseInfo(ingredient, tab)
        println(tab + "Тип обработки: " + ingredient.processingType)
    }

    private fun printBaseInfo(ingredient: IngredientInStage, tab: String = "") {
        println(tab + "id: " + ingredient.id)
        println(tab + "Имя: " + ingredient.name)
        println(tab + "Тип: " + ingredient.type)
        println(tab + "Колличество: " + ingredient.amount)
        println(tab + "Пищевая ценность: " + ingredient.nutritionalValue)
    }
}