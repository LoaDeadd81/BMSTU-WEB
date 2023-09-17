package bl.managers

import bl.entities.Ingredient
import bl.exceptions.*
import bl.repositories.IIngredientRepository
import bl.repositories.IRepository
import org.slf4j.LoggerFactory


object IngredientManager : ICRUDManager<Ingredient> {
    private lateinit var repository: IIngredientRepository

    private val logger = LoggerFactory.getLogger("mainLogger")

    override fun registerRepository(repository: IRepository<Ingredient>) {
        this.repository = repository as IIngredientRepository
    }

    override fun create(obj: Ingredient) {
        logger.trace("{} called with parameters {}", ::create.name, obj)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId)) {
            throw AccessDeniedException("Access denied")
        }
        if (!isUniq(obj)) {
            throw AlreadyExistingIngredientException("Ingredient already exists")
        }
        if (!validate(obj)) {
            throw ValidationIngredientException("Ingredient failed validation")
        }

        repository.create(obj)
    }

    override fun read(id: ULong): Ingredient {
        logger.trace("{} called with parameters {}", ::read.name, id)

        if (!isExist(id)) {
            throw NotExistingIngredientException("Ingredient not exists")
        }

        return repository.read(id)
    }

    override fun update(obj: Ingredient) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId)) {
            throw AccessDeniedException("Access denied")
        }
        if (!isExist(obj.id)) {
            throw NotExistingIngredientException("Ingredient not exists")
        }
        if (!validate(obj)) {
            throw ValidationIngredientException("Ingredient failed validation")
        }

        repository.update(obj)
    }

    override fun delete(id: ULong) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId)) {
            throw AccessDeniedException("Access denied")
        }
        if (!isExist(id)) {
            throw NotExistingIngredientException("Ingredient not exists")
        }

        repository.delete(id)
    }

    override fun getAll(): List<Ingredient> {
        logger.trace("{} called", ::getAll.name)

        return repository.getAll()
    }

    override fun isUniq(obj: Ingredient): Boolean {
        logger.trace("{} called with parameters {}", ::isUniq.name, obj)

        return repository.isNameNotExist(obj.name)
    }

    override fun isExist(id: ULong): Boolean {
        logger.trace("{} called with parameters {}", ::isExist.name, id)

        return repository.exists(id)
    }

    fun findByName(name: String): Ingredient {
        logger.trace("{} called with parameters {}", ::findByName.name, name)

        val tmp = repository.findByName(name)
        if (tmp == null) {
            throw NotExistingIngredientException("Ingredient not exists")
        } else return tmp
    }

    override fun validate(obj: Ingredient): Boolean {
        logger.trace("{} called with parameters {}", ::validate.name, obj)

        return obj.name.isNotEmpty() && obj.nutritionalValue > 0u
    }
}