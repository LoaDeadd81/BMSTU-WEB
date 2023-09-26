package bl.managers

import bl.entities.User
import bl.exceptions.LogInFailedException
import org.slf4j.LoggerFactory

object AccountService {
    private var currentUser: User? = null

    private val logger = LoggerFactory.getLogger("mainLogger")

    fun register(login: String, password: String) {
        logger.trace("{} called with parameters {}", ::register.name, login)

        val user = User(0u, login, password, false)
        UserManager.create(user)

        currentUser = UserManager.getByLogin(login)

        logger.info("User with login {} registered", login)
    }

    fun logIN(login: String, password: String): User {
        logger.trace("{} called with parameters {}", ::logIN.name, login)

        val user = UserManager.getByLogin(login)
        if (user.password != passwordConvert(password)) {
            logger.debug("User with login {} failed logging", login)
            throw LogInFailedException("LogIn failed")
        } else {
            currentUser = user
            return user
        }
    }

    fun getCurrentUserId(): ULong? {
        logger.trace("{} called", ::getCurrentUserId.name)

        return currentUser?.id
    }

    fun isCurrentUserAdmin(): Boolean? {
        logger.trace("{} called", ::getCurrentUserId.name)

        return currentUser?.isAdmin
    }

    fun clearUser() {
        logger.trace("{} called", ::clearUser.name)

        currentUser = null
    }


    private fun passwordConvert(password: String): String { //todo
        logger.trace("{} called", ::passwordConvert.name)

        return password
    }
}