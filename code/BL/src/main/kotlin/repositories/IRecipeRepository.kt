package bl.repositories

import bl.entities.Comment
import bl.entities.Recipe
import bl.entities.RecipePreview

interface IRecipeRepository : IRepository<Recipe> {

    fun getOwnerID(id: ULong): ULong

    fun addToFavorite(id: ULong, userID: ULong)
    fun deleteFromFavorite(id: ULong, userID: ULong)
    fun isInFavorite(id: ULong, userID: ULong): Boolean

    fun addComment(userId: ULong, recipeId: ULong, comment: Comment)

    fun addToPublishQueue(id: ULong)

    fun getPublishQueue(): List<RecipePreview>

    fun getSortedByDate(): List<RecipePreview>

    fun isPublished(id: ULong): Boolean

    fun approvePublication(id: ULong)

    fun cancelPublication(id: ULong)

    fun isInPublishQueue(id: ULong): Boolean
}