package techUi.controllers

import bl.Facade
import bl.exceptions.LogInFailedException
import bl.exceptions.NotExistingUserException
import controllers.interfaces.IAccountController
import org.slf4j.LoggerFactory
import techUi.views.TechTechAccountView

class TechAccountController(facade: Facade) : IAccountController(facade) {

    private val view = TechTechAccountView()
    private val logger = LoggerFactory.getLogger("mainLogger")
    override fun register() {
        logger.debug("Showed registration form")

        try {
            val pair = view.logInForm()
            val login = pair.first
            val password = pair.second
            facade.register(login, password)
        } catch (e: Exception) {
            println("Ошибка")
            println(e.message)
            logger.warn("Exception", e)
        }
        println()
    }

    override fun login() {
        var login = ""
        try {
            val pair = view.logInForm()
            login = pair.first
            val password = pair.second
            facade.logIn(login, password)
        } catch (e: NotExistingUserException) {
            println("Неверный логин")
            logger.debug("Log in with login {} failed", login)
        } catch (e: LogInFailedException) {
            println("Неверный пароль")
            logger.debug("Log in with login {} failed", login)
        } catch (e: Exception) {
            println("Ошибка")
            println(e.message)
            logger.warn("Exception", e)
        }
        logger.debug("User with login {} logged in", login)
        println()
    }
}