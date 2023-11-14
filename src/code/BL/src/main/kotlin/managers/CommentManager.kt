package bl.managers

import bl.entities.Comment
import bl.exceptions.AccessDeniedException
import bl.exceptions.NotAuthorizedException
import bl.exceptions.ValidationException
import bl.repositories.ICommentRepository
import org.slf4j.LoggerFactory


object CommentManager {
    private lateinit var repository: ICommentRepository

    private val logger = LoggerFactory.getLogger("mainLogger")

    fun registerRepository(repository: ICommentRepository) {
        this.repository = repository
    }

    fun create(text: String, recipeID: Int): Int {
        logger.trace("{} called with parameters {}, {}", ::create.name, text, recipeID)

        val userID = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!validateText(text)) throw ValidationException("Comment failed validation")

        return repository.create(userID, text, recipeID)
    }

    fun read(id: Int): Comment {
        logger.trace("{} called with parameters {}", ::read.name, id)

        return repository.read(id)
    }

    fun update(obj: Comment) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId)) throw AccessDeniedException("Access denied")
        if (!validate(obj)) throw ValidationException("Comment failed validation")


        repository.update(obj)
    }

    fun delete(id: Int) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId)) throw AccessDeniedException("Access denied")

        repository.delete(id)
    }

    fun updateText(id: Int, text: String): Comment {
        logger.trace("{} called with parameters {}, {}", ::updateText.name, id, text)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId) && !isOwner(id, uId)) throw AccessDeniedException("Access denied")
        if (!validateText(text)) throw ValidationException("Comment failed validation")

        return repository.updateText(id, text)
    }

    private fun validateText(text: String): Boolean {
        logger.trace("{} called with parameters {}", ::validate.name, text)

        return text.isNotEmpty()
    }

    private fun validate(obj: Comment): Boolean {
        logger.trace("{} called with parameters {}", ::validate.name, obj)

        return obj.text.isNotEmpty()
    }

    fun isOwner(id: Int, userID: Int): Boolean {
        logger.trace("{} called with parameters {}, {}", ::isOwner.name, id, userID)

        return repository.getOwnerID(id) == userID
    }
}