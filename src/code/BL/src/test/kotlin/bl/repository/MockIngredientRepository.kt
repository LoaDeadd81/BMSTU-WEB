//package bl.repository
//
//import bl.entities.Ingredient
//import bl.repositories.IIngredientRepository
//
//class MockIngredientRepository : IIngredientRepository {
//    override fun isNameNotExist(name: String): Boolean = IngredientMockData.find { x -> x.name == name } == null
//
//    override fun create(obj: Ingredient) {
//        IngredientMockData += obj
//    }
//
//    override fun read(id: ULong): Ingredient = IngredientMockData[id.toInt()]
//
//    override fun update(obj: Ingredient) {
//        IngredientMockData[obj.id.toInt()] = obj
//    }
//
//    override fun delete(id: ULong) {
//        IngredientMockData[id.toInt()].name = "deleted"
//    }
//
//    override fun getAll(): List<Ingredient> = IngredientMockData.toList()
//
//    override fun exists(id: ULong): Boolean = IngredientMockData.find { x -> x.id == id } != null
//
//    override fun findByName(name: String): Ingredient? = IngredientMockData.find { x -> x.name == name }
//}