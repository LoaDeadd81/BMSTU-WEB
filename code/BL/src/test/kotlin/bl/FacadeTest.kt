package bl

import bl.entities.*
import bl.exceptions.*
import bl.repository.*
import bl.repository.factory.MockRepositoryFactory
import org.junit.jupiter.api.*
import java.time.LocalDateTime
import kotlin.test.assertEquals

val facade = Facade(MockRepositoryFactory())

class CommentTest {

    @Test
    @DisplayName("Update Comment")
    fun updateComment() {
        val expected = CommentMockData[1]
        expected.text = "fixed1"
        facade.updateComment(expected)
        val actual = CommentMockData[1]

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
        val expected = "deleted"
        facade.deleteComment(2u)
        val actual = CommentMockData[2].text

        assertEquals(expected, actual)
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
            facade.logIn("login0", "password0")
        }
    }
}

class IngredientTest {

    @Test
    @DisplayName("Create Ingredient")
    fun createIngredient() {
        val expected = Ingredient(5u, "name5", IngredientType.MEAT, 987u)
        facade.createIngredient(expected)
        val actual = IngredientMockData[5]

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Create Ingredient, not uniq")
    fun createIngredientNotUniq() {
        val tmp = Ingredient(5u, "name0", IngredientType.MEAT, 987u)

        assertThrows<AlreadyExistingIngredientException> { facade.createIngredient(tmp) }
    }

    @Test
    @DisplayName("Create Ingredient, didn't passed validation")
    fun createNotValidIngredient() {
        val tmp = Ingredient(5u, "", IngredientType.MEAT, 987u)

        assertThrows<ValidationIngredientException> { facade.createIngredient(tmp) }
    }

    @Test
    @DisplayName("Update Ingredient")
    fun updateIngredient() {
        val expected = IngredientMockData[1]
        expected.name = "fixed1"
        facade.updateIngredient(expected)
        val actual = IngredientMockData[1]

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update Ingredient, not exist")
    fun updateIngredientNotExist() {
        val tmp = Ingredient(9u, "name4", IngredientType.MEAT, 987u)

        assertThrows<NotExistingIngredientException> { facade.updateIngredient(tmp) }
    }

    @Test
    @DisplayName("Update Ingredient, didn't passed validation")
    fun updateNotValidIngredient() {
        val tmp = Ingredient(2u, "", IngredientType.MEAT, 987u)

        assertThrows<ValidationIngredientException> { facade.updateIngredient(tmp) }
    }

    @Test
    @DisplayName("Delete Ingredient")
    fun deleteIngredient() {
        val expected = "deleted"
        facade.deleteIngredient(2u)
        val actual = IngredientMockData[2].name

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Delete Ingredient, not exist")
    fun deleteIngredientNotExist() {
        assertThrows<NotExistingIngredientException> { facade.deleteIngredient(9u) }
    }

    @Test
    @DisplayName("Find bu name Ingredient")
    fun findByNameIngredient() {
        val expected = IngredientMockData[0]
        val actual = facade.findByNameIngredient(IngredientMockData[0].name)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Find bu name Ingredient, not exist")
    fun findByNameIngredientNotExist() {
        assertThrows<NotExistingIngredientException> { facade.findByNameIngredient("name9") }
    }

    @Test
    @DisplayName("Get all Ingredients")
    fun getAllIngredients() {
        val expected = IngredientMockData.toList()
        val actual = facade.getAllIngredients()

        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setUser() {
            facade.logIn("login0", "password0")
        }
    }
}

class RecipeTest {

    @Test
    @DisplayName("Create Recipe")
    fun createRecipe() {
        val expected = Recipe(
            5u,
            "name5",
            "description5",
            35u,
            5u,
            PFC(5, 5, 5),
            LocalDateTime.now(),
            false,
            StageMockData.toList(),
            CommentMockData.toList(),
            UserMockData[4],
            IngredientInStageMockData.toList()
        )
        facade.createRecipe(expected)
        val actual = RecipeMockData[6]

        assertEquals(expected, actual)
    }

    @Test
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
            StageMockData.toList(),
            CommentMockData.toList(),
            UserMockData[4],
            IngredientInStageMockData.toList()
        )

        assertThrows<ValidationRecipeException> { facade.createRecipe(tmp) }
    }

    @Test
    @DisplayName("Get Recipe")
    fun getRecipe() {
        val actual = facade.getRecipe(0u)
        val expected = RecipeMockData[0]

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get Recipe, not exist")
    fun getRecipeNotExist() {
        assertThrows<NotExistingRecipeException> { facade.getRecipe(9u) }
    }

    @Test
    @DisplayName("Update Recipe")
    fun updateRecipe() {
        val expected = RecipeMockData[1]
        expected.name = "fixed1"
        facade.updateRecipe(expected)
        val actual = RecipeMockData[1]

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update Recipe, not exist")
    fun updateRecipeNotExist() {
        val tmp = Recipe(
            9u,
            "",
            "description9",
            39u,
            9u,
            PFC(9, 9, 9),
            LocalDateTime.now(),
            false,
            StageMockData.toList(),
            CommentMockData.toList(),
            UserMockData[4],
            IngredientInStageMockData.toList()
        )

        assertThrows<NotExistingRecipeException> { facade.updateRecipe(tmp) }
    }

    @Test
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
            StageMockData.toList(),
            CommentMockData.toList(),
            UserMockData[4],
            IngredientInStageMockData.toList()
        )

        assertThrows<ValidationRecipeException> { facade.updateRecipe(tmp) }
    }

    @Test
    @DisplayName("Delete Recipe")
    fun deleteRecipe() {
        val expected = "deleted"
        facade.deleteRecipe(2u)
        val actual = RecipeMockData[2].name

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Delete Recipe, not exist")
    fun deleteRecipeNotExist() {
        assertThrows<NotExistingRecipeException> { facade.deleteRecipe(9u) }
    }

    @Test
    @DisplayName("Add to favorite")
    fun addToFavorite() {
        val expected = Pair<ULong, ULong>(3u, 3u)
        facade.addToFavorite(expected.first, expected.second)
        val actual = SavedRecipesMockData[5]

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Add to favorite, no recipe")
    fun addToFavoriteNoRecipe() {
        assertThrows<NotExistingRecipeException> { facade.addToFavorite(4u, 9u) }
    }

    @Test
    @DisplayName("Add to favorite, no user")
    fun addToFavoriteNoUser() {
        assertThrows<NotExistingUserException> { facade.addToFavorite(9u, 4u) }
    }

    @Test
    @DisplayName("Add comment")
    fun addComment() {
        val expected = Comment(0u, LocalDateTime.now(), "text5", UserMockData[1])
        facade.addComment(1u, "text5", 1u)
        val actual1 = CommentMockData[5]
        val actual2 = RecipeMockData[1].comments[5]

        expected.date = actual1.date
        assertAll("Add comment asserts", { assertEquals(expected, actual1) }, { assertEquals(expected, actual2) })
    }

    @Test
    @DisplayName("Add comment, no user")
    fun addCommentNoUser() {
        assertThrows<NotExistingUserException> { facade.addComment(9u, "text5", 1u) }
    }

    @Test
    @DisplayName("Add comment, no recipe")
    fun addCommentNoRecipe() {
        assertThrows<NotExistingRecipeException> { facade.addComment(1u, "text5", 9u) }
    }

    @Test
    @DisplayName("Add comment, not valid")
    fun addNotValidComment() {
        assertThrows<ValidationCommentException> { facade.addComment(1u, "", 1u) }
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setUser() {
            facade.logIn("login0", "password0")
        }
    }
}

class PublishTest {

    @Test
    @DisplayName("Get publish queue")
    fun getPublishQueue() {
        val expected = facade.getPublishQueue()
        val actual = RecipeMockData.filter { it.published }.map { it.toRecipePreview() }

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Add to publish queue")
    fun addToPublishQueue() {
        val expected = true
        facade.addToPublishQueue(4u)
        val actual = PublishQueue.find { x -> x.toUInt() == 4u } != null

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
        assertThrows<RecipeAlreadyPublishedException> { facade.addToPublishQueue(0u) }
    }

    @Test
    @DisplayName("Accept recipe publication")
    fun acceptRecipePublication() {
        val expected = true
        facade.addToPublishQueue(3u)
        facade.acceptRecipePublication(3u)
        val actual = RecipeMockData[3].published

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
        val expected = true
        facade.cancelRecipePublication(8u)
        val actual = PublishQueue.find { x -> x.toUInt() == 8u } == null

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
            facade.logIn("login0", "password0")
        }
    }
}

class UserTest {

    @Test
    @DisplayName("Get User")
    fun getUser() {
        val expected = UserMockData[0]
        val actual = facade.getUser(0u)

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
        val expected = UserMockData[1]
        expected.password = "fixed1"
        facade.updateUser(expected)
        val actual = UserMockData[1]

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
        val expected = "deleted"
        facade.deleteUser(2u)
        val actual = UserMockData[2].password

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
        val expected = UserMockData.toList()
        val actual = facade.getAllUsers()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get user published recipes")
    fun getUserPublishedRecipes() {
        val expected =
            RecipeMockData.filter { x -> x.owner.id.toInt() == 0 && x.published }.map { it.toRecipePreview() }
        val actual = facade.getUserPublishedRecipes(0u)

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
            facade.logIn("login0", "password0")
        }
    }
}

class AccountTest {
    @Test
    @DisplayName("Register")
    fun register() {
        val login = "login5"
        val password = "password5"

        val expected = User(0u, login, password, false)
        facade.register(login, password)
        val actual = UserMockData.find { it.login == login }

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Register, not uniq")
    fun registerNotUniq() {
        val login = "login0"
        val password = "password0"

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
        val res = facade.logIn(UserMockData[0].login, UserMockData[0].password)

        assertEquals(UserMockData[0], res)
    }

    @Test
    @DisplayName("LogIn fail")
    fun logInFail() {
        assertThrows<LogInFailedException> { facade.logIn(UserMockData[0].login, "asasasas") }
    }

    @Test
    @DisplayName("LogIn, no User")
    fun logInNoUser() {
        assertThrows<NotExistingUserException> { facade.logIn("dsdsa", "asasasas") }
    }
}

class FeedTest {

    @Test
    @DisplayName("Sorted by date feed")
    fun getFeedSortedByDate() {
        val actual = facade.getFeedSortedByDate()
        val expected = RecipeMockData.sortedBy { it.date }.map { it.toRecipePreview() }.toList()

        assertEquals(expected, actual)
    }


    @Test
    @DisplayName("Get saved feed")
    fun getSavedFeed() {
        val actual = facade.getSavedFeed(0u)
        val lst = SavedRecipesMockData.filter { x -> x.first.toUInt() == 0u }.map { x -> x.first }
        val expected = RecipeMockData.filter { x -> x.id in lst }.map { x -> x.toRecipePreview() }

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
        val actual = facade.getOwnFeed(0u)
        val expected = RecipeMockData.filter { x -> x.owner.id.toUInt() == 0u }.map { x -> x.toRecipePreview() }

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
            facade.logIn("login0", "password0")
        }
    }
}