package da.repositories

import bl.entities.Stage
import bl.repositories.IStageRepository
import da.dao.*
import da.exeption.NotFoundInDBException
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class PgStageRepository : IStageRepository {
    private val logger = LoggerFactory.getLogger("mainLogger")

    override fun create(obj: Stage) {

    }

    override fun read(id: ULong): Stage {
        logger.trace("{} called with parameters {}", ::read.name, id)

        val dao = transaction {
            StageTable.findById(id.toInt()) ?: throw NotFoundInDBException("Stage with id = $id not found")
        }

        val res = dao.toEntity()
        res.ingredients = dao.findIngredients()

        return res
    }


    override fun update(obj: Stage) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        transaction {
            val dao = StageTable.findById(obj.id.toInt())
                ?: throw NotFoundInDBException("Stage with id = ${obj.id} not found")
            dao.time = obj.time.toInt()
            dao.description = obj.description

            dao.list.map { it.delete() }

            for (it in obj.ingredients) {
                IngredientListTable.new {
                    amount = it.amount.toInt()
                    processingType = it.processingType
                    ingredientId = EntityID(it.id.toInt(), Ingredients)
                    stageId = EntityID(obj.id.toInt(), Stages)
                }
            }
        }
    }

    override fun delete(id: ULong) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        transaction {
            val dao = StageTable.findById(id.toInt()) ?: throw NotFoundInDBException("Stage with id = $id not found")

            dao.list.map { it.delete() }
            dao.delete()
        }
    }

    override fun getAll(): List<Stage> {
        logger.trace("{} called", ::getAll.name)

        return transaction {
            StageTable.all().map { it.toEntity() }
        }
    }

    override fun exists(id: ULong): Boolean {
        logger.trace("{} called with parameters {}", ::exists.name, id)

        return transaction { StageTable.findById(id.toInt()) != null }
    }
}