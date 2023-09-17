package bl.repositories

import bl.entities.RecipePreview
import bl.entities.User

interface IUserRepository : IRepository<User> {
    fun isLoginNotExist(login: String): Boolean
    fun changeRole(id: ULong, isAdmin: Boolean)
    fun isAdmin(id: ULong): Boolean
    fun getByLogin(login: String): User?
    fun getSavedRecipes(userID: ULong): List<RecipePreview>
    fun getOwnRecipes(userID: ULong): List<RecipePreview>
    fun getPublishedRecipes(userID: ULong): List<RecipePreview>
}