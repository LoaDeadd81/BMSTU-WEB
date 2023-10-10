package api.dto.responces.user

import kotlinx.serialization.Serializable
import bl.entities.User

@Serializable
data class UserResponse(var id: Int, var login: String, var isAdmin: Boolean) {
    constructor(user: User) : this(user.id, user.login, user.isAdmin)
}