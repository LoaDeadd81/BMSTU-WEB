package bl.exceptions


class NotExistingStageException(override val message: String) : BaseException(message)
class ValidationStageException(override val message: String) : BaseException(message)