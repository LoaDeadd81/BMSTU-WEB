package bl.managers

import bl.entities.Comment
import bl.exceptions.AccessDeniedException
import bl.exceptions.NotAuthorizedException
import bl.exceptions.NotExistingCommentException
import bl.exceptions.ValidationCommentException
import bl.repositories.ICommentRepository
import bl.repositories.IRepository
import org.slf4j.LoggerFactory

object CommentManager : ICRUDManager<Comment> {
    private lateinit var repository: ICommentRepository

    private val logger = LoggerFactory.getLogger("mainLogger")

    override fun registerRepository(repository: IRepository<Comment>) {
        this.repository = repository as ICommentRepository
    }

    override fun create(obj: Comment) {
//        logger.trace("{} called with parameters {}", ::create.name, obj)
//
//
//        AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
//        if (!isUniq(obj)) {
//            throw AlreadyExistingCommentException("Comment already exists")
//        }
//        if (!validate(obj)) {
//            throw ValidationCommentException("Comment failed validation")
//        }
//
//        repository.create(obj)
    }

    override fun read(id: ULong): Comment {
//        logger.trace("{} called with parameters {}", ::read.name, id)
//
//        if (!isExist(id)) {
//            throw NotExistingCommentException("Comment not exists")
//        }
//
//        return repository.read(id)
        TODO()
    }

    override fun update(obj: Comment) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId)) {
            throw AccessDeniedException("Access denied")
        }
        if (!isExist(obj.id)) {
            throw NotExistingCommentException("Comment not exists")
        }
        if (!validate(obj)) {
            throw ValidationCommentException("Comment failed validation")
        }

        repository.update(obj)
    }

    override fun delete(id: ULong) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        val uId = AccountService.getCurrentUserId() ?: throw NotAuthorizedException("User not authorized")
        if (!UserManager.isAdmin(uId)) {
            throw AccessDeniedException("Access denied")
        }
        if (!isExist(id)) {
            throw NotExistingCommentException("Comment not exists")
        }

        repository.delete(id)
    }

    override fun getAll(): List<Comment> {
//        logger.trace("{} called", ::getAll.name)
//
//        return repository.getAll()
        TODO()
    }

    override fun isUniq(obj: Comment) = true

    override fun isExist(id: ULong): Boolean {
        logger.trace("{} called with parameters {}", ::isExist.name, id)

        return repository.exists(id)
    }

    override fun validate(obj: Comment): Boolean {
        logger.trace("{} called with parameters {}", ::validate.name, obj)

        return obj.text.isNotEmpty()
    }
}