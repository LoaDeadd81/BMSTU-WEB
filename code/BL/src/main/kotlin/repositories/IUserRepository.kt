package bl.repositories

import bl.entities.RecipePreview
import bl.entities.User

interface IUserRepository {
    fun create(login: String, password: String): Int
    fun read(id: Int): User
    fun update(obj: User)
    fun delete(id: Int)
    fun getAll(): List<User>
    fun updateCredentials(id: Int, login: String, password: String): User
    fun addToFavorite(id: Int, recipeId: Int)
    fun deleteFromFavorite(id: Int, recipeId: Int)
    fun isInFavorite(id: Int, recipeId: Int): Boolean
    fun isLoginNotExist(login: String): Boolean
    fun isAdmin(id: Int): Boolean
    fun getSavedRecipes(userID: Int): List<RecipePreview>
    fun getOwnRecipes(userID: Int): List<RecipePreview>
    fun getPublishedRecipes(userID: Int): List<RecipePreview>
    fun getByLogin(login: String): User
}