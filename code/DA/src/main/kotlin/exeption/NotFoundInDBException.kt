package da.exeption

import bl.exceptions.BaseException

class NotFoundInDBException(override val message: String) : BaseException(message)