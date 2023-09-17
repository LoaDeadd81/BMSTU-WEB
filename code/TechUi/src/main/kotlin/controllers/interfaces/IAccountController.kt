package controllers.interfaces

import bl.Facade

abstract class IAccountController(protected val facade: Facade) {
    abstract fun register()

    abstract fun login()
}