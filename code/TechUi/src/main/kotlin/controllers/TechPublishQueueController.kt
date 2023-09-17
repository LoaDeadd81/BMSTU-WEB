package techUi.controllers

import bl.Facade
import bl.exceptions.AccessDeniedException
import controllers.interfaces.IPublishQueueController
import org.slf4j.LoggerFactory
import techUi.views.TechPublishQueueView

class TechPublishQueueController(facade: Facade) : IPublishQueueController(facade) {
    private val view = TechPublishQueueView()
    private val logger = LoggerFactory.getLogger("mainLogger")

    override fun showQueue() {
        while (true) {
            try {
                val recipes = facade.getPublishQueue()

                val recipeId = view.print(recipes)
                if (recipeId == 0u.toULong()) break
                handleRecipeView(recipeId)
            } catch (e: AccessDeniedException) {
                println("Нет такого рецепта")
                logger.warn("The user requested the recipe without having access", e)
            } catch (e: Exception) {
                println("Ошибка")
                println(e.message)
                logger.warn("Exception", e)
            }
            println()
        }
    }

    private fun handleRecipeView(recipeId: ULong) {
        val recipe = facade.getRecipe(recipeId)
        val req = view.printRecipe(recipe)
        when (req) {
            1 -> facade.acceptRecipePublication(recipeId)
            2 -> facade.cancelRecipePublication(recipeId)
            else -> println("Нет такого пункта меню")
        }
        println()
    }
}