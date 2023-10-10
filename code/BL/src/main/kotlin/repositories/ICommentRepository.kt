package bl.repositories

import bl.entities.Comment

interface ICommentRepository {

    fun create(userID: Int, text: String, recipeID: Int): Int
    fun update(obj: Comment)
    fun delete(id: Int)
    fun updateText(id: Int, text: String): Comment
    fun getOwnerID(id: Int): Int //todo
}