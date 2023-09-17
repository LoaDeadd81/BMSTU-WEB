package bl.exceptions

class AlreadyExistingRecipeException(override val message: String) : BaseException(message)
class NotExistingRecipeException(override val message: String) : BaseException(message)
class ValidationRecipeException(override val message: String) : BaseException(message)
class RecipeAlreadyPublishedException(override val message: String) : BaseException(message)
class RecipeNotInPublishQueueException(override val message: String) : BaseException(message)