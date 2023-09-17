package bl

import bl.entities.*
import bl.managers.*
import bl.repositories.factory.IRepositoryFactory

class Facade(repositoryFactory: IRepositoryFactory) {
    init {
        CommentManager.registerRepository(repositoryFactory.createCommentRepository())
        IngredientManager.registerRepository(repositoryFactory.createIngredientRepository())
        RecipeManager.registerRepository(repositoryFactory.createRecipeRepository())
        StageManager.registerRepository(repositoryFactory.createStageRepository())
        UserManager.registerRepository(repositoryFactory.createUserRepository())
        FeedService.registerRepository(repositoryFactory.createRecipeRepository())
    }

    //Comment
    fun updateComment(obj: Comment) {
        CommentManager.update(obj)
    }

    fun deleteComment(id: ULong) {
        CommentManager.delete(id)
    }

    //Ingredient
    fun createIngredient(obj: Ingredient) {
        IngredientManager.create(obj)
    }

    fun updateIngredient(obj: Ingredient) {
        IngredientManager.update(obj)
    }

    fun deleteIngredient(id: ULong) {
        IngredientManager.delete(id)
    }

    fun findByNameIngredient(name: String) = IngredientManager.findByName(name)

    fun getAllIngredients(): List<Ingredient> = IngredientManager.getAll()

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

    fun addToFavorite(userID: ULong, recipeID: ULong) {
        RecipeManager.addToFavorite(recipeID, userID)
    }

    fun deleteFromFavorite(userID: ULong, recipeID: ULong) {
        RecipeManager.deleteFromFavorite(recipeID, userID)
    }

    fun isInFavorite(userID: ULong, recipeID: ULong): Boolean = RecipeManager.isInFavorite(recipeID, userID)


    fun addComment(userID: ULong, text: String, recipeID: ULong) {
        RecipeManager.addComment(userID, text, recipeID)
    }

    fun isOwner(userID: ULong, recipeID: ULong): Boolean = RecipeManager.isOwner(recipeID, userID)
    fun addStage(recipeID: ULong, stage: Stage) {
        RecipeManager.addStage(recipeID, stage)
    }

    //Publish
    fun getPublishQueue(): List<RecipePreview> = RecipeManager.getPublishQueue()
    fun addToPublishQueue(id: ULong) {
        RecipeManager.publish(id)
    }

    fun acceptRecipePublication(id: ULong) {
        RecipeManager.approvePublication(id)
    }

    fun cancelRecipePublication(id: ULong) {
        RecipeManager.cancelRecipePublication(id)
    }

    //Stage
    fun updateStage(obj: Stage) {
        StageManager.update(obj)
    }

    fun deleteStage(id: ULong) {
        StageManager.delete(id)
    }

    //User
    fun createUser(obj: User) {
        UserManager.create(obj)
    }

    fun getUser(id: ULong) = UserManager.read(id)

    fun updateUser(obj: User) {
        UserManager.update(obj)
    }

    fun deleteUser(id: ULong) {
        UserManager.delete(id)
    }

    fun getAllUsers(): List<User> = UserManager.getAll()
    fun changeUserRole(id: ULong, isAdmin: Boolean) {
        UserManager.changeRole(id, isAdmin)
    }

    fun isAdmin(id: ULong): Boolean = UserManager.isAdmin(id)
    fun getUserPublishedRecipes(id: ULong): List<RecipePreview> = UserManager.getPublishedRecipes(id)

    //Account
    fun register(login: String, password: String) {
        AccountService.register(login, password)
    }

    fun logIn(login: String, password: String): User = AccountService.logIN(login, password)

    //feed
    fun getFeedSortedByDate(): List<RecipePreview> = FeedService.getSortedByDate()
    fun getSavedFeed(id: ULong): List<RecipePreview> = UserManager.getSavedRecipes(id)
    fun getOwnFeed(id: ULong): List<RecipePreview> = UserManager.getOwnRecipes(id)
}