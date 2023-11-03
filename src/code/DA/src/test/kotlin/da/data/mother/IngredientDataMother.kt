package da.data.mother

import bl.entities.Ingredient
import bl.entities.IngredientType

object IngredientDataMother {
    fun newIngredient(): Ingredient {
        return Ingredient(6, "name6", IngredientType.MEAT, 6)
    }

    fun updatedIngredient(): Ingredient {
        return Ingredient(1, "name_new", IngredientType.EGGS, 10)
    }

    fun notExistIngredient(): Ingredient {
        return Ingredient(9, "name", IngredientType.EGGS, 10)
    }

    fun regularIngredient(): Ingredient {
        return Ingredient(1, "name1", IngredientType.MEAT, 1)
    }

    fun allIngredients(): List<Ingredient> {
        return listOf(
            Ingredient(1, "name1", IngredientType.MEAT, 1),
            Ingredient(2, "name2", IngredientType.MEAT, 2),
            Ingredient(3, "name3", IngredientType.MEAT, 3),
            Ingredient(4, "name4", IngredientType.MEAT, 4),
            Ingredient(5, "name5", IngredientType.MEAT, 5),
        )
    }
}