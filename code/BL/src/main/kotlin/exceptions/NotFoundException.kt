package  bl.exceptions

import bl.exceptions.BaseException

class NotFoundException(override val message: String) : BaseException(message)