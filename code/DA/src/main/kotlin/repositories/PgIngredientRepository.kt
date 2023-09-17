package da.repositories

import bl.entities.Ingredient
import bl.repositories.IIngredientRepository
import da.dao.IngredientTable
import da.dao.Ingredients
import da.dao.toEntity
import da.exeption.NotFoundInDBException
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class PgIngredientRepository : IIngredientRepository {
    private val logger = LoggerFactory.getLogger("mainLogger")

    override fun create(obj: Ingredient) {
        logger.trace("{} called with parameters {}", ::create.name, obj)

        transaction {
            IngredientTable.new {
                name = obj.name
                type = obj.type
                nutritionalValue = obj.nutritionalValue.toInt()
            }
        }
    }

    override fun read(id: ULong): Ingredient {
        logger.trace("{} called with parameters {}", ::read.name, id)

        return transaction {
            IngredientTable.findById(id.toInt())?.toEntity()
                ?: throw NotFoundInDBException("Ingredient with id = $id not found")

        }
    }

    override fun update(obj: Ingredient) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        transaction {
            val dao = IngredientTable.findById(obj.id.toInt())
                ?: throw NotFoundInDBException("Ingredient with id = ${obj.id} not found")
            dao.name = obj.name
            dao.type = obj.type
            dao.nutritionalValue = obj.nutritionalValue.toInt()
        }
    }

    override fun delete(id: ULong) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        transaction {
            val obj = IngredientTable.findById(id.toInt())
                ?: throw NotFoundInDBException("Ingredient with id = $id not found")
            obj.list.map { it.delete() }
            obj.delete()
        }
    }

    override fun getAll(): List<Ingredient> {
        logger.trace("{} called", ::getAll.name)

        return transaction {
            IngredientTable.all().map { it.toEntity() }
        }
    }

    override fun exists(id: ULong): Boolean {
        logger.trace("{} called with parameters {}", ::exists.name, id)

        return transaction { IngredientTable.findById(id.toInt()) != null }
    }

    override fun isNameNotExist(name: String): Boolean {
        logger.trace("{} called with parameters {}", ::isNameNotExist.name, name)

        return transaction { IngredientTable.find { Ingredients.name eq name }.firstOrNull() } == null
    }

    override fun findByName(name: String): Ingredient {
        logger.trace("{} called with parameters {}", ::findByName.name, name)

        return transaction {
            IngredientTable.find { Ingredients.name eq name }.firstOrNull()?.toEntity()
                ?: throw NotFoundInDBException("Ingredient with name = $name not found")
        }
    }
}