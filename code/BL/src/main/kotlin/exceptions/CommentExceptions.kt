package bl.exceptions

class AlreadyExistingCommentException(override val message: String) : BaseException(message)
class NotExistingCommentException(override val message: String) : BaseException(message)
class ValidationCommentException(override val message: String) : BaseException(message)
