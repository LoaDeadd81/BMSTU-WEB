package da.repositories

import bl.entities.Comment
import bl.repositories.ICommentRepository
import da.dao.CommentTable
import da.dao.Recipes
import da.dao.Users
import da.dao.toEntity
import da.exeption.NotFoundException
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class PgCommentRepository : ICommentRepository {
    private val logger = LoggerFactory.getLogger("mainLogger")

    override fun create(userID: Int, text: String, recipeID: Int): Int {
        logger.trace("{} called with parameters {}, {}, {}", ::create.name, userID, text, recipeID)

        val dao = transaction {
            CommentTable.new {
                date = LocalDateTime.now()
                this.text = text
                autorId = EntityID(userID, Users)
                this.recipeId = EntityID(recipeID, Recipes)
            }
        }

        return dao.id.value
    }

    override fun update(obj: Comment) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        transaction {
            val dao = CommentTable.findById(obj.id)
                ?: throw NotFoundException("Comment with id = ${obj.id} not found")
            dao.date = obj.date
            dao.text = obj.text
            dao.autorId = EntityID(obj.autor.id, Users)
        }
    }

    override fun delete(id: Int) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        transaction {
            CommentTable.findById(id)?.delete()
                ?: throw NotFoundException("Comment with id = $id not found")
        }
    }

    override fun updateText(id: Int, text: String): Comment {
        logger.trace("{} called with parameters {}, {}", ::updateText.name, id, text)

        transaction {
            val dao = CommentTable.findById(id)
                ?: throw NotFoundException("Comment with id = $id not found")
            dao.text = text
        }

        return transaction {
            CommentTable.findById(id)?.toEntity()
                ?: throw NotFoundException("Comment with id = $id not found")
        }
    }

    override fun getOwnerID(id: Int): Int {
        return transaction {
            CommentTable.findById(id)?.autor?.id?.value
                ?: throw NotFoundException("Comment with id = $id not found")
        }
    }
}