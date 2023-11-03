package da.repositories

import bl.entities.*
import bl.repositories.IRecipeRepository
import da.dao.*
import da.exeption.NotFoundException
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class PgRecipeRepository : IRecipeRepository {
    private val logger = LoggerFactory.getLogger("mainLogger")

    override fun create(obj: Recipe): Int {
        logger.trace("{} called with parameters {}", ::create.name, obj)

        val tmp = transaction {
            RecipeTable.new {
                name = obj.name
                description = obj.description
                time = obj.time
                servingsNum = obj.servingsNum
                protein = obj.pfc.protein
                fat = obj.pfc.fat
                carbon = obj.pfc.carbon
                date = LocalDateTime.now()
                ownerId = EntityID(obj.owner.id, Users)
                state = RecipeState.CLOSE.value
            }
        }

        logger.debug("Recipe created")

        val tmpId = tmp.id.value
        var i = 1
        for (it in obj.stages) {
            createStage(it, tmpId, i)
            i++
        }

        logger.debug("Recipe creation finished")

        return tmpId
    }

    private fun createStage(obj: Stage, recipeId: Int, i: Int) {
        logger.trace("{} called with parameters {}, {}", ::createStage.name, obj, recipeId)

        val dao = transaction {
            StageTable.new {
                time = obj.time
                description = obj.description
                this.recipeId = EntityID(recipeId, Users)
                orderNum = i
            }
        }

        logger.debug("Stage created")

        transaction {
            for (it in obj.ingredients) {
                IngredientListTable.new {
                    amount = it.amount
                    processingType = it.processingType.value
                    ingredientId = EntityID(it.id, Ingredients)
                    stageId = EntityID(dao.id.value, Stages)
                }
            }
        }

        logger.debug("Ingredients added")
    }

    override fun read(id: Int): Recipe {
        logger.trace("{} called with parameters {}", ::read.name, id)

        val dao = transaction {
            RecipeTable.findById(id)?.toEntity() ?: throw NotFoundException("Recipe with id = $id not found")
        }

        return dao
    }

    override fun update(obj: Recipe) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        transaction {
            val dao = RecipeTable.findById(obj.id)
                ?: throw NotFoundException("Recipe with id = ${obj.id} not found")
            dao.name = obj.name
            dao.description = obj.description
            dao.time = obj.time
            dao.servingsNum = obj.servingsNum
            dao.protein = obj.pfc.protein
            dao.fat = obj.pfc.fat
            dao.carbon = obj.pfc.carbon
            dao.date = obj.date
            dao.state = obj.state.value
            dao.ownerId = EntityID(obj.owner.id, Users)

            logger.debug("Recipe updated")

            dao.stages.map {
                it.list.map { x -> x.delete() }
                it.delete()
            }

            var i = 1
            for (it in obj.stages) {
                createStage(it, obj.id, i)
                i++
            }

            logger.debug("Stages updated")
        }
    }

    override fun delete(id: Int) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        transaction {
            val dao = RecipeTable.findById(id) ?: throw NotFoundException("Recipe with id = $id not found")
            dao.stages.map { y ->
                y.list.map { z -> z.delete() }
                y.delete()
            }
            logger.debug("Stages deleted")

            dao.comments.map { y -> y.delete() }
            logger.debug("Comments deleted")

            dao.savedRecipes.map { y -> y.delete() }
            logger.debug("Recipe deleted from saved list")

            dao.delete()
            logger.debug("Recipe deleted")
        }
    }

    override fun getAll(): List<RecipePreview> {
        logger.trace("{} called", ::getAll.name)

        return transaction {
            RecipePreviewTable.find { Recipes.state eq RecipeState.PUBLISHED.value }.sortedByDescending { it.date }
                .map { it.toEntity() }
        }
    }

    override fun updateInfo(obj: Recipe): Recipe {
        logger.trace("{} called with parameters {}", ::updateInfo.name, obj)

        transaction {
            val dao = RecipeTable.findById(obj.id)
                ?: throw NotFoundException("Recipe with id = ${obj.id} not found")
            dao.name = obj.name
            dao.description = obj.description
            dao.time = obj.time
            dao.servingsNum = obj.servingsNum
            dao.protein = obj.pfc.protein
            dao.fat = obj.pfc.fat
            dao.carbon = obj.pfc.carbon
            dao.state = obj.state.value
        }

        val dao = transaction {
            RecipeTable.findById(obj.id)?.toEntity() ?: throw NotFoundException("Recipe with id = ${obj.id} not found")
        }

        return dao
    }

    override fun updateStages(id: Int, list: List<Stage>): Recipe {
        logger.trace("{} called with parameters {}, {}", ::updateStages.name, id, list)

        transaction {
            val dao = RecipeTable.findById(id)
                ?: throw NotFoundException("Recipe with id = $id not found")
            dao.stages.map {
                it.list.map { x -> x.delete() }
                it.delete()
            }

            var i = 1
            for (it in list) {
                createStage(it, id, i)
                i++
            }
        }
        logger.debug("Stages updated")

        val dao = transaction {
            RecipeTable.findById(id)?.toEntity() ?: throw NotFoundException("Recipe with id = $id not found")
        }

        return dao
    }

    override fun getOwnerID(id: Int): Int {
        logger.trace("{} called with parameters {}", ::getOwnerID.name, id)

        return transaction {
            RecipeTable.findById(id)?.ownerId?.value
                ?: throw NotFoundException("Recipe with owner id = $id not found")
        }
    }

    override fun getPublishQueue(): List<RecipePreview> { // ready to publish
        logger.trace("{} called", ::getPublishQueue.name)

        return transaction {
            RecipePreviewTable.find { Recipes.state eq RecipeState.READY_TO_PUBLISH.value }.map { it.toEntity() }
        }
    }

    override fun getSavedRecipes(userID: Int): List<RecipePreview> { // id + saved
        logger.trace("{} called with parameters {}", ::getSavedRecipes.name, userID)

        return transaction {
            UserTable.findById(userID)?.savedRecipesPreview?.map { it.toEntity() }
                ?: throw NotFoundException("User with id = $id not found")
        }
    }

    override fun getOwnRecipes(userID: Int): List<RecipePreview> { // id
        logger.trace("{} called with parameters {}", ::getOwnRecipes.name, userID)

        return transaction {
            UserTable.findById(userID)?.recipesPreview?.map { it.toEntity() }
                ?: throw NotFoundException("User with id = $id not found")
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