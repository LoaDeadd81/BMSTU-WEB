package bl

import bl.entities.*
import bl.managers.*
import bl.repositories.factory.IRepositoryFactory

class Facade(repositoryFactory: IRepositoryFactory) {
    init {
        CommentManager.registerRepository(repositoryFactory.createCommentRepository())
        IngredientManager.registerRepository(repositoryFactory.createIngredientRepository())
        RecipeManager.registerRepository(repositoryFactory.createRecipeRepository())
        UserManager.registerRepository(repositoryFactory.createUserRepository())
        FeedService.registerRepository(repositoryFactory.createRecipeRepository())
    }

    //Account
    fun register(login: String, password: String) {
        AccountService.register(login, password)
    }

    fun logIn(login: String, password: String): User = AccountService.logIN(login, password) //todo

    //User
    fun getAllUsers(): List<User> = UserManager.getAll()
    fun getUser(id: ULong) = UserManager.read(id)
    fun updateUser(obj: User) {
        UserManager.update(obj)
    }

    fun deleteUser(id: ULong) {
        UserManager.delete(id)
    }

    fun getOwnFeed(id: ULong): List<RecipePreview> = UserManager.getOwnRecipes(id)
    fun getSavedFeed(id: ULong): List<RecipePreview> = UserManager.getSavedRecipes(id)
    fun getUserPublishedRecipes(id: ULong): List<RecipePreview> = UserManager.getPublishedRecipes(id)

    //Recipe
    fun createRecipe(obj: Recipe) {
        RecipeManager.create(obj)
    }

    fun getRecipe(id: ULong): Recipe = RecipeManager.read(id)
    fun updateRecipe(obj: Recipe) {
        RecipeManager.update(obj)
    }

    fun deleteRecipe(id: ULong) {
        RecipeManager.delete(id)
    }

    fun addComment(userID: ULong, text: String, recipeID: ULong) {
        RecipeManager.addComment(userID, text, recipeID)
    }

    fun addToFavorite(userID: ULong, recipeID: ULong) {
        RecipeManager.addToFavorite(recipeID, userID)
    }

    fun deleteFromFavorite(userID: ULong, recipeID: ULong) {
        RecipeManager.deleteFromFavorite(recipeID, userID)
    }

    fun addToPublishQueue(id: ULong) {
        RecipeManager.publish(id)
    }

    fun cancelRecipePublication(id: ULong) {
        RecipeManager.cancelRecipePublication(id)
    }

    fun isInFavorite(userID: ULong, recipeID: ULong): Boolean = RecipeManager.isInFavorite(recipeID, userID)


    //Comment
    fun updateComment(obj: Comment) {
        CommentManager.update(obj)
    }

    fun deleteComment(id: ULong) {
        CommentManager.delete(id)
    }

    //Feed
    fun getFeedSortedByDate(): List<RecipePreview> = FeedService.getSortedByDate()

    //Publish Queue
    fun getPublishQueue(): List<RecipePreview> = RecipeManager.getPublishQueue()
    fun acceptRecipePublication(id: ULong) {
        RecipeManager.approvePublication(id)
    }
    //todo delete

    //Ingredient
    fun createIngredient(obj: Ingredient) {
        IngredientManager.create(obj)
    }

    fun getAllIngredients(): List<Ingredient> = IngredientManager.getAll()
    fun updateIngredient(obj: Ingredient) {
        IngredientManager.update(obj)
    }

    fun findByNameIngredient(name: String) = IngredientManager.findByName(name) //todo
    fun deleteIngredient(id: ULong) {
        IngredientManager.delete(id)
    }


}