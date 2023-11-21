package da.repositories

import bl.entities.RecipePreview
import bl.entities.RecipeState
import bl.entities.User
import bl.repositories.IUserRepository
import da.dao.*
import da.exeption.NotFoundException
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class PgUserRepository : IUserRepository {
    private val logger = LoggerFactory.getLogger("mainLogger")

    override fun create(login: String, password: String): Int {
        logger.trace("{} called with parameters {}, {}", ::create.name, login, password)

        val dao = transaction {
            UserTable.new {
                this.login = login
                this.password = password
            }
        }

        return dao.id.value
    }

    override fun read(id: Int): User {
        logger.trace("{} called with parameters {}", ::read.name, id)

        return transaction {
            UserTable.findById(id)?.toEntity()
                ?: throw NotFoundException("Comment with id = $id not found")
        }
    }

    override fun update(obj: User) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        transaction {
            val dao =
                UserTable.findById(obj.id) ?: throw NotFoundException("User with id = ${obj.id} not found")
            dao.login = obj.login
            dao.password = obj.password
            dao.isAdmin = obj.isAdmin
        }
    }

    override fun delete(id: Int) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        transaction {
            val obj = UserTable.findById(id) ?: throw NotFoundException("User with id = $id not found")

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

    override fun updateCredentials(id: Int, login: String, password: String): User {
        logger.trace("{} called with parameters {}, {}, {}", ::updateCredentials.name, id, login, password)

        transaction {
            val dao =
                UserTable.findById(id) ?: throw NotFoundException("User with id = $id not found")
            dao.login = login
            dao.password = password
        }

        val dao = transaction { UserTable.findById(id) ?: throw NotFoundException("User with id = $id not found") }

        return dao.toEntity()
    }

    override fun addToFavorite(id: Int, recipeId: Int) {
        logger.trace("{} called with parameters {}, {}", ::addToFavorite.name, id, recipeId)

        transaction {
            SavedRecipeTable.new {
                this.userId = EntityID(id, Users)
                this.recipeId = EntityID(recipeId, Recipes)
            }
        }
    }

    override fun deleteFromFavorite(id: Int, recipeId: Int) {
        logger.trace("{} called with parameters {}, {}", ::deleteFromFavorite.name, id, recipeId)

        transaction {
            SavedRecipes.deleteWhere {
                user eq EntityID(id, Users) and (recipe eq EntityID(
                    recipeId, Recipes
                ))
            }
        }
    }

    override fun isInFavorite(id: Int, recipeId: Int): Boolean {
        logger.trace("{} called with parameters {}, {}", ::isInFavorite.name, id, recipeId)

        return transaction {
            SavedRecipeTable.find {
                SavedRecipes.user eq EntityID(id, Users) and (SavedRecipes.recipe eq EntityID(
                    recipeId, Recipes
                ))
            }.firstOrNull() != null
        }
    }

    override fun isLoginNotExist(login: String): Boolean {
        logger.trace("{} called with parameters {}", ::isLoginNotExist.name, login)

        return transaction { UserTable.find { Users.login eq login }.firstOrNull() } == null
    }

    override fun isAdmin(id: Int): Boolean {
        logger.trace("{} called with parameters {}", ::isAdmin.name, id)

        return transaction {
            UserTable.findById(id)?.isAdmin ?: throw NotFoundException("User with id = $id not found")
        }
    }

    override fun getByLogin(login: String): User {
        logger.trace("{} called with parameters {}", ::getByLogin.name, login)

        return transaction {
            UserTable.find { Users.login eq login }.firstOrNull()?.toEntity()
                ?: throw NotFoundException("User with login = $login not found")
        }
    }

    override fun getPublishedRecipes(userID: Int): List<RecipePreview> { //id + published
        logger.trace("{} called with parameters {}", ::getPublishedRecipes.name, userID)

        return transaction {
            val query = Recipes.innerJoin(Users).slice(Recipes.columns)
                .select((Users.id eq userID) and (Recipes.state eq RecipeState.PUBLISHED.value))
            RecipePreviewTable.wrapRows(query).map { it.toEntity() }
        }
    }
}
