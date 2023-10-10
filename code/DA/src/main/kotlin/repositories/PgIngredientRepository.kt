package da.repositories

import bl.entities.Ingredient
import bl.repositories.IIngredientRepository
import da.dao.IngredientTable
import da.dao.Ingredients
import da.dao.toEntity
import da.exeption.NotFoundException
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.sql.Connection

class PgIngredientRepository : IIngredientRepository {
    private val logger = LoggerFactory.getLogger("mainLogger")

    override fun create(obj: Ingredient): Int {
        logger.trace("{} called with parameters {}", ::create.name, obj)

        val dao = transaction {
            IngredientTable.new {
                name = obj.name
                type = obj.type.value
                nutritionalValue = obj.nutritionalValue.toInt()
            }
        }

        return dao.id.value
    }

    override fun read(id: Int): Ingredient {
        logger.trace("{} called with parameters {}", ::read.name, id)

        return transaction {
            IngredientTable.findById(id)?.toEntity() ?: throw NotFoundException("Ingredient with id = $id not found")
        }
    }

    override fun update(obj: Ingredient) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        transaction {
            val dao = IngredientTable.findById(obj.id.toInt())
                ?: throw NotFoundException("Ingredient with id = ${obj.id} not found")
            dao.name = obj.name
            dao.type = obj.type.value
            dao.nutritionalValue = obj.nutritionalValue.toInt()
        }
    }

    override fun delete(id: Int) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        transaction {
            val obj = IngredientTable.findById(id.toInt())
                ?: throw NotFoundException("Ingredient with id = $id not found")
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

    override fun isNameNotExist(name: String): Boolean {
        logger.trace("{} called with parameters {}", ::isNameNotExist.name, name)

        return transaction { IngredientTable.find { Ingredients.name eq name }.firstOrNull() } == null
    }

    override fun findByName(name: String): List<Ingredient> {
        logger.trace("{} called with parameters {}", ::findByName.name, name)

        return transaction {
            IngredientTable.find { Ingredients.name like "%$name%" }.map { it.toEntity() }
        }
    }
}