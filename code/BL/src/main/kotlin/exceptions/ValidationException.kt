package  bl.exceptions

import bl.exceptions.BaseException

class ValidationException(override val message: String) : BaseException(message)