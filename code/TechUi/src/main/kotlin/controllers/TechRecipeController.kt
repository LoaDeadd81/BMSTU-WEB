package techUi.controllers

import bl.Facade
import bl.entities.Recipe
import bl.exceptions.AccessDeniedException
import bl.managers.AccountService
import controllers.interfaces.IRecipeController
import org.slf4j.LoggerFactory
import techUi.views.TechRecipeView

class TechRecipeController(facade: Facade) : IRecipeController(facade) {

    private val view = TechRecipeView()

    private val techUserController = TechUserController(facade)
    private val techCommentController = TechCommentController(facade)
    private val logger = LoggerFactory.getLogger("mainLogger")

    override fun printRecipe(recipeId: ULong) {
        logger.debug("Showed recipe with id {}", recipeId)

        while (true) {
            val recipe = facade.getRecipe(recipeId)
            val uId = AccountService.getCurrentUserId()
            if (uId == null) {
                try {
                    val req = view.printNotAuth(recipe)
                    if (req == 0) break
                    handleNotAuthReq(req, recipe)
                } catch (e: NumberFormatException) {
                    println("Введите цифру")
                    logger.warn("Incorrect menu item entered", e)
                } catch (e: AccessDeniedException) {
                    println("Нет такого рецепта")
                    logger.warn(
                        "The unauth user requested the recipe with id {} without having access",
                        recipeId,
                        e
                    )
                } catch (e: Exception) {
                    println("Ошибка")
                    println(e.message)
                    logger.warn("Exception", e)
                }
                println()
            } else {
                try {
                    val isInFavorite = facade.isInFavorite(uId, recipeId)
                    val isAdmin = AccountService.isCurrentUserAdmin()
                    if (uId == recipe.owner.id || isAdmin == true) {
                        val req = view.printOwnerView(recipe, isInFavorite)
                        if (req == 0) break
                        handleOwnerReq(req, recipe, isInFavorite)
                        println()
                        if (req == 6) return
                    } else {
                        val req = view.printAuth(recipe, isInFavorite)
                        if (req == 0) break
                        handleAuthReq(req, recipe, isInFavorite)
                        println()
                    }
                } catch (e: NumberFormatException) {
                    println("Введите цифру")
                    logger.warn("Incorrect menu item entered", e)
                } catch (e: AccessDeniedException) {
                    println("Нет такого рецепта")
                    logger.warn(
                        "The user with id {} requested the recipe with id {} without having access",
                        uId,
                        recipeId,
                        e
                    )
                } catch (e: Exception) {
                    println("Ошибка")
                    println(e.message)
                    logger.warn("Exception", e)
                }
            }
        }
    }

    private fun handleNotAuthReq(req: Int, recipe: Recipe) {
        when (req) {
            1 -> techUserController.printUser(recipe.owner)
            else -> println("Нет такого пункта меню")
        }
    }

    private fun handleAuthReq(req: Int, recipe: Recipe, isInFavorite: Boolean) {
        when (req) {
            1 -> techUserController.printUser(recipe.owner)
            2 -> {
                val uId = AccountService.getCurrentUserId()
                if (uId != null) {
                    if (isInFavorite) facade.deleteFromFavorite(uId, recipe.id)
                    else facade.addToFavorite(uId, recipe.id)
                }
            }

            3 -> techCommentController.addComment(recipe.id)
            else -> println("Нет такого пункта меню")
        }
    }

    private fun handleOwnerReq(req: Int, recipe: Recipe, isInFavorite: Boolean) {
        when (req) {
            1 -> techUserController.printUser(recipe.owner)
            2 -> {
                val uId = AccountService.getCurrentUserId()
                if (uId != null) {
                    if (isInFavorite) facade.deleteFromFavorite(uId, recipe.id)
                    else facade.addToFavorite(uId, recipe.id)
                }
            }

            3 -> techCommentController.addComment(recipe.id)
            4 -> {
                val rec = inputRecipe(recipe.id)
                if (rec != null) facade.updateRecipe(rec)
            }

            5 -> facade.deleteRecipe(recipe.id)
            6 -> {
                if (recipe.published) println("Нет такого пункта меню")
                else facade.addToPublishQueue(recipe.id)
            }

            else -> println("Нет такого пункта меню")
        }
    }

    override fun inputRecipe(recipeId: ULong): Recipe? {
        val uId = AccountService.getCurrentUserId()
        return if (uId != null) {
            val user = facade.getUser(uId)
            val ingredients = facade.getAllIngredients()
            view.inputRecipe(user, recipeId, ingredients)
        } else null
    }
}