package techUi.views

import org.slf4j.LoggerFactory

class TechMenuView {
    private val logger = LoggerFactory.getLogger("mainLogger")

    fun printUnAuthMenu(): Int {
        logger.debug("Showed unauth tech menu")

        println("1. Зарегестрироваться")
        println("2. Войти")
        println("3. Просмотреть ленту")
        printBottom()
        return readln().toInt()
    }

    fun printAuthMenu(): Int {
        logger.debug("Showed auth tech menu")

        printBaseActions()
        printBottom()
        return readln().toInt()
    }

    fun printAdminMenu(): Int {
        logger.debug("Showed auth tech menu")

        printBaseActions()
        println("5. Просмотреть очередь рецептов на публикацию")
        printBottom()
        return readln().toInt()
    }

    private fun printBaseActions() {
        println("Главное меню")
        println("1. Просмотреть ленту")
        println("2. Просмотреть профиль")
        println("3. Просмотреть собственные рецепты рецепты")
        println("4. Добавить рецепт")
    }

    private fun printBottom() {
        println("0. Выход")
        print("Выбор: ")
    }
}