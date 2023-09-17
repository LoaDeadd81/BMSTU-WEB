package techUi.views

import bl.entities.Recipe
import bl.entities.RecipePreview

class TechPublishQueueView {
    private val techIngredientView = TechIngredientView()
    private val techStageView = TechStageView()
    fun print(recipes: List<RecipePreview>): ULong {
        println("Очередь заявок на публикацию")
        for (item in recipes) {
            println("id: " + item.id)
            println("Имя: " + item.name)
            println("Описание: " + item.description)
            println("Время: " + item.time)
            println("Количество порций: " + item.servingsNum)
            println()
        }
        print("Номер рецепта (0 для выхода): ")

        return readln().toULong()
    }

    fun printRecipe(recipe: Recipe): Int {
        println("id: " + recipe.id)
        println("Имя: " + recipe.name)
        println("Описание: " + recipe.description)
        println("Время: " + recipe.time)
        println("Количество порций: " + recipe.servingsNum)
        println("Белки: " + recipe.pfc.protein)
        println("Жиры: " + recipe.pfc.fat)
        println("Углеводы: " + recipe.pfc.carbon)
        println("Дата создания: " + recipe.published)
        println("Создатель: " + recipe.owner.login)
        println("Ингредиенты: ")
        for (item in recipe.ingredients) techIngredientView.printIngredientInRecipe(item, "\t")
        println("Стадии приготовления: ")
        for (item in recipe.stages) techStageView.print(item, "\t")
        println()

        println("1. Опубликовать")
        println("2. Удалить из очереди")
        println("0. Выход")
        print("Выбор: ")
        return readln().toInt()
    }
}