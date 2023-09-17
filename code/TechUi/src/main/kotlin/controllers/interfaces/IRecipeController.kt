package controllers.interfaces

import bl.Facade
import bl.entities.Recipe

abstract class IRecipeController(protected val facade: Facade) {
    abstract fun printRecipe(recipeId: ULong)
    abstract fun inputRecipe(recipeId: ULong): Recipe?
}