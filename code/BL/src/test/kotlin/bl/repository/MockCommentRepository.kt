package bl.repository

import bl.entities.Comment
import bl.repositories.ICommentRepository

class MockCommentRepository : ICommentRepository {
    override fun create(obj: Comment) {
        CommentMockData += obj
    }

    override fun read(id: ULong): Comment = CommentMockData[id.toInt()]

    override fun update(obj: Comment) {
        CommentMockData[obj.id.toInt()] = obj
    }

    override fun delete(id: ULong) {
        CommentMockData[id.toInt()].text = "deleted"
    }

    override fun getAll(): List<Comment> = CommentMockData.toList()

    override fun exists(id: ULong): Boolean = CommentMockData.find { x -> x.id == id } != null
}