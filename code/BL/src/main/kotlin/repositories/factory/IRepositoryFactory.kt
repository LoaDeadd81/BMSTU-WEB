package bl.repositories.factory

import bl.repositories.ICommentRepository
import bl.repositories.IIngredientRepository
import bl.repositories.IRecipeRepository
import bl.repositories.IUserRepository

interface IRepositoryFactory {
    fun createCommentRepository(): ICommentRepository
    fun createIngredientRepository(): IIngredientRepository
    fun createRecipeRepository(): IRecipeRepository
    fun createUserRepository(): IUserRepository
}