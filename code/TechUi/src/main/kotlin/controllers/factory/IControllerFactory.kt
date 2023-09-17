package controllers.factory

import bl.Facade
import controllers.interfaces.*

abstract class IControllerFactory(protected val facade: Facade) {
    abstract fun createFeedController(): IFeedController
    abstract fun createAccountController(): IAccountController
    abstract fun createUserController(): IUserController
    abstract fun createPublishQueueController(): IPublishQueueController
    abstract fun createRecipeController(): IRecipeController
    abstract fun createCommentController() : ICommentController
}