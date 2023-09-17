package techUi.controllers

import bl.Facade
import bl.entities.User
import controllers.interfaces.IUserController
import org.slf4j.LoggerFactory
import techUi.views.TechUserView

class TechUserController(facade: Facade) : IUserController(facade) {
    private val view = TechUserView()
    private val logger = LoggerFactory.getLogger("mainLogger")

    override fun printUser(userId: ULong) {
        logger.debug("User with id {} showed", userId)

        try {
            while (true) {
                val user = facade.getUser(userId)
                val recipes = facade.getSavedFeed(userId)
                val req = view.print(user, recipes)
                if (req == 0) break
            }
        } catch (e: Exception) {
            println("Ошибка")
            println(e.message)
            logger.warn("Exception", e)
        }
        println()
    }

    override fun printUser(user: User) {
        logger.debug("User with id {} showed", user.id)

        try {
            while (true) {
                val recipes = facade.getSavedFeed(user.id)
                val req = view.print(user, recipes)
                if (req == 0) break
            }
        } catch (e: Exception) {
            println("Ошибка")
            println(e.message)
            logger.warn("Exception", e)
        }
        println()
    }
}