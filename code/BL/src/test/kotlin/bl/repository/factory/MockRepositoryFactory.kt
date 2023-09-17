package bl.repository.factory

import bl.repositories.factory.IRepositoryFactory
import bl.repository.*

class MockRepositoryFactory : IRepositoryFactory {
    override fun createCommentRepository(): MockCommentRepository = MockCommentRepository()

    override fun createIngredientRepository(): MockIngredientRepository = MockIngredientRepository()

    override fun createRecipeRepository(): MockRecipeRepository = MockRecipeRepository()

    override fun createStageRepository(): MockStageRepository = MockStageRepository()

    override fun createUserRepository(): MockUserRepository = MockUserRepository()
}