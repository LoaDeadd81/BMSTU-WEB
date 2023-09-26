package da

import bl.Facade
import bl.entities.*
import bl.exceptions.*
import bl.managers.AccountService
import com.radcortez.flyway.test.annotation.DataSource
import com.radcortez.flyway.test.annotation.FlywayTest
import com.radcortez.flyway.test.junit.DataSourceInfo
import com.radcortez.flyway.test.junit.DataSourceProvider
import da.dao.*
import da.exeption.NotFoundInDBException
import da.repositories.factory.PgRepositoryFactory
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtensionContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.assertEquals

val dotenv = dotenv()
val facade = Facade(PgRepositoryFactory(dotenv["TEST_SCHEMA"]))

class PGDataSourceProvider : DataSourceProvider {
    override fun getDatasourceInfo(extensionContext: ExtensionContext?): DataSourceInfo {
        val url =
            "jdbc:" + dotenv["DB_CONNECT"] + "://" + dotenv["DB_HOST"] + ":" + dotenv["DB_PORT"] + "/" + dotenv["DB_NAME"] + "?currentSchema=" + dotenv["TEST_SCHEMA"]
        val user = dotenv["DB_USER"]
        val password = dotenv["DB_PASSWORD"]
        return DataSourceInfo.config(url, user, password)
    }

}

@FlywayTest(DataSource(PGDataSourceProvider::class))
class CommentTest {

    @Test
    @DisplayName("Update Comment")
    fun updateComment() {
        val id = 1
        val str = "2023-01-01 00:00"
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dateTime = LocalDateTime.parse(str, formatter)

        val expected = Comment(id.toULong(), dateTime, "text1", User(1u, "login1", "password1", true))
        expected.text = "fixed1"
        facade.updateComment(expected)
        val actual = transaction {
            CommentTable.findById(id)?.toEntity() ?: throw NotFoundInDBException("Comment with id = $id not found")
        }

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update Comment, not exist")
    fun updateCommentNotExist() {
        val tmp = Comment(9u, LocalDateTime.now(), "text1", UserMockData[1])

        assertThrows<NotExistingCommentException> { facade.updateComment(tmp) }
    }

    @Test
    @DisplayName("Update Comment, didn't passed validation")
    fun updateNotValidComment() {
        val tmp = Comment(1u, LocalDateTime.now(), "", UserMockData[1])

        assertThrows<ValidationCommentException> { facade.updateComment(tmp) }
    }

    @Test
    @DisplayName("Delete Comment")
    fun deleteComment() {
        val id = 3
        facade.deleteComment(id.toULong())
        assertThrows<NotFoundInDBException> {
            transaction {
                val obj = CommentTable.findById(id)
                obj?.toEntity() ?: throw NotFoundInDBException("Comment with id = $id not found")
            }
        }
    }

    @Test
    @DisplayName("Delete Comment, not exist")
    fun deleteCommentNotExist() {
        assertThrows<NotExistingCommentException> { facade.deleteComment(9u) }
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setUser() {
            facade.logIn("login1", "password1")
        }
    }
}

@FlywayTest(DataSource(PGDataSourceProvider::class))
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class IngredientTest {
    @Order(1)
    @Test
    @DisplayName("Create Ingredient")
    fun createIngredient() {
        val id = 6
        val expected = Ingredient(id.toULong(), "name6", IngredientType.MEAT, 1u)
        facade.createIngredient(expected)
        val actual = transaction {
            IngredientTable.findById(id)?.toEntity()
                ?: throw NotFoundInDBException("Ingredient with id = $id not found")
        }

        assertEquals(expected, actual)
    }

    @Order(2)
    @Test
    @DisplayName("Create Ingredient, not uniq")
    fun createIngredientNotUniq() {
        val tmp = Ingredient(5u, "name5", IngredientType.MEAT, 987u)

        assertThrows<AlreadyExistingIngredientException> { facade.createIngredient(tmp) }
    }

    @Order(2)
    @Test
    @DisplayName("Create Ingredient, didn't passed validation")
    fun createNotValidIngredient() {
        val tmp = Ingredient(5u, "", IngredientType.MEAT, 987u)

        assertThrows<ValidationIngredientException> { facade.createIngredient(tmp) }
    }

    @Order(2)
    @Test
    @DisplayName("Update Ingredient")
    fun updateIngredient() {
        val id = 1
        val expected = Ingredient(id.toULong(), "", IngredientType.MEAT, 1u)
        expected.name = "fixed1"
        facade.updateIngredient(expected)
        val actual = transaction {
            val obj = IngredientTable.findById(id)
            obj?.toEntity() ?: throw NotFoundInDBException("Ingredient with id = $id not found")
        }

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update Ingredient, not exist")
    fun updateIngredientNotExist() {
        val tmp = Ingredient(9u, "name4", IngredientType.MEAT, 987u)

        assertThrows<NotExistingIngredientException> { facade.updateIngredient(tmp) }
    }

    @Order(2)
    @Test
    @DisplayName("Update Ingredient, didn't passed validation")
    fun updateNotValidIngredient() {
        val tmp = Ingredient(2u, "", IngredientType.MEAT, 987u)

        assertThrows<ValidationIngredientException> { facade.updateIngredient(tmp) }
    }

    @Order(2)
    @Test
    @DisplayName("Delete Ingredient")
    fun deleteIngredient() {
        val id = 2
        facade.deleteIngredient(id.toULong())
        assertThrows<NotFoundInDBException> {
            transaction {
                val obj = IngredientTable.findById(id)
                obj?.toEntity() ?: throw NotFoundInDBException("Comment with id = $id not found")
            }
        }
    }

    @Order(2)
    @Test
    @DisplayName("Delete Ingredient, not exist")
    fun deleteIngredientNotExist() {
        assertThrows<NotExistingIngredientException> { facade.deleteIngredient(9u) }
    }

    @Order(2)
    @Test
    @DisplayName("Find by name Ingredient")
    fun findByNameIngredient() {
        val id = 2
        val name = "name2"

        val expected = Ingredient(id.toULong(), "name2", IngredientType.MEAT, 2u)
        val actual = facade.findByNameIngredient(name)

        assertEquals(expected, actual)
    }

    @Order(2)
    @Test
    @DisplayName("Find bu name Ingredient, not exist")
    fun findByNameIngredientNotExist() {
        assertThrows<NotFoundInDBException> { facade.findByNameIngredient("name9") }
    }

    @Order(2)
    @Test
    @DisplayName("Get all Ingredients")
    fun getAllIngredients() {
        val expected = transaction {
            IngredientTable.all().map { it.toEntity() }
        }
        val actual = facade.getAllIngredients()

        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setUser() {
            facade.logIn("login1", "password1")
        }
    }
}


@FlywayTest(DataSource(PGDataSourceProvider::class))
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class RecipeTest {

    @Test
    @Order(2)
    @DisplayName("Create Recipe")
    fun createRecipe() {
        val id = 6
        val ingredients = listOf(
            IngredientInStage(1u, "name1", IngredientType.MEAT, 10u, 1u, ProcessingType.WASH),
            IngredientInStage(2u, "name2", IngredientType.MEAT, 20u, 2u, ProcessingType.WASH),
            IngredientInStage(3u, "name3", IngredientType.MEAT, 30u, 3u, ProcessingType.WASH),
            IngredientInStage(4u, "name4", IngredientType.MEAT, 40u, 4u, ProcessingType.WASH),
            IngredientInStage(5u, "name5", IngredientType.MEAT, 50u, 5u, ProcessingType.WASH)
        )
        val stages = listOf(
            Stage(6u, 30u, "description0", ingredients),
            Stage(7u, 31u, "description1", ingredients),
            Stage(8u, 32u, "description2", ingredients),
            Stage(9u, 33u, "description3", ingredients),
            Stage(10u, 34u, "description4", ingredients)
        )

        val recipeIngredients = mutableListOf<IngredientInStage>()

        for (stage in stages) {
            for (ingredient in stage.ingredients) {
                val tmp = recipeIngredients.find { it.id == ingredient.id }
                if (tmp == null) {
                    recipeIngredients += ingredient.copy()
                } else tmp.amount += ingredient.amount
            }
        }

        val expected = Recipe(
            id.toULong(),
            "name5",
            "description5",
            35u,
            5u,
            PFC(5, 5, 5),
            LocalDateTime.now(),
            false,
            stages,
            listOf(),
            User(1u, "login1", "password1", true),
            recipeIngredients
        )
        facade.createRecipe(expected)
        val actual = facade.getRecipe(6u)

        actual.date = expected.date
        assertEquals(expected, actual)
    }

    @Test
    @Order(3)
    @DisplayName("Create Recipe, didn't passed validation")
    fun createNotValidRecipe() {
        val tmp = Recipe(
            5u,
            "",
            "description5",
            35u,
            5u,
            PFC(5, 5, 5),
            LocalDateTime.now(),
            false,
            listOf(),
            listOf(),
            User(1u, "", "", false),
            listOf()
        )

        assertThrows<ValidationRecipeException> { facade.createRecipe(tmp) }
    }

    @Test
    @Order(3)
    @DisplayName("Get Recipe")
    fun getRecipe() {
        val id = 1
        val str = "2023-01-01 00:00"
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dateTime = LocalDateTime.parse(str, formatter)

        val expected = Recipe(
            id.toULong(),
            "name1",
            "desc1",
            1u,
            1u,
            PFC(1, 1, 1),
            dateTime,
            true,
            listOf(
                Stage(
                    1u,
                    1u,
                    "desc1",
                    listOf(IngredientInStage(1u, "name1", IngredientType.MEAT, 1u, 1u, ProcessingType.WASH))
                )
            ),
            listOf(Comment(id.toULong(), dateTime, "text1", User(1u, "login1", "password1", true))),
            User(1u, "login1", "password1", true),
            listOf(IngredientInStage(1u, "name1", IngredientType.MEAT, 1u, 1u, ProcessingType.WASH))
        )
        val actual = facade.getRecipe(id.toULong())

        assertEquals(expected, actual)
    }

    @Test
    @Order(3)
    @DisplayName("Get Recipe access denied")
    fun getRecipeAccessDenied() {
        val id = 3
        AccountService.logIN("login2", "password2")

        assertThrows<AccessDeniedException> { facade.getRecipe(id.toULong()) }

        AccountService.logIN("login1", "password1")
    }

    @Test
    @Order(3)
    @DisplayName("Get Recipe, not exist")
    fun getRecipeNotExist() {
        assertThrows<NotExistingRecipeException> { facade.getRecipe(9u) }
    }

    @Test
    @Order(3)
    @DisplayName("Update Recipe")
    fun updateRecipe() {
        val id = 2
        val str = "2023-02-02 00:00"
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dateTime = LocalDateTime.parse(str, formatter)

        val expected = Recipe(
            id.toULong(),
            "name2",
            "desc2",
            2u,
            2u,
            PFC(2, 2, 2),
            dateTime,
            true,
            listOf(
                Stage(
                    6u, 2u, "desc2", listOf(
                        IngredientInStage(2u, "name2", IngredientType.MEAT, 1u, 2u, ProcessingType.DRY),
                        IngredientInStage(2u, "name2", IngredientType.MEAT, 2u, 2u, ProcessingType.WASH)
                    )
                )
            ),
            listOf(Comment(id.toULong(), dateTime, "text2", User(2u, "login2", "password2", false))),
            User(2u, "login2", "password2", false),
            listOf(IngredientInStage(2u, "name2", IngredientType.MEAT, 3u, 2u, ProcessingType.DRY))
        )
        expected.name = "fixed1"
        facade.updateRecipe(expected)
        val actual = facade.getRecipe(id.toULong())

        actual.date = expected.date
        assertEquals(expected, actual)
    }

    @Test
    @Order(3)
    @DisplayName("Update Recipe, not exist")
    fun updateRecipeNotExist() {
        val tmp = Recipe(
            9u,
            "name5",
            "description5",
            35u,
            5u,
            PFC(5, 5, 5),
            LocalDateTime.now(),
            false,
            listOf(),
            listOf(),
            User(1u, "", "", false),
            listOf()
        )

        assertThrows<NotExistingRecipeException> { facade.updateRecipe(tmp) }
    }

    @Test
    @Order(3)
    @DisplayName("Update Recipe, didn't passed validation")
    fun updateNotValidRecipe() {
        val tmp = Recipe(
            1u,
            "",
            "description1",
            31u,
            1u,
            PFC(1, 1, 1),
            LocalDateTime.now(),
            false,
            listOf(),
            listOf(),
            User(1u, "", "", false),
            listOf()
        )

        assertThrows<ValidationRecipeException> { facade.updateRecipe(tmp) }
    }

    @Test
    @Order(3)
    @DisplayName("Delete Recipe")
    fun deleteRecipe() {
        val id = 3
        facade.deleteRecipe(id.toULong())
        assertThrows<NotFoundInDBException> {
            transaction {
                val obj = RecipeTable.findById(id)
                obj?.toEntity() ?: throw NotFoundInDBException("Recipe with id = $id not found")
            }
        }
    }

    @Test
    @Order(3)
    @DisplayName("Delete Recipe, not exist")
    fun deleteRecipeNotExist() {
        assertThrows<NotExistingRecipeException> { facade.deleteRecipe(9u) }
    }

    @Test
    @Order(3)
    @DisplayName("Add to favorite")
    fun addToFavorite() {
        val expected = Pair<ULong, ULong>(5u, 1u)
        facade.addToFavorite(expected.first, expected.second)
        val tmp = transaction {
            SavedRecipeTable.findById(16) ?: throw NotFoundInDBException("")
        }
        val actual = Pair(tmp.userId.value.toULong(), tmp.recipeId.value.toULong())

        assertEquals(expected, actual)
    }

    @Test
    @Order(3)
    @DisplayName("Add to favorite, no recipe")
    fun addToFavoriteNoRecipe() {
        assertThrows<NotExistingRecipeException> { facade.addToFavorite(4u, 9u) }
    }

    @Test
    @Order(3)
    @DisplayName("Add to favorite, no user")
    fun addToFavoriteNoUser() {
        assertThrows<NotExistingUserException> { facade.addToFavorite(9u, 4u) }
    }

    @Test
    @Order(3)
    @DisplayName("Add comment")
    fun addComment() {
        val id = 1
        val comId = 6
        val tmpUser = transaction {
            UserTable.findById(id)?.toEntity() ?: throw NotFoundInDBException("User with id = $id not found")
        }

        val expected = Comment(comId.toULong(), LocalDateTime.now(), "text5", tmpUser)
        facade.addComment(id.toULong(), "text5", id.toULong())
        val actual1 = transaction {
            CommentTable.findById(comId)?.toEntity() ?: throw NotFoundInDBException("Comment with id = $id not found")
        }
        val actual2 = transaction {
            RecipeTable.findById(id)?.toEntity()?.comments?.last()
                ?: throw NotFoundInDBException("Recipe with id = $id not found")
        }

        expected.date = actual2.date
        assertAll("Add comment asserts", { assertEquals(expected, actual1) }, { assertEquals(expected, actual2) })
    }

    @Test
    @Order(3)
    @DisplayName("Add comment, no user")
    fun addCommentNoUser() {
        assertThrows<NotExistingUserException> { facade.addComment(9u, "text5", 1u) }
    }

    @Test
    @Order(3)
    @DisplayName("Add comment, no recipe")
    fun addCommentNoRecipe() {
        assertThrows<NotExistingRecipeException> { facade.addComment(1u, "text5", 9u) }
    }

    @Test
    @Order(3)
    @DisplayName("Add comment, not valid")
    fun addNotValidComment() {
        assertThrows<ValidationCommentException> { facade.addComment(1u, "", 1u) }
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setUser() {
            facade.logIn("login1", "password1")
        }
    }
}

@FlywayTest(DataSource(PGDataSourceProvider::class))
class PublishTest {

    @Test
    @DisplayName("Get publish queue")
    fun getPublishQueue() {
        val expected = facade.getPublishQueue()
        val actual = transaction {
            val query = da.dao.PublishQueue.innerJoin(Recipes).slice(Recipes.columns).selectAll()
            RecipePreviewTable.wrapRows(query).map { it.toEntity() }
        }

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Add to publish queue")
    fun addToPublishQueue() {
        val id = 4
        val expected = true
        facade.addToPublishQueue(id.toULong())
        val actual = transaction {
            PublishQueueTable.find { da.dao.PublishQueue.recipe eq EntityID(id, Recipes) }.firstOrNull() != null
        }

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Add to publish queue, recipe not exists")
    fun addToPublishQueueRecipeNotExist() {
        assertThrows<NotExistingRecipeException> { facade.addToPublishQueue(9u) }
    }

    @Test
    @DisplayName("Add to publish queue, already published")
    fun addToPublishQueueRecipeAlreadyPublished() {
        assertThrows<RecipeAlreadyPublishedException> { facade.addToPublishQueue(1u) }
    }

    @Test
    @DisplayName("Accept recipe publication")
    fun acceptRecipePublication() {
        val id = 3u
        val expected = true
        facade.addToPublishQueue(id.toULong())
        facade.acceptRecipePublication(id.toULong())
        val actual = transaction {
            RecipeTable.findById(id.toInt())?.published ?: throw NotFoundInDBException("Recipe with id = $id not found")
        }

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Accept recipe publication, recipe not exists")
    fun acceptRecipePublicationRecipeNotExist() {
        assertThrows<NotExistingRecipeException> { facade.acceptRecipePublication(9u) }
    }

    @Test
    @DisplayName("Accept recipe publication, not in queue")
    fun acceptRecipePublicationRecipeNotInQueue() {
        assertThrows<RecipeNotInPublishQueueException> { facade.acceptRecipePublication(2u) }
    }

    @Test
    @DisplayName("Cansel recipe publication")
    fun cancelRecipePublication() {
        val id = 4
        val expected = true
        facade.cancelRecipePublication(id.toULong())
        val actual = transaction {
            PublishQueueTable.find { da.dao.PublishQueue.recipe eq EntityID(id, Recipes) }.firstOrNull() == null
        }

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Cansel recipe publication, recipe not exists")
    fun cancelRecipePublicationRecipeNotExist() {
        assertThrows<NotExistingRecipeException> { facade.cancelRecipePublication(9u) }
    }

    @Test
    @DisplayName("Cansel recipe publication, not in queue")
    fun cancelRecipePublicationRecipeNotInQueue() {
        assertThrows<RecipeNotInPublishQueueException> { facade.cancelRecipePublication(2u) }
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setUser() {
            facade.logIn("login1", "password1")
        }
    }
}

@FlywayTest(DataSource(PGDataSourceProvider::class))
class UserTest {
    @Test
    @DisplayName("Get User")
    fun getUser() {
        val id = 1
        val expected = User(id.toULong(), "login1", "password1", true)
        val actual = facade.getUser(id.toULong())

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get User, not exist")
    fun getUserNotExist() {
        assertThrows<NotExistingUserException> { facade.getUser(9u) }
    }

    @Test
    @DisplayName("Update User")
    fun updateUser() {
        val id = 2
        val expected = User(id.toULong(), "login2", "password2", false)
        expected.password = "fixed1"
        facade.updateUser(expected)
        val actual = transaction {
            UserTable.findById(id)?.toEntity() ?: throw NotFoundInDBException("User with id = $id not found")
        }

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update User, not exist")
    fun updateUserNotExist() {
        val tmp = User(9u, "login1", "password1", false)

        assertThrows<NotExistingUserException> { facade.updateUser(tmp) }
    }

    @Test
    @DisplayName("Update User, didn't passed validation")
    fun updateNotValidUser() {
        val tmp = User(1u, "", "", false)

        assertThrows<ValidationUserException> { facade.updateUser(tmp) }
    }

    @Test
    @DisplayName("Delete User")
    fun deleteUser() {
        val id = 3
        val expected = true
        facade.deleteUser(id.toULong())
        val actual = transaction {
            UserTable.findById(id) == null
        }

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Delete User, not exist")
    fun deleteUserNotExist() {
        assertThrows<NotExistingUserException> { facade.deleteUser(9u) }
    }

    @Test
    @DisplayName("Get all Users")
    fun getAllUsers() {
        val expected = listOf(
            User(1u, "login1", "password1", true),
            User(2u, "login2", "password2", false),
            User(3u, "login3", "password3", false),
            User(4u, "login4", "password4", false),
            User(5u, "login5", "password5", false)
        )
        val actual = facade.getAllUsers()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get user published recipes")
    fun getUserPublishedRecipes() {
        val id = 1
        val expected = listOf(RecipePreview(id.toULong(), "name1", "desc1", 1u, 1u))
        val actual = facade.getUserPublishedRecipes(id.toULong())

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get user published recipes, not exist")
    fun getUserPublishedRecipesNotExist() {
        assertThrows<NotExistingUserException> { facade.getUserPublishedRecipes(9u) }
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setUser() {
            facade.logIn("login1", "password1")
        }
    }
}

@FlywayTest(DataSource(PGDataSourceProvider::class))
class AccountTest {

    @Test
    @DisplayName("Register")
    fun register() {
        val login = "login6"
        val password = "password6"
        val id = 6

        val expected = User(id.toULong(), login, password, false)
        facade.register(login, password)
        val actual = transaction {
            UserTable.findById(id)?.toEntity() ?: throw NotFoundInDBException("User with id = $id not found")
        }

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Register, not uniq")
    fun registerNotUniq() {
        val login = "login1"
        val password = "password1"

        assertThrows<AlreadyExistingUserException> { facade.register(login, password) }
    }

    @Test
    @DisplayName("Register, didn't passed validation")
    fun registerNotValid() {
        val login = ""
        val password = ""

        assertThrows<ValidationUserException> { facade.register(login, password) }
    }

    @Test
    @DisplayName("LogIn")
    fun logIn() {
        val login = "login1"
        val password = "password1"
        val id = 1

        val actual = facade.logIn(login, password)
        val expected = User(id.toULong(), "login1", "password1", true)

        assertEquals(actual, expected)
    }

    @Test
    @DisplayName("LogIn fail")
    fun logInFail() {
        val login = "login1"
        assertThrows<LogInFailedException> { facade.logIn(login, "asasasas") }
    }

    @Test
    @DisplayName("LogIn, no User")
    fun logInNoUser() {
        assertThrows<NotExistingUserException> { facade.logIn("dsdsa", "asasasas") }
    }
}

@FlywayTest(DataSource(PGDataSourceProvider::class))
class FeedTest {

    @Test
    @DisplayName("Sorted by date feed")
    fun getFeedSortedByDate() {
        val actual = facade.getFeedSortedByDate()
        val expected = listOf(
            RecipePreview(1u, "name1", "desc1", 1u, 1u)
        )

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get saved feed")
    fun getSavedFeed() {
        val id = 4

        val actual = facade.getSavedFeed(id.toULong())
        val expected = listOf(
            RecipePreview(4u, "name4", "desc4", 4u, 4u), RecipePreview(5u, "name5", "desc5", 5u, 5u)
        )

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get saved feed, user not exist")
    fun getSavedFeedUserNotExist() {
        assertThrows<NotExistingUserException> { facade.getSavedFeed(9u) }
    }

    @Test
    @DisplayName("Get own feed")
    fun getOwnFeed() {
        val id = 1

        val actual = facade.getOwnFeed(id.toULong())
        val expected = listOf(RecipePreview(1u, "name1", "desc1", 1u, 1u))

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get own feed, user not exist")
    fun getOwnFeedUserNotExist() {
        assertThrows<NotExistingUserException> { facade.getOwnFeed(9u) }
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setUser() {
            facade.logIn("login1", "password1")
        }
    }
}