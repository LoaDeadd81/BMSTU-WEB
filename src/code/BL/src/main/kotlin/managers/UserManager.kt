package bl.managers

import bl.entities.RecipePreview
import bl.entities.User
import bl.exceptions.AccessDeniedException
import bl.exceptions.AlreadyExistException
import bl.exceptions.NotAuthorizedException
import bl.exceptions.ValidationException
import bl.repositories.IUserRepository
import org.slf4j.LoggerFactory

object UserManager {
    private lateinit var repository: IUserRepository

    private val logger = LoggerFactory.getLogger("mainLogger")

    fun registerRepository(repository: IUserRepository) {
        this.repository = repository
    }

    fun create(login: String, password: String): Int {
        logger.trace("{} called with parameters {}, {}", ::create.name, login, password)

        if (!isUniq(login)) throw AlreadyExistException("User already exists")
        if (!validateLP(login, password)) throw ValidationException("User failed validation")

        return repository.create(login, password)
    }

    fun read(id: Int): User {
        logger.trace("{} called with parameters {}", ::read.name, id)

        return repository.read(id)
    }

    fun update(obj: User) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!isAdmin(uId)) throw AccessDeniedException("Access denied")
        if (!validate(obj)) throw ValidationException("User failed validation")

        repository.update(obj)
    }

    fun delete(id: Int) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (uId != id && !isAdmin(uId)) throw AccessDeniedException("Access denied")

        repository.delete(id)
    }

    fun getAll(): List<User> {
        logger.trace("{} called", ::getAll.name)
        return repository.getAll()
    }

    fun updateCredentials(id: Int, login: String, password: String): User {
        logger.trace("{} called with parameters {}, {}, {}", ::updateCredentials.name, id, login, password)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!isAdmin(uId) && uId != id) throw AccessDeniedException("Access denied")
        if (!validateLP(login, password)) throw ValidationException("User failed validation")

        return repository.updateCredentials(id, login, password)
    }

    fun addToFavorite(id: Int, recipeId: Int) {
        logger.trace("{} called with parameters {}, {}", ::updateCredentials.name, id, recipeId)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!isAdmin(uId) && uId != id) throw AccessDeniedException("Access denied")

        repository.addToFavorite(id, recipeId)
    }

    fun deleteFromFavorite(id: Int, recipeId: Int) {
        logger.trace("{} called with parameters {}, {}", ::deleteFromFavorite.name, id, recipeId)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!isAdmin(uId) && uId != id) throw AccessDeniedException("Access denied")

        repository.deleteFromFavorite(id, recipeId)
    }

    fun isInFavorite(id: Int, recipeId: Int) { //todo add ro resp
        logger.trace("{} called with parameters {}, {}", ::isInFavorite.name, id, recipeId)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!isAdmin(uId) && uId != id) throw AccessDeniedException("Access denied")

        repository.isInFavorite(id, recipeId)
    }

    fun getPublishedRecipes(userID: Int): List<RecipePreview> {
        logger.trace("{} called with parameters {}", ::getPublishedRecipes.name, userID)

        return repository.getPublishedRecipes(userID)
    }

    fun isAdmin(id: Int): Boolean {
        logger.trace("{} called with parameters {}", ::isAdmin.name, id)


        return repository.isAdmin(id)
    }

    fun getByLogin(login: String): User {
        logger.trace("{} called with parameters {}", ::getByLogin.name, login)

        return repository.getByLogin(login)
    }

    private fun isUniq(login: String): Boolean {
        logger.trace("{} called with parameters {}", ::isUniq.name, login)

        return repository.isLoginNotExist(login)
    }

    private fun validateLP(login: String, password: String): Boolean {
        logger.trace("{} called with parameters {}, {}", ::validateLP.name, login, password)

        return login.isNotEmpty() && password.isNotEmpty()
    }

    private fun validate(obj: User): Boolean {
        logger.trace("{} called with parameters {}", ::validate.name, obj)

        return obj.login.isNotEmpty() && obj.password.isNotEmpty()
    }


}