package bl.repositories

import bl.entities.*

interface IRecipeRepository {
    fun create(obj: Recipe): Int
    fun read(id: Int): Recipe
    fun update(obj: Recipe)
    fun delete(id: Int)
    fun getAll(): List<RecipePreview>
    fun updateInfo(obj: Recipe): Recipe
    fun updateStages(id: Int, list: List<Stage>): Recipe
    fun getOwnerID(id: Int): Int //todo
    fun getPublishQueue(): List<RecipePreview>
}