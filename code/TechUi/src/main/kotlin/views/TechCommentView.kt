package techUi.views

import bl.entities.Comment

class TechCommentView {
    fun print(comment: Comment, tab: String = "") {
        println(tab + "id: " + comment.id)
        println(tab + "Автор: " + comment.autor.login)
        println(tab + "Дата создания: " + comment.date)
        println(tab + "Текст: " + comment.text)
    }

    fun inputPrompt(): String {
        print("Введите текст комментария: ")
        val text = readln()
        return text
    }
}