package bl.entities

import java.time.LocalDateTime

data class Comment(val id: ULong, var date: LocalDateTime, var text: String, var autor: User)
