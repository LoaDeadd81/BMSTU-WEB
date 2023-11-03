package exceptions

import bl.exceptions.BaseException

class IllegalArguments(override val message: String) : BaseException(message)