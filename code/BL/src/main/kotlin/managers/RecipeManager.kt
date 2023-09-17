package bl.managers

import bl.entities.Comment
import bl.entities.Recipe
import bl.entities.RecipePreview
import bl.entities.Stage
import bl.exceptions.*
import bl.repositories.IRecipeRepository
import bl.repositories.IRepository
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

object RecipeManager : ICRUDManager<Recipe> {
    private lateinit var repository: IRecipeRepository

    private val logger = LoggerFactory.getLogger("mainLogger")

    override fun registerRepository(repository: IRepository<Recipe>) {
        this.repository = repository as IRecipeRepository
    }

    override fun create(obj: Recipe) {
        logger.trace("{} called with parameters {}", ::create.name, obj)

        AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!isUniq(obj)) throw AlreadyExistingRecipeException("Recipe already exists")
        if (!validate(obj)) throw ValidationRecipeException("Recipe failed validation")
        for (i in obj.stages) {
            if (!StageManager.validate(i)) throw ValidationIngredientException("Stage failed validation")
        }

        repository.create(obj)
    }

    override fun read(id: ULong): Recipe {
        logger.trace("{} called with parameters {}", ::read.name, id)

        if (!isExist(id)) throw NotExistingRecipeException("Recipe not exists")
        val recipe = repository.read(id)
        val uId = AccountService.getCurrentUserId()
        if (!recipe.published && (uId != null && !UserManager.isAdmin(uId) && !isOwner(id, uId)))
            throw AccessDeniedException("Access denied")

        return recipe
    }

    override fun update(obj: Recipe) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId) && !isOwner(obj.id, uId)) throw AccessDeniedException("Access denied")
        if (!isExist(obj.id)) throw NotExistingRecipeException("Recipe not exists")
        if (!validate(obj)) throw ValidationRecipeException("Recipe failed validation")

        repository.update(obj)
    }

    override fun delete(id: ULong) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId) && !isOwner(id, uId)) throw AccessDeniedException("Access denied")
        if (!isExist(id)) throw NotExistingRecipeException("Recipe not exists")

        repository.delete(id)
    }

    override fun getAll(): List<Recipe> {
        logger.trace("{} called", ::getAll.name)

        return repository.getAll()
    }

    override fun isUniq(obj: Recipe) = true

    override fun isExist(id: ULong): Boolean {
        logger.trace("{} called with parameters {}", ::isExist.name, id)

        return repository.exists(id)
    }

    override fun validate(obj: Recipe): Boolean {
        logger.trace("{} called with parameters {}", ::validate.name, obj)

        return obj.name.isNotEmpty() && obj.description.isNotEmpty() && obj.time > 0u && obj.servingsNum > 0u &&
                obj.stages.isNotEmpty()
    }

    fun getPublishQueue(): List<RecipePreview> {
        logger.trace("{} called", ::getPublishQueue.name)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId)) throw AccessDeniedException("Access denied")

        return repository.getPublishQueue()
    }

    fun publish(id: ULong) {
        logger.trace("{} called with parameters {}", ::publish.name, id)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId) && !isOwner(id, uId)) throw AccessDeniedException("Access denied")
        if (!isExist(id)) throw NotExistingRecipeException("Recipe not exists")
        if (isPublished(id)) throw RecipeAlreadyPublishedException("Recipe already published")
        if (isInPublishQueue(id)) return

        repository.addToPublishQueue(id)
    }

    fun approvePublication(id: ULong) {
        logger.trace("{} called with parameters {}", ::approvePublication.name, id)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId)) throw AccessDeniedException("Access denied")
        if (!isExist(id)) throw NotExistingRecipeException("Recipe not exists")
        if (!isInPublishQueue(id)) throw RecipeNotInPublishQueueException("Recipe not in publish queue")

        repository.approvePublication(id)
    }

    fun cancelRecipePublication(id: ULong) {
        logger.trace("{} called with parameters {}", ::cancelRecipePublication.name, id)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId)) throw AccessDeniedException("Access denied")
        if (!isExist(id)) throw NotExistingRecipeException("Recipe not exists")
        if (!isInPublishQueue(id)) throw RecipeNotInPublishQueueException("Recipe not in publish queue")

        repository.cancelPublication(id)
    }

    fun addToFavorite(id: ULong, userID: ULong) {
        logger.trace("{} called with parameters {}, {}", ::addToFavorite.name, id, userID)

        AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!isExist(id)) throw NotExistingRecipeException("Recipe not exist")
        if (!UserManager.isExist(userID)) throw NotExistingUserException("User not exist")
        if (isInFavorite(id, userID)) return

        repository.addToFavorite(id, userID)
    }

    fun isInFavorite(id: ULong, userID: ULong): Boolean {
        logger.trace("{} called with parameters {}, {}", ::isInFavorite.name, id, userID)

        return repository.isInFavorite(id, userID)
    }

    fun deleteFromFavorite(id: ULong, userID: ULong) {
        logger.trace("{} called with parameters {}, {}", ::deleteFromFavorite.name, id, userID)

        return repository.deleteFromFavorite(id, userID)
    }


    fun isOwner(id: ULong, userID: ULong): Boolean {
        logger.trace("{} called with parameters {}, {}", ::isOwner.name, id, userID)

        if (!isExist(id)) throw NotExistingRecipeException("Recipe not exist")
        if (!UserManager.isExist(userID)) throw NotExistingUserException("User not exist")

        return repository.getOwnerID(id) == userID
    }

    fun addComment(userID: ULong, text: String, recipeID: ULong) {
        logger.trace("{} called with parameters {}, {}, {}", ::addComment.name, userID, text, recipeID)

        AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isExist(userID)) throw NotExistingUserException("User not exists")
        if (!isExist(recipeID)) throw NotExistingRecipeException("Recipe not exists")

        val owner = UserManager.read(userID)
        val comment = Comment(0u, LocalDateTime.now(), text, owner)

        if (!CommentManager.validate(comment)) throw ValidationCommentException("Comment failed validation")

        repository.addComment(userID, recipeID, comment)
    }

    fun addStage(recipeID: ULong, stage: Stage) {
        logger.trace("{} called with parameters {}, {}", ::addStage.name, recipeID, stage)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId) && !isOwner(recipeID, uId)) throw AccessDeniedException("Access denied")
        if (!StageManager.validate(stage)) throw ValidationStageException("Stage failed validation")
        if (!isExist(recipeID)) throw NotExistingRecipeException("Recipe not exist")

        repository.addStage(recipeID, stage)
    }

    private fun isInPublishQueue(id: ULong): Boolean {
        logger.trace("{} called with parameters {}", ::isInPublishQueue.name, id)

        return repository.isInPublishQueue(id)
    }

    private fun isPublished(id: ULong): Boolean {
        logger.trace("{} called with parameters {}", ::isPublished.name, id)

        return repository.isPublished(id)
    }
}