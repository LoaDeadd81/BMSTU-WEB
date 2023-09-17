package bl.entities

data class User(val id: ULong, var login: String, var password: String, var isAdmin: Boolean)
