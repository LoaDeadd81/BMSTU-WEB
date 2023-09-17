package controllers.factory

import bl.Facade
import controllers.interfaces.*
import techUi.controllers.*

class TechControllerFactory(facade: Facade) : IControllerFactory(facade) {
    override fun createFeedController(): IFeedController = TechFeedController(facade)

    override fun createAccountController(): IAccountController = TechAccountController(facade)

    override fun createUserController(): IUserController = TechUserController(facade)

    override fun createPublishQueueController(): IPublishQueueController = TechPublishQueueController(facade)

    override fun createRecipeController(): IRecipeController = TechRecipeController(facade)

    override fun createCommentController(): ICommentController = TechCommentController(facade)
}