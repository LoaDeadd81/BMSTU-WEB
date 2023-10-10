package bl.repositories

import bl.entities.Ingredient

interface IIngredientRepository {
    fun create(obj: Ingredient): Int
    fun read(id: Int): Ingredient
    fun update(obj: Ingredient)
    fun delete(id: Int)
    fun getAll(): List<Ingredient>
    fun isNameNotExist(name: String): Boolean
    fun findByName(name: String): List<Ingredient>
}