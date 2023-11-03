package bl.managers

import bl.entities.Ingredient
import bl.exceptions.AccessDeniedException
import bl.exceptions.AlreadyExistException
import bl.exceptions.NotAuthorizedException
import bl.exceptions.ValidationException
import bl.repositories.IIngredientRepository
import org.slf4j.LoggerFactory


object IngredientManager {
    private lateinit var repository: IIngredientRepository

    private val logger = LoggerFactory.getLogger("mainLogger")

    fun registerRepository(repository: IIngredientRepository) {
        this.repository = repository
    }

    fun create(obj: Ingredient): Int {
        logger.trace("{} called with parameters {}", ::create.name, obj)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId)) throw AccessDeniedException("Access denied")
        if (!isUniq(obj)) throw AlreadyExistException("Ingredient already exists")
        if (!validate(obj)) throw ValidationException("Ingredient failed validation")

        return repository.create(obj)
    }

    fun read(id: Int): Ingredient {
        logger.trace("{} called with parameters {}", ::read.name, id)

        return repository.read(id)
    }

    fun update(obj: Ingredient) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId)) throw AccessDeniedException("Access denied")
        if (!validate(obj)) throw ValidationException("Ingredient failed validation")

        repository.update(obj)
    }

    fun delete(id: Int) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId)) throw AccessDeniedException("Access denied")

        repository.delete(id)
    }

    fun getAll(): List<Ingredient> {
        logger.trace("{} called", ::getAll.name)

        return repository.getAll()
    }

    fun findByName(name: String): List<Ingredient> {
        logger.trace("{} called with parameters {}", ::findByName.name, name)

        return repository.findByName(name)
    }

    private fun isUniq(obj: Ingredient): Boolean {
        logger.trace("{} called with parameters {}", ::isUniq.name, obj)

        return repository.isNameNotExist(obj.name)
    }

    private fun validate(obj: Ingredient): Boolean {
        logger.trace("{} called with parameters {}", ::validate.name, obj)

        return obj.name.isNotEmpty() && obj.nutritionalValue > 0
    }
}