package bl.repositories

interface IRepository<T> {
    fun create(obj: T)
    fun read(id: ULong): T
    fun update(obj: T)
    fun delete(id: ULong)
    fun getAll(): List<T>

    fun exists(id: ULong): Boolean
}