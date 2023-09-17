package controllers.interfaces

import bl.Facade
import bl.entities.User

abstract class IUserController(protected val facade: Facade) {
    abstract fun printUser(userId: ULong)
    abstract fun printUser(user: User)
}