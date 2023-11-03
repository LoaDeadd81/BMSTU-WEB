package api.dto.requests.user

import bl.entities.User
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUser(var login: String, var password: String, var isAdmin: Boolean) {
    fun toBLEntity(id: Int = 0): User {
        return User(id, login, password, isAdmin)
    }
}
