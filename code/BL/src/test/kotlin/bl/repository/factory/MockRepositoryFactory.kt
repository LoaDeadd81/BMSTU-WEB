package bl.repository.factory

import bl.repositories.factory.IRepositoryFactory
import bl.repository.MockCommentRepository
import bl.repository.MockIngredientRepository
import bl.repository.MockRecipeRepository
import bl.repository.MockUserRepository

class MockRepositoryFactory : IRepositoryFactory {
    override fun createCommentRepository(): MockCommentRepository = MockCommentRepository()

    override fun createIngredientRepository(): MockIngredientRepository = MockIngredientRepository()

    override fun createRecipeRepository(): MockRecipeRepository = MockRecipeRepository()

    override fun createUserRepository(): MockUserRepository = MockUserRepository()
}