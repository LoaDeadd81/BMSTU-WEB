package da.repositories

import bl.entities.*
import bl.repositories.IRecipeRepository
import da.dao.*
import da.exeption.NotFoundInDBException
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class PgRecipeRepository : IRecipeRepository {
    private val logger = LoggerFactory.getLogger("mainLogger")

    override fun create(obj: Recipe) {
        logger.trace("{} called with parameters {}", ::create.name, obj)

        val tmp = transaction {
            RecipeTable.new {
                name = obj.name
                description = obj.description
                time = obj.time.toInt()
                servingsNum = obj.servingsNum.toInt()
                protein = obj.pfc.protein
                fat = obj.pfc.fat
                carbon = obj.pfc.carbon
                date = obj.date
                published = obj.published
                ownerId = EntityID(obj.owner.id.toInt(), Users)
            }
        }

        logger.debug("Recipe created")

        val tmpId = tmp.id.value.toULong()
        for (it in obj.stages) {
            createStage(it, tmpId)
        }

        logger.debug("Recipe creation finished")
    }

    private fun createStage(obj: Stage, recipeId: ULong) {
        logger.trace("{} called with parameters {}, {}", ::createStage.name, obj, recipeId)

        val dao = transaction {
            StageTable.new {
                time = obj.time.toInt()
                description = obj.description
                this.recipeId = EntityID(recipeId.toInt(), Users)
            }
        }

        logger.debug("Stage created")

        transaction {
            for (it in obj.ingredients) {
                IngredientListTable.new {
                    amount = it.amount.toInt()
                    processingType = it.processingType
                    ingredientId = EntityID(it.id.toInt(), Ingredients)
                    stageId = EntityID(dao.id.value, Stages)
                }
            }
        }

        logger.debug("Ingredients added")
    }

    override fun read(id: ULong): Recipe {
        logger.trace("{} called with parameters {}", ::read.name, id)

        val dao = transaction {
            RecipeTable.findById(id.toInt()) ?: throw NotFoundInDBException("Recipe with id = $id not found")
        }
        val res = transaction { dao.toEntity() }

        logger.debug("Recipe readed {}", res)

        transaction {
            res.stages = dao.stages.map {
                val tmpRes = it.toEntity()
                tmpRes.ingredients = it.findIngredients()
                tmpRes
            }
        }

        logger.debug("Stages readed {}", res.stages)

        transaction {
            val recipeIngredients = mutableListOf<IngredientInStage>()
            for (stage in res.stages) {
                for (ingredient in stage.ingredients) {
                    val tmp = recipeIngredients.find { it.id == ingredient.id }
                    if (tmp == null) {
                        recipeIngredients += ingredient.copy()
                    } else tmp.amount += ingredient.amount
                }
            }
            res.ingredients = recipeIngredients
        }

        logger.debug("Ingredients readed {}", res.stages)

        return res
    }

    override fun update(obj: Recipe) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        transaction {
            val dao = RecipeTable.findById(obj.id.toInt())
                ?: throw NotFoundInDBException("Recipe with id = ${obj.id} not found")
            dao.name = obj.name
            dao.description = obj.description
            dao.time = obj.time.toInt()
            dao.servingsNum = obj.servingsNum.toInt()
            dao.protein = obj.pfc.protein
            dao.fat = obj.pfc.fat
            dao.carbon = obj.pfc.carbon
            dao.date = obj.date
            dao.published = obj.published
            dao.ownerId = EntityID(obj.owner.id.toInt(), Users)

            logger.debug("Recipe updated")

            dao.stages.map {
                it.list.map { x -> x.delete() }
                it.delete()
            }

            for (it in obj.stages) createStage(it, obj.id)

            logger.debug("Stages updated")
        }
    }

//    private fun updateStage(obj: Stage, recipeId: ULong) {
//        val dao = transaction {
//            StageTable.new {
//                time = obj.time.toInt()
//                description = obj.description
//                this.recipeId = EntityID(recipeId.toInt(), Users)
//            }
//        }
//
//        transaction {
//            for (it in obj.ingredients) {
//                IngredientListTable.new {
//                    amount = it.amount.toInt()
//                    processingType = it.processingType
//                    ingredientId = EntityID(it.id.toInt(), Ingredients)
//                    stageId = EntityID(obj.id.toInt(), Stages)
//                }
//            }
//        }
//    }

    override fun delete(id: ULong) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        transaction {
            val dao = RecipeTable.findById(id.toInt()) ?: throw NotFoundInDBException("Recipe with id = $id not found")
            dao.stages.map { y ->
                y.list.map { z -> z.delete() }
                y.delete()
            }
            logger.debug("Stages deleted")

            dao.comments.map { y -> y.delete() }
            logger.debug("Comments deleted")

            dao.publishRecipes.map { y -> y.delete() }
            logger.debug("Recipe deleted from published list")

            dao.savedRecipes.map { y -> y.delete() }
            logger.debug("Recipe deleted from saved list")

            dao.delete()
            logger.debug("Recipe deleted")

            //todo delete from publish qeueu
        }
    }

    override fun getAll(): List<Recipe> {
        logger.trace("{} called", ::getAll.name)

        return transaction {
            RecipeTable.all().map { it.toEntity() }
        }
    }

    override fun exists(id: ULong): Boolean {
        logger.trace("{} called with parameters {}", ::exists.name, id)

        return transaction { RecipeTable.findById(id.toInt()) != null }
    }

    override fun getAllPreview(): List<RecipePreview> {
        logger.trace("{} called", ::getAllPreview.name)

        return transaction {
            RecipePreviewTable.all().map { it.toEntity() }
        }
    }

    override fun getOwnerID(id: ULong): ULong {
        logger.trace("{} called with parameters {}", ::getOwnerID.name, id)

        return transaction {
            RecipeTable.findById(id.toInt())?.ownerId?.value?.toULong()
                ?: throw NotFoundInDBException("Recipe with owner id = $id not found")
        }
    }


    override fun addToFavorite(id: ULong, userID: ULong) {
        logger.trace("{} called with parameters {}, {}", ::addToFavorite.name, id, userID)

        transaction {
            SavedRecipeTable.new {
                userId = EntityID(userID.toInt(), Users)
                recipeId = EntityID(id.toInt(), Recipes)
            }
        }
    }

    override fun isInFavorite(id: ULong, userID: ULong): Boolean {
        logger.trace("{} called with parameters {}, {}", ::isInFavorite.name, id, userID)

        return transaction {
            SavedRecipeTable.find {
                SavedRecipes.user eq EntityID(userID.toInt(), Users) and (SavedRecipes.recipe eq EntityID(
                    id.toInt(), Recipes
                ))
            }.firstOrNull() != null
        }
    }

    override fun deleteFromFavorite(id: ULong, userID: ULong) {
        logger.trace("{} called with parameters {}, {}", ::deleteFromFavorite.name, id, userID)

        transaction {
            SavedRecipes.deleteWhere {
                user eq EntityID(userID.toInt(), Users) and (recipe eq EntityID(
                    id.toInt(), Recipes
                ))
            }
        }
    }

    override fun addComment(userId: ULong, recipeId: ULong, comment: Comment) {
        logger.trace("{} called with parameters {}, {}, {}", ::addComment.name, userId, recipeId, comment)

        transaction {
            CommentTable.new {
                date = comment.date
                text = comment.text
                autorId = EntityID(userId.toInt(), Users)
                this.recipeId = EntityID(recipeId.toInt(), Recipes)
            }
        }
    }

    override fun addToPublishQueue(id: ULong) {
        logger.trace("{} called with parameters {}", ::addToPublishQueue.name, id)

        transaction {
            PublishQueueTable.new {
                recipeId = EntityID(id.toInt(), Recipes)
            }
        }
    }

    override fun getPublishQueue(): List<RecipePreview> {
        logger.trace("{} called", ::getPublishQueue.name)

        return transaction {
            val query = PublishQueue.innerJoin(Recipes).slice(Recipes.columns).selectAll()
            RecipePreviewTable.wrapRows(query).map { it.toEntity() }
        }
    }

    override fun getSortedByDate(): List<RecipePreview> {
        logger.trace("{} called", ::getSortedByDate.name)

        return transaction {
//        val query = Recipes.selectAll().orderBy(Recipes.date to SortOrder.DESC)
            val query = Recipes.select { Recipes.published eq true }.orderBy(Recipes.date to SortOrder.DESC)
            RecipePreviewTable.wrapRows(query).map { it.toEntity() }
        }
    }

    override fun isPublished(id: ULong): Boolean {
        logger.trace("{} called with parameters {}", ::isPublished.name, id)

        return transaction {
            RecipeTable.findById(id.toInt())?.published ?: throw NotFoundInDBException("Recipe with id = $id not found")
        }
    }

    override fun approvePublication(id: ULong) {
        logger.trace("{} called with parameters {}", ::approvePublication.name, id)

        transaction {
            val recipe = RecipeTable.findById(id.toInt())
            if (recipe != null) recipe.published = true
            else throw NotFoundInDBException("Recipe with id = $id not found")

            PublishQueueTable.find { PublishQueue.recipe eq EntityID(id.toInt(), Recipes) }.firstOrNull()?.delete()
                ?: throw NotFoundInDBException("Recipe with id = $id not found in publication queue")
        }
    }

    override fun cancelPublication(id: ULong) {
        logger.trace("{} called with parameters {}", ::cancelPublication.name, id)

        transaction {
            PublishQueueTable.find { PublishQueue.recipe eq EntityID(id.toInt(), Recipes) }.firstOrNull()?.delete()
                ?: throw NotFoundInDBException("Recipe with id = $id not found in publication queue")
        }
    }

    override fun isInPublishQueue(id: ULong): Boolean {
        logger.trace("{} called with parameters {}", ::isInPublishQueue.name, id)

        return transaction {
            PublishQueueTable.find { PublishQueue.recipe eq EntityID(id.toInt(), Recipes) }.firstOrNull() != null
        }
    }

    override fun addStage(recipeId: ULong, stage: Stage) {
        logger.trace("{} called with parameters {}, {}", ::addStage.name, recipeId, stage)

        createStage(stage, recipeId)
    }
}