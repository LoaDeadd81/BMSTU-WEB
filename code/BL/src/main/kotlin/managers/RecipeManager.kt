package bl.managers

import bl.entities.Recipe
import bl.entities.RecipePreview
import bl.entities.RecipeState
import bl.entities.Stage
import bl.exceptions.AccessDeniedException
import bl.exceptions.NotAuthorizedException
import bl.exceptions.ValidationException
import bl.repositories.IRecipeRepository
import org.slf4j.LoggerFactory

object RecipeManager {
    private lateinit var repository: IRecipeRepository

    private val logger = LoggerFactory.getLogger("mainLogger")

    fun registerRepository(repository: IRecipeRepository) {
        this.repository = repository
    }

    fun create(obj: Recipe): Int {
        logger.trace("{} called with parameters {}", ::create.name, obj)

        AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!validate(obj)) throw ValidationException("Recipe failed validation")
        for (i in obj.stages) {
            if (!validateStage(i)) throw ValidationException("Stage failed validation")
        }

        return repository.create(obj)
    }

    fun read(id: Int): Recipe {
        logger.trace("{} called with parameters {}", ::read.name, id)

        val recipe = repository.read(id)
        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (recipe.state != RecipeState.PUBLISHED && !UserManager.isAdmin(uId) && !isOwner(id, uId))
            throw AccessDeniedException("Access denied")

        return recipe
    }

    fun update(obj: Recipe) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId)) throw AccessDeniedException("Access denied")
        if (!validate(obj)) throw ValidationException("Recipe failed validation")

        repository.update(obj)
    }

    fun delete(id: Int) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId) && !isOwner(id, uId)) throw AccessDeniedException("Access denied")

        repository.delete(id)
    }

    fun getAll(): List<RecipePreview> {
        logger.trace("{} called", ::getAll.name)

        return repository.getAll()
    }

    fun updateInfo(obj: Recipe): Recipe {
        logger.trace("{} called with parameters {}", ::updateInfo.name, obj)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId) && !isOwner(obj.id, uId)) throw AccessDeniedException("Access denied")
        if (!validate(obj)) throw ValidationException("Recipe failed validation")
        if (!UserManager.isAdmin(uId) && (obj.state == RecipeState.CLOSE || obj.state == RecipeState.PUBLISHED)) throw AccessDeniedException(
            "Access denied"
        )

        return repository.updateInfo(obj)
    }

    fun updateStages(id: Int, list: List<Stage>): Recipe {
        logger.trace("{} called with parameters {}, {}", ::updateInfo.name, id, list)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId) && !isOwner(id, uId)) throw AccessDeniedException("Access denied")
        for (i in list) {
            if (!validateStage(i)) throw ValidationException("Stage failed validation")
        }

        return repository.updateStages(id, list)
    }

    fun getPublishQueue(): List<RecipePreview> {
        logger.trace("{} called", ::getPublishQueue.name)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId)) throw AccessDeniedException("Access denied")

        return repository.getPublishQueue()
    }

    private fun validate(obj: Recipe): Boolean {
        logger.trace("{} called with parameters {}", ::validate.name, obj)

        return obj.name.isNotEmpty() && obj.description.isNotEmpty() && obj.time > -1 && obj.servingsNum > 0 &&
                obj.pfc.protein > -1 && obj.pfc.fat > -1 && obj.pfc.carbon > -1
    }

    private fun validateStage(obj: Stage): Boolean {
        logger.trace("{} called with parameters {}", ::validateStage.name, obj)

        return obj.time > 0 && obj.description.isNotEmpty() && obj.orderNum > -1
    }

    private fun isOwner(id: Int, userID: Int): Boolean {
        logger.trace("{} called with parameters {}, {}", ::isOwner.name, id, userID)

        return repository.getOwnerID(id) == userID
    }
}