package bl.exceptions

class AlreadyExistingIngredientException(override val message: String) : BaseException(message)
class NotExistingIngredientException(override val message: String) : BaseException(message)
class ValidationIngredientException(override val message: String) : BaseException(message)