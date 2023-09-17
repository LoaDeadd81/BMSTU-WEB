package da.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object PublishQueue : IntIdTable("publish_queue") {
    val recipe = reference("recipeid", Recipes)
}

class PublishQueueTable(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PublishQueueTable>(PublishQueue)

    var recipeId by PublishQueue.recipe

    var recipes by RecipeTable referencedOn PublishQueue.recipe
}