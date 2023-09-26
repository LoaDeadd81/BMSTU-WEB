package da.repositories

import bl.entities.Comment
import bl.repositories.ICommentRepository
import da.dao.CommentTable
import da.dao.Users
import da.exeption.NotFoundInDBException
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class PgCommentRepository : ICommentRepository {
    private val logger = LoggerFactory.getLogger("mainLogger")

    override fun create(obj: Comment) {
//        logger.trace("{} called with parameters {}", ::create.name, obj)
//
//        transaction {
//            CommentTable.new {
//                date = obj.date
//                text = obj.text
//                autorId = EntityID(obj.autor.id.toInt(), Users)
//            }
//        }
    }

    override fun read(id: ULong): Comment {
//        logger.trace("{} called with parameters {}", ::read.name, id)
//
//        return transaction {
//            CommentTable.findById(id.toInt())?.toEntity()
//                ?: throw NotFoundInDBException("Comment with id = $id not found")
//        }
        TODO()
    }

    override fun update(obj: Comment) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        transaction {
            val dao = CommentTable.findById(obj.id.toInt())
                ?: throw NotFoundInDBException("Comment with id = ${obj.id} not found")
            dao.date = obj.date
            dao.text = obj.text
            dao.autorId = EntityID(obj.autor.id.toInt(), Users)
        }
    }

    override fun delete(id: ULong) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        transaction {
            CommentTable.findById(id.toInt())?.delete()
                ?: throw NotFoundInDBException("Comment with id = $id not found")
        }
    }

    override fun getAll(): List<Comment> {
//        logger.trace("{} called", ::getAll.name)
//
//        return transaction {
//            CommentTable.all().map { it.toEntity() }
//        }
        TODO()
    }

    override fun exists(id: ULong): Boolean {
        logger.trace("{} called with parameters {}", ::exists.name, id)

        return transaction { CommentTable.findById(id.toInt()) != null }
    }

}