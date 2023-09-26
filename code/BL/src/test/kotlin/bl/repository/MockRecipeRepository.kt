package bl.repository

import bl.entities.Comment
import bl.entities.Recipe
import bl.entities.RecipePreview
import bl.entities.toRecipePreview
import bl.repositories.IRecipeRepository

class MockRecipeRepository : IRecipeRepository {
    override fun getOwnerID(id: ULong): ULong = RecipeMockData[id.toInt()].owner.id
    override fun addToFavorite(id: ULong, userID: ULong) {
        SavedRecipesMockData += Pair(userID, id)
    }

    override fun deleteFromFavorite(id: ULong, userID: ULong) {

    }

    override fun isInFavorite(id: ULong, userID: ULong): Boolean =
        SavedRecipesMockData.find { x -> x.first == id && x.second == userID } != null


    override fun addComment(userId: ULong, recipeId: ULong, comment: Comment) {
        CommentMockData += comment
        RecipeMockData[recipeId.toInt()].comments += comment
    }

    override fun addToPublishQueue(id: ULong) {
        PublishQueue += id
    }

    override fun getPublishQueue(): List<RecipePreview> =
        RecipeMockData.filter { it.published }.map { it.toRecipePreview() }

    override fun getSortedByDate(): List<RecipePreview> =
        RecipeMockData.sortedBy { it.date }.map { it.toRecipePreview() }


    override fun create(obj: Recipe) {
        RecipeMockData += obj
    }

    override fun read(id: ULong): Recipe = RecipeMockData[id.toInt()]

    override fun update(obj: Recipe) {
        RecipeMockData[obj.id.toInt()] = obj
    }

    override fun delete(id: ULong) {
        RecipeMockData[id.toInt()].name = "deleted"
    }

    override fun getAll(): List<Recipe> = RecipeMockData.toList()

    override fun exists(id: ULong): Boolean = RecipeMockData.find { x -> x.id == id } != null

    override fun isPublished(id: ULong): Boolean = RecipeMockData[id.toInt()].published

    override fun approvePublication(id: ULong) {
        RecipeMockData[id.toInt()].published = true
    }

    override fun isInPublishQueue(id: ULong): Boolean = PublishQueue.find { x -> x == id } != null

    override fun cancelPublication(id: ULong) {
        val lst = PublishQueue.toMutableList()
        lst.removeAt(lst.indexOf(id))
        PublishQueue = lst.toTypedArray()
    }

}