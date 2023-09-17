package techUi.controllers

import bl.Facade
import bl.exceptions.BaseException
import bl.managers.AccountService
import controllers.interfaces.ICommentController
import org.slf4j.LoggerFactory
import techUi.views.TechCommentView

class TechCommentController(facade: Facade) : ICommentController(facade) {
    private val view = TechCommentView()
    private val logger = LoggerFactory.getLogger("mainLogger")
    override fun addComment(recipeId: ULong) {
        logger.debug("Showed add comment form")

        val uId = AccountService.getCurrentUserId()
        if (uId != null) {
            try {
                val text = view.inputPrompt()
                facade.addComment(uId, text, recipeId)
            } catch (e: BaseException) {
                println("Ошибка")
                println(e.message)
                logger.warn("Exception", e)
            }
            println()
        }
    }
}