package bl.entities

enum class IngredientType(val value: Int) {
    MEAT(1), HORTICULTURAL_PROD(2), TASTE_PROD(3), FATS(4), MILK_PROD(5), CONFECTIONERY(6), GRAIN(7), FISH(8), EGGS(9);

    fun toString(x: IngredientType): String = when (x) {
        MEAT -> "Мясо"
        HORTICULTURAL_PROD -> "Плодоовощные продукты"
        TASTE_PROD -> "Вкусовые продукты"
        FATS -> "Жиры"
        MILK_PROD -> "Молочные продукты"
        CONFECTIONERY -> "Кондитреские продукты"
        GRAIN -> "Зёрно-мучные продукты"
        FISH -> "Морепродукты"
        EGGS -> "Яичные продукты"
    }

    companion object {
        fun fromInt(value: Int) = IngredientType.values().first { it.value == value }
    }
}

enum class ProcessingType(val value: Int) {
    WASH(1), DRY(2), CLEAR(3), CUT(4), MIX(5), BOIL(6), FRY(7), BAKE(8), PUT_OUT(9);

    fun toString(x: ProcessingType): String = when (x) {
        WASH -> "Помыть"
        DRY -> "Высушить"
        CLEAR -> "Очистить"
        CUT -> "Нарезать"
        MIX -> "Смешать"
        BOIL -> "Сварить"
        FRY -> "Пожарить"
        BAKE -> "Выпечь"
        PUT_OUT -> "Потушить"
    }

    companion object {
        fun fromInt(value: Int) = ProcessingType.values().first { it.value == value }
    }
}

data class Ingredient(
    val id: ULong,
    var name: String,
    var type: IngredientType,
    var nutritionalValue: UInt,
)

data class IngredientInStage(
    val id: ULong,
    var name: String,
    var type: IngredientType,
    var amount: UInt,
    var nutritionalValue: UInt,
    var processingType: ProcessingType
)

//data class IngredientInRecipe(
//    val id: ULong,
//    var name: String,
//    var type: IngredientType,
//    var amount: UInt,
//    var nutritionalValue: UInt,
//)
