package bl.repositories

import bl.entities.Ingredient

interface IIngredientRepository : IRepository<Ingredient> {
    fun isNameNotExist(name: String): Boolean
    fun findByName(name: String): Ingredient?
}