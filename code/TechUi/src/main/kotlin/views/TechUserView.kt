package techUi.views

import bl.entities.RecipePreview
import bl.entities.User

class TechUserView {
    fun print(user: User, recipes: List<RecipePreview>): Int {
        println("Профиль пользователя")
        println("id: " + user.id)
        println("Имя: " + user.login)
        println("Сохранённые рецепты")
        for (item in recipes) {
            println("\t" + "id: " + item.id)
            println("\t" + "Имя: " + item.name)
            println("\t" + "Описание: " + item.description)
            println("\t" + "Время: " + item.time)
            println("\t" + "Количество порций: " + item.servingsNum)
            println()
        }

        println("0. Выход")
        print("Выбор: ")

        return readln().toInt()
    }
}