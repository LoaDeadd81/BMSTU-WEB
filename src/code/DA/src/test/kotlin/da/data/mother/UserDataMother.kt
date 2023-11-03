package da.data.mother

import bl.entities.User

object UserDataMother {
    fun admin(): User {
        return User(1, "login1", "password1", true)
    }

    fun regular(): User {
        return User(2, "login2", "password2", false)
    }

    fun created(): User {
        return User(0, "crl", "crp", false)
    }

    fun updated(): User {
        return User(2, "new", "new", false)
    }

    fun notExist(): User {
        return User(9, "new", "password2", true)
    }

    fun all(): List<User> {
        return listOf(
            User(1, "login1", "password1", true),
            User(2, "login2", "password2", false),
            User(3, "login3", "password3", false),
            User(4, "login4", "password4", false),
            User(5, "login5", "password5", false)
        )
    }
}