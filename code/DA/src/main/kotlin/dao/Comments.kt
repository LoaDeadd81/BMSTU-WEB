package da.dao

import bl.entities.Comment
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Comments : IntIdTable("comment") {
    val date = datetime("date")
    val text = text("text")
    val autor = reference("autorid", Users)
    val recipe = reference("recipeid", Recipes)
}

class CommentTable(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CommentTable>(Comments)

    var date by Comments.date
    var text by Comments.text
    var autorId by Comments.autor
    var recipeId by Comments.recipe

    var autor by UserTable referencedOn Comments.autor
    var recipe by RecipeTable referencedOn Comments.recipe
}

fun CommentTable.toEntity(): Comment = Comment(
    id = this.id.value.toULong(), date = this.date, text = this.text, autor = this.autor.toEntity()
)