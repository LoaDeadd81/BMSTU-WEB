package da.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object SavedRecipes : IntIdTable("saved_recipes") {
    val user = reference("userid", Users, onDelete = ReferenceOption.CASCADE)
    val recipe = reference("recipeid", Recipes, onDelete = ReferenceOption.CASCADE)
}

class SavedRecipeTable(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SavedRecipeTable>(SavedRecipes)

    var userId by SavedRecipes.user
    var recipeId by SavedRecipes.recipe

    var user by UserTable referencedOn SavedRecipes.user
    var recipe by RecipeTable referencedOn SavedRecipes.recipe
}