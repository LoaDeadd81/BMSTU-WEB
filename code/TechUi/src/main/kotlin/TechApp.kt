package techUi

import bl.Facade
import bl.managers.AccountService
import controllers.factory.TechControllerFactory
import techUi.views.TechMenuView

class TechApp(private val facade: Facade, controllerFactory: TechControllerFactory) {
    private val accountController = controllerFactory.createAccountController()
    private val feedController = controllerFactory.createFeedController()
    private val userController = controllerFactory.createUserController()
    private val publishQueueController = controllerFactory.createPublishQueueController()
    private val recipeController = controllerFactory.createRecipeController()

    private val view = TechMenuView()
    fun menu() {
        while (true) {
            val uId = AccountService.getCurrentUserId()
            var req: Int
            if (uId == null) {
                req = view.printUnAuthMenu()
                if (req == 0) break
                handleUnAuthReq(req)
                println()
            } else {
                if (facade.isAdmin(uId)) {
                    req = view.printAdminMenu()
                    if (req == 0) {
                        AccountService.clearUser()
                        continue
                    }
                    handleAdminReq(req)
                    println()
                } else {
                    req = view.printAuthMenu()
                    if (req == 0) {
                        AccountService.clearUser()
                        continue
                    }
                    handleAuthReq(req)
                    println()
                }
            }
        }
    }

    private fun handleUnAuthReq(req: Int) {
        when (req) {
            1 -> accountController.register()
            2 -> accountController.login()
            3 -> feedController.showFeed()
            else -> println("Нет такого пункта меню")
        }
    }

    private fun handleAuthReq(req: Int) {
        when (req) {
            1 -> feedController.showFeed()
            2 -> {
                val uId = AccountService.getCurrentUserId()
                if (uId != null) userController.printUser(uId)
            }

            3 -> {
                val uId = AccountService.getCurrentUserId()
                if (uId != null) feedController.showOwnFeed(uId)
            }

            4 -> {
                val rec = recipeController.inputRecipe(0u)
                if (rec != null) facade.createRecipe(rec)
            }

            else -> println("Нет такого пункта меню")
        }
    }

    private fun handleAdminReq(req: Int) {
        when (req) {
            1 -> feedController.showFeed()
            2 -> {
                val uId = AccountService.getCurrentUserId()
                if (uId != null) userController.printUser(uId)
            }

            3 -> {
                val uId = AccountService.getCurrentUserId()
                if (uId != null) feedController.showOwnFeed(uId)
            }

            4 -> {
                val rec = recipeController.inputRecipe(0u)
                if (rec != null) facade.createRecipe(rec)
            }

            5 -> {
                publishQueueController.showQueue()
            }

            else -> println("Нет такого пункта меню")
        }
    }
}