package bl.managers

import bl.repositories.IRepository

interface ICRUDManager<T> {

    fun registerRepository(repository: IRepository<T>)
    fun create(obj: T)
    fun read(id: ULong): T
    fun update(obj: T)
    fun delete(id: ULong)
    fun getAll(): List<T>
    fun isUniq(obj: T): Boolean
    fun isExist(id: ULong): Boolean
    fun validate(obj: T): Boolean
}