package bl.managers

import bl.entities.RecipePreview
import bl.entities.User
import bl.exceptions.*
import bl.repositories.IRepository
import bl.repositories.IUserRepository
import org.slf4j.LoggerFactory

object UserManager : ICRUDManager<User> {
    private lateinit var repository: IUserRepository

    private val logger = LoggerFactory.getLogger("mainLogger")

    override fun registerRepository(repository: IRepository<User>) {
        this.repository = repository as IUserRepository
    }

    override fun create(obj: User) {
        logger.trace("{} called with parameters {}", ::create.name, obj)

        if (!isUniq(obj)) throw AlreadyExistingUserException("User already exists")
        if (!validate(obj)) throw ValidationUserException("User failed validation")

        repository.create(obj)
    }

    override fun read(id: ULong): User {
        logger.trace("{} called with parameters {}", ::read.name, id)

        if (!isExist(id)) throw NotExistingUserException("User not exists")

        return repository.read(id)
    }

    override fun update(obj: User) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (uId != obj.id && !isAdmin(uId)) throw AccessDeniedException("Access denied")
        if (!isExist(obj.id)) throw NotExistingUserException("User not exists")
        if (!validate(obj)) throw ValidationUserException("User failed validation")

        repository.update(obj)
    }

    override fun delete(id: ULong) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (uId != id && !isAdmin(uId)) throw AccessDeniedException("Access denied")
        if (!isExist(id)) throw NotExistingUserException("User not exists")

        repository.delete(id)
    }

    override fun getAll(): List<User> {
        logger.trace("{} called", ::getAll.name)
        return repository.getAll()
    }

    override fun isUniq(obj: User): Boolean {
        logger.trace("{} called with parameters {}", ::isUniq.name, obj)

        return repository.isLoginNotExist(obj.login)
    }

    override fun isExist(id: ULong): Boolean {
        logger.trace("{} called with parameters {}", ::isExist.name, id)

        return repository.exists(id)
    }

    override fun validate(obj: User): Boolean {
        logger.trace("{} called with parameters {}", ::validate.name, obj)

        return obj.login.isNotEmpty() && obj.password.isNotEmpty()
    }

    fun getByLogin(login: String): User {
        logger.trace("{} called with parameters {}", ::getByLogin.name, login)

        return repository.getByLogin(login) ?: throw NotExistingUserException("User not exists")
    }

    fun getSavedRecipes(userId: ULong): List<RecipePreview> {
        logger.trace("{} called with parameters {}", ::getSavedRecipes.name, userId)

        if (!isExist(userId)) throw NotExistingUserException("User not exists")

        return repository.getSavedRecipes(userId)
    }

    fun getOwnRecipes(userId: ULong): List<RecipePreview> {
        logger.trace("{} called with parameters {}", ::getOwnRecipes.name, userId)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (uId != userId && !isAdmin(uId)) throw AccessDeniedException("Access denied")
        if (!isExist(userId)) throw NotExistingUserException("User not exists")

        return repository.getOwnRecipes(userId)
    }

    fun getPublishedRecipes(userID: ULong): List<RecipePreview> {
        logger.trace("{} called with parameters {}", ::getPublishedRecipes.name, userID)

        if (!isExist(userID)) throw NotExistingUserException("User not exists")

        return repository.getPublishedRecipes(userID)
    }

    fun isAdmin(id: ULong): Boolean {
        logger.trace("{} called with parameters {}", ::isAdmin.name, id)

        if (!isExist(id)) throw NotExistingUserException("User not exists")

        return repository.isAdmin(id)
    }

}