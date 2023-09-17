package bl.managers

import bl.entities.RecipePreview
import bl.repositories.IRecipeRepository
import org.slf4j.LoggerFactory

object FeedService {
    private lateinit var repository: IRecipeRepository

    private val logger = LoggerFactory.getLogger("mainLogger")

    fun registerRepository(repository: IRecipeRepository) {
        this.repository = repository
    }

    fun getSortedByDate(): List<RecipePreview> {
        logger.trace("{} called", ::getSortedByDate.name)

        return repository.getSortedByDate()
    }
}