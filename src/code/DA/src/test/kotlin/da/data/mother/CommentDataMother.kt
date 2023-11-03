package da.data.mother

import bl.entities.Comment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object CommentDataMother {
    fun updatedComment(): Comment {
        return Comment(2, LocalDateTime.now(), "text_new", UserDataMother.admin())
    }

    fun regularCommentWithNewText(): Comment {
        return Comment(2, LocalDateTime.now(), "newtext", UserDataMother.regular())
    }

    fun notExistComment(): Comment {
        return Comment(9, LocalDateTime.now(), "text", UserDataMother.regular())
    }

    fun newComment(): Comment {
        return Comment(6, LocalDateTime.now(), "text", UserDataMother.regular())
    }

    fun regularComment(): Comment {
        return Comment(
            1,
            LocalDateTime.parse("2023-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            "text1",
            UserDataMother.admin()
        )
    }
}
