package techUi.controllers

import bl.Facade
import bl.exceptions.AccessDeniedException
import bl.exceptions.NotExistingRecipeException
import controllers.interfaces.IFeedController
import org.slf4j.LoggerFactory
import techUi.views.TechFeedView

class TechFeedController(facade: Facade) : IFeedController(facade) {

    private val view = TechFeedView()
    private val techRecipeController = TechRecipeController(facade)
    private val logger = LoggerFactory.getLogger("mainLogger")
    override fun showFeed() {
        logger.debug("Showed feed")

        while (true) {
            try {
                val recipes = facade.getFeedSortedByDate()
                val recipeId = view.printFeed(recipes)
                if (recipeId == 0u.toULong()) break
                handleRecipeView(recipeId)
            } catch (e: NumberFormatException) {
                println("Введите цифру")
                logger.warn("Incorrect menu item entered", e)
            } catch (e: NotExistingRecipeException) {
                println("Нет такого рецепта")
                logger.warn("The user requested a non-existent recipe", e)
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

    override fun showOwnFeed(userId: ULong) {
        logger.debug("Showed own feed")

        try {
            val recipes = facade.getOwnFeed(userId)
            val recipeId = view.printFeed(recipes)
            handleRecipeView(recipeId)
        } catch (e: Exception) {
            println("Ошибка")
            println(e.message)
            logger.warn("Exception", e)
        }
        println()
    }

    override fun showSavedFeed(userId: ULong) {
        logger.debug("Showed saved feed")

        try {
            val recipes = facade.getSavedFeed(userId)
            val recipeId = view.printFeed(recipes)
            handleRecipeView(recipeId)
        } catch (e: Exception) {
            println("Ошибка")
            println(e.message)
            logger.warn("Exception", e)
        }
        println()
    }

    private fun handleRecipeView(recipeId: ULong) {
        when (recipeId) {
            0u.toULong() -> println()
            else -> techRecipeController.printRecipe(recipeId)
        }
        println()
    }
}