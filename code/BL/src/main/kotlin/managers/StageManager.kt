package bl.managers

import bl.entities.Stage
import bl.exceptions.*
import bl.repositories.IRepository
import bl.repositories.IStageRepository
import org.slf4j.LoggerFactory

object StageManager : ICRUDManager<Stage> {
    private lateinit var repository: IStageRepository

    private val logger = LoggerFactory.getLogger("mainLogger")

    override fun registerRepository(repository: IRepository<Stage>) {
        this.repository = repository as IStageRepository
    }

    override fun create(obj: Stage) {
        logger.trace("{} called with parameters {}", ::create.name, obj)

        if (!isUniq(obj)) throw AlreadyExistingIngredientException("Stage already exists")
        if (!validate(obj)) throw ValidationIngredientException("Stage failed validation")

        repository.create(obj)
    }

    override fun read(id: ULong): Stage {
        logger.trace("{} called with parameters {}", ::read.name, id)

        //todo check
        if (!isExist(id)) throw NotExistingIngredientException("Stage not exists")

        return repository.read(id)
    }

    override fun update(obj: Stage) {
        logger.trace("{} called with parameters {}", ::update.name, obj)

        //todo check
        if (!isExist(obj.id)) throw NotExistingStageException("Stage not exists")
        if (!validate(obj)) throw ValidationStageException("Stage failed validation")

        repository.update(obj)
    }

    override fun delete(id: ULong) {
        logger.trace("{} called with parameters {}", ::delete.name, id)

        if (!isExist(id)) throw NotExistingStageException("Stage not exists")

        repository.delete(id)
    }

    override fun getAll(): List<Stage> {
        logger.trace("{} called", ::getAll.name)
        return repository.getAll()
    }

    override fun isUniq(obj: Stage): Boolean {
        logger.trace("{} called with parameters {}", ::isUniq.name, obj)

        return isExist(obj.id)
    }

    override fun isExist(id: ULong): Boolean {
        logger.trace("{} called with parameters {}", ::isExist.name, id)

        return repository.exists(id)
    }

    override fun validate(obj: Stage): Boolean {
        logger.trace("{} called with parameters {}", ::validate.name, obj)

        return obj.time > 0u && obj.description.isNotEmpty()
    }
}