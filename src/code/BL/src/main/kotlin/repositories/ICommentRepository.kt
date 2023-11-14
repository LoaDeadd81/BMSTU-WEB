package bl.repositories

import bl.entities.Comment

interface ICommentRepository {

    fun create(userID: Int, text: String, recipeID: Int): Int
    fun read(id: Int) : Comment
    fun update(obj: Comment)
    fun delete(id: Int)
    fun updateText(id: Int, text: String): Comment
    fun getOwnerID(id: Int): Int
}