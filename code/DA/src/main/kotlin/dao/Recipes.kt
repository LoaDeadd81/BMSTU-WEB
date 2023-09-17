package da.dao

import bl.entities.PFC
import bl.entities.Recipe
import bl.entities.RecipePreview
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Recipes : IntIdTable("recipe") {
    val name = text("name")
    val description = text("description")
    val time = integer("time").check { it.greaterEq(0) }
    val servingsNum = integer("servings_num").autoIncrement()
    val protein = integer("protein").check { it.greaterEq(0) }
    val fat = integer("fat").check { it.greaterEq(0) }
    val carbon = integer("carbon").check { it.greaterEq(0) }
    val date = datetime("date")
    val published = bool("published").default(false)
    val owner = reference("ownerid", Users)
}

class RecipeTable(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RecipeTable>(Recipes)

    var name by Recipes.name
    var description by Recipes.description
    var time by Recipes.time
    var servingsNum by Recipes.servingsNum
    var protein by Recipes.protein
    var fat by Recipes.fat
    var carbon by Recipes.carbon
    var date by Recipes.date
    var published by Recipes.published
    var ownerId by Recipes.owner

    var owner by UserTable referencedOn Recipes.owner

    val comments by CommentTable referrersOn Comments.recipe
    val stages by StageTable referrersOn Stages.recipe
    val publishRecipes by PublishQueueTable referrersOn PublishQueue.recipe
    val savedRecipes by SavedRecipeTable referrersOn SavedRecipes.recipe

//    var savedUsers by UserTable via SavedRecipes
}

class RecipePreviewTable(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RecipePreviewTable>(Recipes)

    var name by Recipes.name
    var description by Recipes.description
    var time by Recipes.time
    var servingsNum by Recipes.servingsNum

//    var savedUsers by UserTable via SavedRecipes
}

fun RecipeTable.toEntity(): Recipe = Recipe(
    id = this.id.value.toULong(),
    name = this.name,
    description = this.description,
    time = this.time.toULong(),
    servingsNum = this.servingsNum.toUInt(),
    pfc = PFC(this.protein, this.fat, this.carbon),
    date = this.date,
    published = this.published,
    stages = listOf(),
    owner = this.owner.toEntity(),
    comments = this.comments.toList().map { it.toEntity() },
    ingredients = listOf()
)

fun RecipePreviewTable.toEntity(): RecipePreview = RecipePreview(
    id = this.id.value.toULong(),
    name = this.name,
    description = this.description,
    time = this.time.toULong(),
    servingsNum = this.servingsNum.toUInt()
)