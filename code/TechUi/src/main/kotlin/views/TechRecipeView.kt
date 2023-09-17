package techUi.views

import bl.entities.*
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class TechRecipeView {
    private val techIngredientView = TechIngredientView()
    private val techStageView = TechStageView()
    private val techCommentView = TechCommentView()
    private val logger = LoggerFactory.getLogger("mainLogger")

    fun printNotAuth(recipe: Recipe): Int {
        logger.debug("Showed unauth recipe view")

        printRecipe(recipe)
        printBaseActions()
        printBottom()
        return readln().toInt()
    }

    fun printAuth(recipe: Recipe, isInFavorite: Boolean): Int {
        logger.debug("Showed auth recipe view")

        printRecipe(recipe)
        printBaseActions()
        if (isInFavorite) println("2. Удалить из избраного")
        else println("2. Добавить в избранное")
        println("3. Прокоментировать")
        printBottom()
        return readln().toInt()
    }

    fun printOwnerView(recipe: Recipe, isInFavorite: Boolean): Int {
        logger.debug("Showed owner recipe view")

        printRecipe(recipe, true)
        printBaseActions()
        if (isInFavorite) println("2. Удалить из избраного")
        else println("2. Добавить в избранное")
        println("3. Прокоментировать")
        println("4. Изменить рецепт")
        println("5. Удалить рецепт")
        if (!recipe.published) println("6. Опубликовать")
        printBottom()
        return readln().toInt()
    }

    private fun printRecipe(recipe: Recipe, ownerView: Boolean = false) {
        println("Рецепт")
        println("id: " + recipe.id)
        println("Имя: " + recipe.name)
        println("Описание: " + recipe.description)
        println("Время: " + recipe.time)
        if (ownerView) println("Рецепт опубликован: " + recipe.published)
        println("Количество порций: " + recipe.servingsNum)
        println("Белки: " + recipe.pfc.protein)
        println("Жиры: " + recipe.pfc.fat)
        println("Углеводы: " + recipe.pfc.carbon)
        println("Дата создания: " + recipe.date)
        println("Создатель: " + recipe.owner.login)
        println("Ингредиенты: ")
        for (item in recipe.ingredients) techIngredientView.printIngredientInRecipe(item, "\t")
        println("Стадии приготовления: ")
        for (item in recipe.stages) techStageView.print(item, "\t")
        println("Коментарии: ")
        for (item in recipe.comments) techCommentView.print(item, "\t")
        println()
    }

    fun inputRecipe(user: User, recipeId: ULong, ingredients: List<Ingredient>): Recipe {
        print("Имя: ")
        val name = readln()
        print("Описание: ")
        val description = readln()
        print("Время: ")
        val time = readln().toULong()
        print("Количество порций: ")
        val servingsNum = readln().toUInt()
        print("Белки: ")
        val protein = readln().toInt()
        print("Жиры: ")
        val fat = readln().toInt()
        print("Углеводы: ")
        val carbon = readln().toInt()
        val date = LocalDateTime.now()
        val owner = user

        val stages = mutableListOf<Stage>()
        println("Стадии приготовления: ")
        println("1. Добавить стадию")
        println("0. Выход")
        var req = readln().toInt()
        while (req > 0) {
            stages.add(inputStage(ingredients))
            println("1. Добавить стадию")
            println("0. Выход")
            req = readln().toInt()
        }

        return Recipe(
            recipeId,
            name,
            description,
            time,
            servingsNum,
            PFC(protein, fat, carbon),
            date,
            false,
            stages,
            listOf(),
            owner,
            listOf()
        )
    }

    private fun inputStage(ingredients: List<Ingredient>): Stage {
        print("Описание: ")
        val description = readln()
        print("Время: ")
        val time = readln().toULong()

        val ingr = mutableListOf<IngredientInStage>()
        println("Ингредиенты: ")

        for (item in ingredients) {
            println("id: " + item.id)
            println("Имя: " + item.name)
            println("Тип: " + item.type)
            println("Пищевая ценность: " + item.nutritionalValue)
            println()
        }

        println("1. Добавить ингредиент")
        println("0. Выход")
        var req = readln().toInt()
        while (req > 0) {
            ingr.add(inputIngredient(ingredients))
            println("1. Добавить ингредиент")
            println("0. Выход")
            req = readln().toInt()
        }

        return Stage(0u, time, description, ingr)
    }

    private fun inputIngredient(ingredients: List<Ingredient>): IngredientInStage {
        print("Номер ингредиента: ")
        val num = readln().toULong()
        val tmp = ingredients.find { x -> x.id == num }

        val id = tmp?.id ?: 0u
        val name = tmp?.name ?: ""
        val nutritionalValue = tmp?.nutritionalValue ?: 0u
        val type = tmp?.type ?: IngredientType.MEAT


        print("Количество: ")
        val amount = readln().toUInt()
        println("1. Помыть")
        println("2. Высушить")
        println("3. Вкусовые продукты")
        println("4. Очистить")
        println("5. Нарезать")
        println("6. Смешать")
        println("7. Сварить")
        println("8. Пожарить")
        println("9. Выпечь")
        println("10. Потушить")
        print("Тип обработки: ")
        val processingType = ProcessingType.fromInt(readln().toInt())

        return IngredientInStage(id, name, type, amount, nutritionalValue, processingType)
    }

    private fun printBaseActions() {
        println("1. Просмотреть профиль создателя")
    }

    private fun printBottom() {
        println("0. Выход")
        print("Выбор: ")
    }
}