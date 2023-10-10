package da

import bl.entities.*
import bl.exceptions.*
import com.radcortez.flyway.test.annotation.DataSource
import com.radcortez.flyway.test.annotation.FlywayTest
import com.radcortez.flyway.test.junit.DataSourceInfo
import com.radcortez.flyway.test.junit.DataSourceProvider
import da.dao.*
import da.data.builder.RecipeDataBuilder
import da.data.mother.CommentDataMother
import da.data.mother.IngredientDataMother
import da.data.mother.StageDataMother
import da.data.mother.UserDataMother
import da.exeption.NotFoundException
import da.repositories.factory.PgRepositoryFactory
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.flow.combineTransform
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtensionContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

val dotenv = dotenv()
val factory = PgRepositoryFactory(dotenv["TEST_SCHEMA"])

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
@TestMethodOrder(MethodOrderer.Random::class)
class CommentTest {

    private val repository = factory.createCommentRepository()

    @Test
    @DisplayName("Create comment")
    fun createComment() {
        val expected = CommentDataMother.newComment()
        val rId = 1

        val cId = repository.create(expected.autor.id, expected.text, rId)
        val actual1 = transaction {
            CommentTable.findById(cId)?.toEntity()
        }
        val actual2 = transaction {
            RecipeTable.findById(rId)?.toEntity()?.comments?.last()
                ?: throw NotFoundException("Recipe with id = $id not found")
        }
        expected.date = actual2.date

        assertAll("Add comment asserts", { assertEquals(expected, actual1) }, { assertEquals(expected, actual2) })
    }

    @Test
    @DisplayName("Update Comment")
    fun updateComment() {
        val expected = CommentDataMother.updatedComment()

        repository.update(expected)
        val actual = transaction {
            CommentTable.findById(expected.id)?.toEntity()
                ?: throw NotFoundException("Comment with id = $id not found")
        }
        expected.date = actual.date

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update Comment, not found")
    fun updateCommentNotFound() {
        val tmp = CommentDataMother.notExistComment()

        assertThrows<NotFoundException> { repository.update(tmp) }
    }

    @Test
    @DisplayName("Delete Comment")
    fun deleteComment() {
        val id = 5

        repository.delete(id)

        assertNull(transaction { CommentTable.findById(id) })
    }

    @Test
    @DisplayName("Delete Comment, not exist")
    fun deleteCommentNotExist() {
        val id = 9

        assertThrows<NotFoundException> { repository.delete(id) }
    }

    @Test
    @DisplayName("Update text")
    fun updateText() {
        val expected = CommentDataMother.regularCommentWithNewText()

        val actual = repository.updateText(expected.id, expected.text)
        expected.date = actual.date

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update text, not found")
    fun updateTextNotFound() {
        val id = 9

        assertThrows<NotFoundException> { repository.updateText(id, "text") }
    }

    @Test
    @DisplayName("Get owner id")
    fun getOwnerID() {
        val expected = 1
        val id = 1

        val actual = repository.getOwnerID(id)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get owner id, not found")
    fun getOwnerIDNotFound() {
        val id = 9

        assertThrows<NotFoundException> { repository.getOwnerID(id) }
    }
}

@FlywayTest(DataSource(PGDataSourceProvider::class))
@TestMethodOrder(MethodOrderer.Random::class)
class IngredientTest {

    private val repository = factory.createIngredientRepository()

    @Test
    @DisplayName("Create Ingredient")
    fun createIngredient() {
        val expected = IngredientDataMother.newIngredient()

        val iId = repository.create(expected)
        val actual = transaction {
            IngredientTable.findById(iId)?.toEntity()
        }
        expected.id = iId

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update Ingredient")
    fun updateIngredient() {
        val expected = IngredientDataMother.updatedIngredient()

        repository.update(expected)
        val actual = transaction {
            IngredientTable.findById(expected.id)?.toEntity()
                ?: throw NotFoundException("Ingredient with id = $id not found")
        }

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update Ingredient, not found")
    fun updateIngredientNotFound() {
        val tmp = IngredientDataMother.notExistIngredient()

        assertThrows<NotFoundException> { repository.update(tmp) }
    }

    @Test
    @DisplayName("Delete Ingredient")
    fun deleteIngredient() {
        val id = 5

        repository.delete(id)

        assertNull(transaction { IngredientTable.findById(id) })
    }

    @Test
    @DisplayName("Delete Ingredient, not exist")
    fun deleteCommentNotExist() {
        val id = 9

        assertThrows<NotFoundException> { repository.delete(id) }
    }

    @Test
    @DisplayName("Find by name Ingredient")
    fun findByNameIngredient() {
        val expected = IngredientDataMother.regularIngredient()

        val actual = repository.findByName(expected.name)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Find bu name Ingredient, not exist")
    fun findByNameIngredientNotExist() {
        val name = "no_name"

        assertThrows<NotFoundException> { repository.findByName(name) }
    }

    @Test
    @DisplayName("Get all Ingredients")
    fun getAllIngredients() {
        val expected = IngredientDataMother.allIngredients()

        val actual = repository.getAll()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Is name not exist, exist")
    fun isNameNotExistExist() {
        val name = IngredientDataMother.regularIngredient().name

        val actual = repository.isNameNotExist(name)

        assertFalse(actual)

    }

    @Test
    @DisplayName("Is name not exist, not exist")
    fun isNameNotExistNotExist() {
        val name = "no name"

        val actual = repository.isNameNotExist(name)

        assertTrue(actual)
    }
}


@FlywayTest(DataSource(PGDataSourceProvider::class))
@TestMethodOrder(MethodOrderer.Random::class)
//@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class RecipeTest {

    private val repository = factory.createRecipeRepository()

    @Test
    @DisplayName("Create Recipe")
    fun createRecipe() {
        val expected =
            RecipeDataBuilder().withId(6).withName("new").withState(RecipeState.CLOSE).withComments(listOf()).build()

        val rId = repository.create(expected)

        val actual = transaction {
            RecipeTable.findById(rId)?.toEntity() ?: throw NotFoundException("Recipe with id = $id not found")
        }
        expected.id = rId
        expected.date = actual.date
        for (i in actual.stages.indices) {
            expected.stages[i].id = actual.stages[i].id
        }

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get Recipe")
    fun getRecipe() {
        val expected = RecipeDataBuilder().build()

        repository.read(expected.id)
        val actual = transaction {
            RecipeTable.findById(expected.id)?.toEntity()
                ?: throw NotFoundException("Recipe with id = $id not found")
        }

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get Recipe, not found")
    fun getRecipeNotFound() {
        val id = 9

        assertThrows<NotFoundException> { repository.read(id) }
    }

    @Test
    @DisplayName("Update Recipe")
    fun updateRecipe() {
        val expected = RecipeDataBuilder().withName("new").withState(RecipeState.PUBLISHED).build()

        repository.update(expected)
        val actual = transaction {
            RecipeTable.findById(expected.id)?.toEntity()
                ?: throw NotFoundException("Recipe with id = $id not found")
        }
        for (i in actual.stages.indices) {
            expected.stages[i].id = actual.stages[i].id
        }

        assertEquals(expected, actual)
    }


    @Test
    @DisplayName("Update Recipe, not found")
    fun updateRecipeNotFound() {
        val tmp = RecipeDataBuilder().withId(9).build()

        assertThrows<NotFoundException> { repository.update(tmp) }
    }

    @Test
    @DisplayName("Delete Recipe")
    fun deleteRecipe() {
        val id = 5

        repository.delete(id)

        assertNull(transaction { RecipeTable.findById(id) })
    }

    @Test
    @DisplayName("Delete Recipe, not exist")
    fun deleteCommentNotExist() {
        val id = 9

        assertThrows<NotFoundException> { repository.delete(id) }
    }

//    @Test
//    @DisplayName("Get all")
//    fun getAll() {
//        val expected = listOf(
//            RecipeDataBuilder().build(),
//            RecipeDataBuilder()
//                .withId(1)
//                .withName("name1")
//                .withDesc("desc1")
//                .withTime(1)
//                .withServNum(1)
//                .withPFC(PFC(1, 1, 1))
//                .withDate(LocalDateTime.parse("01-01-2023", DateTimeFormatter.ofPattern("MM-dd-yyyy")))
//                .withState(RecipeState.READY_TO_PUBLISH)
//                .withStages(listOf())
//                .withComments(listOf())
//                .withOwner()
//                .withIngredients(listOf())
//
//        )
//
//        assertThrows<NotFoundException> { repository.delete(id) }
//    }

    @Test
    @DisplayName("Update info")
    fun updateInfo() {
        val expected =
            RecipeDataBuilder().withId(1).withName("new_name").withDesc("new_desc").withTime(1).withServNum(1)
                .withPFC(PFC(1, 1, 1)).withState(RecipeState.READY_TO_PUBLISH).build()

        val actual1 = repository.updateInfo(expected)
        val actual2 = transaction {
            RecipeTable.findById(expected.id)?.toEntity()
                ?: throw NotFoundException("Recipe with id = $id not found")
        }

        assertEquals(expected, actual1)
        assertEquals(expected, actual2)
    }

    @Test
    @DisplayName("Update info, not found")
    fun updateInfoNotFound() {
        val tmp = RecipeDataBuilder().withId(9).build()

        assertThrows<NotFoundException> { repository.updateInfo(tmp) }
    }

    @Test
    @DisplayName("Update stages")
    fun updateStages() {
        val expected = RecipeDataBuilder().withStages(listOf(StageDataMother.updatedStage())).build()

        val actual1 = repository.updateStages(expected.id, expected.stages)
        val actual2 = transaction {
            RecipeTable.findById(expected.id)?.toEntity()
                ?: throw NotFoundException("Recipe with id = $id not found")
        }

        assertEquals(expected, actual1)
        assertEquals(expected, actual2)
    }

    @Test
    @DisplayName("Update stages, not found")
    fun updateStagesNotFound() {
        val id = 9

        assertThrows<NotFoundException> { repository.updateStages(id, listOf(StageDataMother.updatedStage())) }
    }

    @Test
    @DisplayName("Get owner id")
    fun getOwnerID() {
        val expected = 1

        val actual = repository.getOwnerID(1)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get publish queue")
    fun getPublishQueue() {
        val expected = listOf(RecipeDataBuilder().build().toRecipePreview())

//        try {
        val actual = repository.getPublishQueue()
//        } catch (e: Exception) {
//            println("")
//        }


        assertEquals(expected, actual)
    }


}

@FlywayTest(DataSource(PGDataSourceProvider::class))
class UserTest {

    private val repository = factory.createUserRepository()

    @Test
    @DisplayName("Create user")
    fun createUser() {
        val expected = UserDataMother.created()

        val uid = repository.create(expected.login, expected.password)
        val actual = transaction {
            UserTable.findById(uid)?.toEntity() ?: throw NotFoundException("User with id = $id not found")
        }
        expected.id = uid

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get User")
    fun getUser() {
        val expected = UserDataMother.admin()

        val actual = repository.read(expected.id)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Get User, not exist")
    fun getUserNotExist() {
        val id = 9

        assertThrows<NotFoundException> { repository.read(id) }
    }

    @Test
    @DisplayName("Update User")
    fun updateUser() {
        val expected = UserDataMother.updated()

        repository.update(expected)
        val actual = transaction {
            UserTable.findById(expected.id)?.toEntity() ?: throw NotFoundException("User with id = $id not found")
        }

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update User, not exist")
    fun updateUserNotExist() {
        val tmp = UserDataMother.notExist()

        assertThrows<NotFoundException> { repository.update(tmp) }
    }

    @Test
    @DisplayName("Delete User")
    fun deleteUser() {
        val id = 5

        repository.delete(id)

        val ent = EntityID(id, Users)
        assertTrue(transaction { CommentTable.find { Comments.autor eq ent }.empty() })
        assertTrue(transaction { RecipeTable.find { Recipes.owner eq ent }.empty() })
        assertTrue(transaction { SavedRecipeTable.find { SavedRecipes.user eq ent }.empty() })
    }

    @Test
    @DisplayName("Delete User, not exist")
    fun deleteUserNotExist() {
        val id = 9

        assertThrows<NotFoundException> { repository.delete(id) }
    }

    @Test
    @DisplayName("Get all Users")
    fun getAllUsers() {
        val expected = UserDataMother.all()

        val actual = repository.getAll()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Update credentials")
    fun updateCredentials() {
        val expected = UserDataMother.updated()

        val actual1 = repository.updateCredentials(expected.id, expected.login, expected.password)
        val actual2 = transaction {
            UserTable.findById(expected.id)?.toEntity() ?: throw NotFoundException("User with id = $id not found")
        }

        assertEquals(expected, actual1)
        assertEquals(expected, actual2)
    }

//    @Test
//    @DisplayName("Update credentials, not found")
//    fun updateCredentialsNotFound() {
//        val tmp = UserDataMother.notExist()
//
//        assertThrows<NotFoundException> { repository.updateCredentials(tmp.id, tmp.login, tmp.password) }
//    }

//    @Test
//    @DisplayName("Update credentials, not found")
//    fun addToFavorite() {
//
//
//        assertThrows<NotFoundException> { repository.updateCredentials(tmp.id, tmp.login, tmp.password) }
//    }
//
//    @Test
//    @DisplayName("Get user published recipes")
//    fun getUserPublishedRecipes() {
//        val id = 1
//        val expected = listOf(RecipePreview(id.toULong(), "name1", "desc1", 1u, 1u))
//        val actual = facade.getUserPublishedRecipes(id.toULong())
//
//        assertEquals(expected, actual)
//    }
//
//    @Test
//    @DisplayName("Get user published recipes, not exist")
//    fun getUserPublishedRecipesNotExist() {
//        assertThrows<NotExistingUserException> { facade.getUserPublishedRecipes(9u) }
//    }
//
//    companion object {
//        @JvmStatic
//        @BeforeAll
//        fun setUser() {
//            facade.logIn("login1", "password1")
//        }
//    }
}
