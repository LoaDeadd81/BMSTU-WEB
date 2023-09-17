package bl.repository

import bl.entities.RecipePreview
import bl.entities.User
import bl.entities.toRecipePreview
import bl.repositories.IUserRepository

class MockUserRepository : IUserRepository {
    override fun create(obj: User) {
        UserMockData += obj
    }

    override fun read(id: ULong): User = UserMockData[id.toInt()]

    override fun update(obj: User) {
        UserMockData[obj.id.toInt()] = obj
    }

    override fun delete(id: ULong) {
        UserMockData[id.toInt()].password = "deleted"
    }

    override fun getAll(): List<User> = UserMockData.toList()

    override fun exists(id: ULong): Boolean = UserMockData.find { x -> x.id == id } != null

    override fun isLoginNotExist(login: String): Boolean = UserMockData.find { x -> x.login == login } == null

    override fun getByLogin(login: String): User? = UserMockData.find { x -> x.login == login }

    override fun getSavedRecipes(userID: ULong): List<RecipePreview> {
        val lst = SavedRecipesMockData.filter { x -> x.first == userID }.map { x -> x.first }
        return RecipeMockData.filter { x -> x.id in lst }.map { it.toRecipePreview() }
    }

    override fun getOwnRecipes(userID: ULong): List<RecipePreview> =
        RecipeMockData.filter { x -> x.owner.id == userID }.map { it.toRecipePreview() }

    override fun changeRole(id: ULong, isAdmin: Boolean) {
        UserMockData[id.toInt()].isAdmin = isAdmin
    }

    override fun isAdmin(id: ULong): Boolean = UserMockData[id.toInt()].isAdmin

    override fun getPublishedRecipes(userID: ULong): List<RecipePreview> =
        RecipeMockData.filter { x -> x.owner.id == userID && x.published }.map { it.toRecipePreview() }
}