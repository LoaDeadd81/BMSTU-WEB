package controllers.interfaces

import bl.Facade

abstract class IPublishQueueController(protected val facade: Facade) {
    abstract fun showQueue()
}