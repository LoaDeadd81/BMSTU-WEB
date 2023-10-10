package bl.entities

import java.time.LocalDateTime

data class Comment(var id: Int, var date: LocalDateTime, var text: String, var autor: User)
