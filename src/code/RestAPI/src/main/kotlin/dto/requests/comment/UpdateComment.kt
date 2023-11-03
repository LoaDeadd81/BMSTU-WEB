package api.dto.requests.comment

import bl.entities.Comment
import bl.entities.User
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UpdateComment(var date: LocalDateTime, var text: String, var user_id: Int) {
    fun toBLEntity(id: Int): Comment {
        return Comment(id, date.toJavaLocalDateTime(), text, User(user_id))
    }
}

