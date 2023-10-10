package bl.managers

import bl.entities.User
import bl.exceptions.LogInFailedException
import org.slf4j.LoggerFactory

object AccountService {
    private var currentUserID: Int? = null

    private val logger = LoggerFactory.getLogger("mainLogger")

    fun register(login: String, password: String): Int {
        logger.trace("{} called with parameters {}", ::register.name, login)

        val id = UserManager.create(login, password)

        logger.info("User with login {} registered", login)

        return id
    }

    fun logIN(login: String, password: String): User {
        logger.trace("{} called with parameters {}", ::logIN.name, login)

        val user = UserManager.getByLogin(login)
        if (user.password != passwordConvert(password)) {
            logger.debug("User with login {} failed logging", login)
            throw LogInFailedException("LogIn failed")
        } else {
            currentUserID = user.id
            return user
        }
    }

    fun setId(id: Int) {
        currentUserID = id
    }

    fun getCurrentUserId(): Int? {
        logger.trace("{} called", ::getCurrentUserId.name)

        return currentUserID
    }

    fun clearUser() {
        logger.trace("{} called", ::clearUser.name)

        currentUserID = null
    }


    private fun passwordConvert(password: String): String {
        logger.trace("{} called", ::passwordConvert.name)

        return password
    }
}