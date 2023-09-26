package da.repositories

import bl.entities.RecipePreview
import bl.entities.User
import bl.repositories.IUserRepository
import da.dao.*
import da.exeption.NotFoundInDBException
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class PgUserRepository : IUserRepository {
    private val logger = LoggerFactory.getLogger("mainLogger")

    override fun create(obj: User) {
        logger.trace("{} called with parameters {}", ::create.name, obj)

        transaction {
            UserTable.new {
                login = obj.login
                password = obj.password
                isAdmin = obj.isAdmin
            }
        }
    }

    override fun read(id: ULong): User {
        logger.trace("{} called with parameters {}", ::read.name, id)

        return transaction {
            UserTable.findById(id.toInt())?.toEntity()
                ?: throw NotFoundInDBException("Comment with id = $id not found")
        }
    }

    override fun update(obj: User) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        transaction {
            val dao =
                UserTable.findById(obj.id.toInt()) ?: throw NotFoundInDBException("User with id = ${obj.id} not found")
            dao.login = obj.login
            dao.password = obj.password
            dao.isAdmin = obj.isAdmin
        }
    }

    override fun delete(id: ULong) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        transaction {
            val obj = UserTable.findById(id.toInt()) ?: throw NotFoundInDBException("User with id = $id not found")

            obj.comments.map { x -> x.delete() }
            logger.debug("Comments deleted")

            obj.recipes.map { x ->
                x.stages.map { y ->
                    y.list.map { z -> z.delete() }
                    y.delete()
                }
                x.comments.map { y -> y.delete() }
                x.publishRecipes.map { y -> y.delete() }
                x.savedRecipes.map { y -> y.delete() }
                x.delete()
            }
            logger.debug("Recipes deleted")

            obj.savedRecipesList.map { y -> y.delete() }
            logger.debug("Saved recipes deleted")

            obj.delete()
            logger.debug("User deleted")
        }
    }

    override fun getAll(): List<User> {
        logger.trace("{} called", ::getAll.name)

        return transaction {
            UserTable.all().map { it.toEntity() }
        }
    }

    override fun exists(id: ULong): Boolean {
        logger.trace("{} called with parameters {}", ::exists.name, id)

        return transaction { UserTable.findById(id.toInt()) != null }
    }

    override fun isLoginNotExist(login: String): Boolean {
        logger.trace("{} called with parameters {}", ::isLoginNotExist.name, login)

        return transaction { UserTable.find { Users.login eq login }.firstOrNull() } == null
    }

    override fun isAdmin(id: ULong): Boolean {
        logger.trace("{} called with parameters {}", ::isAdmin.name, id)

        return transaction {
            UserTable.findById(id.toInt())?.isAdmin ?: throw NotFoundInDBException("User with id = $id not found")
        }
    }

    override fun getByLogin(login: String): User? {
        logger.trace("{} called with parameters {}", ::getByLogin.name, login)

        return transaction {
            UserTable.find { Users.login eq login }.firstOrNull()?.toEntity()
        }
    }

    override fun getSavedRecipes(userID: ULong): List<RecipePreview> {
        logger.trace("{} called with parameters {}", ::getSavedRecipes.name, userID)

        return transaction {
            UserTable.findById(userID.toInt())?.savedRecipesPreview?.map { it.toEntity() }
                ?: throw NotFoundInDBException("User with id = $id not found")
        }
    }

    override fun getOwnRecipes(userID: ULong): List<RecipePreview> {
        logger.trace("{} called with parameters {}", ::getOwnRecipes.name, userID)

        return transaction {
            UserTable.findById(userID.toInt())?.recipesPreview?.map { it.toEntity() }
                ?: throw NotFoundInDBException("User with id = $id not found")
        }
    }

    override fun getPublishedRecipes(userID: ULong): List<RecipePreview> {
        logger.trace("{} called with parameters {}", ::getPublishedRecipes.name, userID)

        return transaction {
            val query = Recipes.innerJoin(Users).slice(Recipes.columns)
                .select((Users.id eq userID.toInt()) and (Recipes.published eq true))
            RecipePreviewTable.wrapRows(query).map { it.toEntity() }
        }
    }

}
