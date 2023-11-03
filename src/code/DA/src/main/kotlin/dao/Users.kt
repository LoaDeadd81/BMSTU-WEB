package da.dao

import bl.entities.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable("user") {
    val login = text("login")
    val password = text("password")
    val isAdmin = bool("is_admin").default(false)
}

class UserTable(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserTable>(Users)

    var login by Users.login
    var password by Users.password
    var isAdmin by Users.isAdmin

    val recipesPreview by RecipePreviewTable referrersOn Recipes.owner

    var savedRecipesPreview by RecipePreviewTable via SavedRecipes
}

fun UserTable.toEntity(): User = User(
    id = this.id.value,
    login = this.login,
    password = this.password,
    isAdmin = this.isAdmin
)

