package da.exeption

import bl.exceptions.BaseException

class NotFoundException(override val message: String) : BaseException(message)