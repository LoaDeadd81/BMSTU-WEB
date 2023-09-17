package controllers.interfaces

import bl.Facade

abstract class IFeedController(protected val facade: Facade) {
    abstract fun showFeed()
    abstract fun showOwnFeed(userId: ULong)
    abstract fun showSavedFeed(userId: ULong)
}