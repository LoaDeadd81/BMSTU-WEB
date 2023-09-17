package controllers.interfaces

import bl.Facade

abstract class ICommentController(protected val facade: Facade) {
    abstract fun addComment(recipeId: ULong)
}