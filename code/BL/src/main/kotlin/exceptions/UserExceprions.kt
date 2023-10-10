package bl.exceptions
class LogInFailedException(override val message: String) : BaseException(message)
class NotAuthorizedException(override val message: String) : BaseException(message)