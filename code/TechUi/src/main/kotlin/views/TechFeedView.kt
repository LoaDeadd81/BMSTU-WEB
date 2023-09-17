package techUi.views

import bl.entities.RecipePreview

class TechFeedView {
    fun printFeed(recipes: List<RecipePreview>): ULong {
        println("Лента рецептов")
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
}