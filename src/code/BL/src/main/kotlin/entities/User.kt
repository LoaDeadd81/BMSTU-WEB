package bl.entities

data class User(var id: Int, var login: String = "", var password: String = "", var isAdmin: Boolean = false)
