package bl.exceptions

class AlreadyExistingUserException(override val message: String) : BaseException(message)
class NotExistingUserException(override val message: String) : BaseException(message)
class ValidationUserException(override val message: String) : BaseException(message)
class LogInFailedException(override val message: String) : BaseException(message)
class NotAuthorizedException(override val message: String) : BaseException(message)