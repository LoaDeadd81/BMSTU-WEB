package api.plugins

import api.dto.requests.comment.StoreComment
import api.dto.requests.comment.UpdateComment
import api.dto.requests.ingredients.IngredientRequest
import api.dto.requests.recipe.*
import api.dto.requests.user.UpdateUser
import api.dto.requests.user.UserCredentials
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.requestvalidation.RequestValidation

fun Application.configureValidation() {
    install(ContentNegotiation) {
        json()
    }
    install(RequestValidation) {
        validate<UserCredentials> { user ->
            if (user.login.isEmpty())
                ValidationResult.Invalid("Login should not be empty")
            else if (user.password.isEmpty())
                ValidationResult.Invalid("Password should not be empty")
            else ValidationResult.Valid
        }
        validate<UpdateUser> { user ->
            if (user.login.isEmpty())
                ValidationResult.Invalid("Login should not be empty")
            else if (user.password.isEmpty())
                ValidationResult.Invalid("Password should not be empty")
            else ValidationResult.Valid
        }

        validate<IngredientRequest> { ingredient ->
            if (ingredient.name.isEmpty())
                ValidationResult.Invalid("Name should not be empty")
            else if (ingredient.nutritionalValue <= 0)
                ValidationResult.Invalid("Nutrition value should be greater or equal 0")
            else ValidationResult.Valid
        }

        validate<StoreRecipe> { recipe ->
            if (recipe.name.isEmpty())
                ValidationResult.Invalid("Name should not be empty")
            else if (recipe.description.isEmpty())
                ValidationResult.Invalid("Description should not be empty")
            else if (recipe.time < 0)
                ValidationResult.Invalid("Time should be greater or equal 0")
            else if (recipe.servingsNum <= 0)
                ValidationResult.Invalid("Serving number should be greater 0")
            else if (recipe.pfc.protein < 0)
                ValidationResult.Invalid("Protein should be greater or equal 0")
            else if (recipe.pfc.fat < 0)
                ValidationResult.Invalid("Fat should be greater or equal 0")
            else if (recipe.pfc.carbon < 0)
                ValidationResult.Invalid("Carbon should be greater or equal 0")
            else if (recipe.stages.isEmpty())
                ValidationResult.Invalid("The recipe must have at least 1 stage")
            else ValidationResult.Valid
        }
        validate<StoreStage> { stage ->
            if (stage.description.isEmpty())
                ValidationResult.Invalid("Description should not be empty")
            else if (stage.time < 0)
                ValidationResult.Invalid("Time should be greater or equal 0")
            else ValidationResult.Valid
        }
        validate<StoreIngredientInStage> { ingredient ->
            if (ingredient.id < 0)
                ValidationResult.Invalid("Ingredient id should not be empty")
            else if (ingredient.amount < 0)
                ValidationResult.Invalid("Amount should be greater than 0")
            else ValidationResult.Valid
        }
        validate<UpdateRecipe> { recipe ->
            if (recipe.name.isEmpty())
                ValidationResult.Invalid("Name should not be empty")
            else if (recipe.description.isEmpty())
                ValidationResult.Invalid("Description should not be empty")
            else if (recipe.time < 0)
                ValidationResult.Invalid("Time should be greater or equal 0")
            else if (recipe.servingsNum <= 0)
                ValidationResult.Invalid("Serving number should be greater 0")
            else if (recipe.pfc.protein < 0)
                ValidationResult.Invalid("Protein should be greater or equal 0")
            else if (recipe.pfc.fat < 0)
                ValidationResult.Invalid("Fat should be greater or equal 0")
            else if (recipe.pfc.carbon < 0)
                ValidationResult.Invalid("Carbon should be greater or equal 0")
            else if (recipe.stages.isEmpty())
                ValidationResult.Invalid("The recipe must have at least 1 stage")
            else if (recipe.owner_id <= 0)
                ValidationResult.Invalid("Owner id should be greater or equal 0")
            else ValidationResult.Valid
        } //todo check validatin
        validate<UpdateRecipeInfo> { recipe ->
            if (recipe.name.isEmpty())
                ValidationResult.Invalid("Name should not be empty")
            else if (recipe.description.isEmpty())
                ValidationResult.Invalid("Description should not be empty")
            else if (recipe.time < 0)
                ValidationResult.Invalid("Time should be greater or equal 0")
            else if (recipe.servingsNum <= 0)
                ValidationResult.Invalid("Serving number should be greater 0")
            else if (recipe.pfc.protein < 0)
                ValidationResult.Invalid("Protein should be greater or equal 0")
            else if (recipe.pfc.fat < 0)
                ValidationResult.Invalid("Fat should be greater or equal 0")
            else if (recipe.pfc.carbon < 0)
                ValidationResult.Invalid("Carbon should be greater or equal 0")
            else ValidationResult.Valid
        }
        validate<UpdateRecipeStages> { recipe ->
            if (recipe.stages.isEmpty())
                ValidationResult.Invalid("The recipe must have at least 1 stage")
            else ValidationResult.Valid
        }

        validate<StoreComment> { recipe ->
            if (recipe.text.isEmpty())
                ValidationResult.Invalid("Text should not be empty")
            else ValidationResult.Valid
        }
        validate<UpdateComment> { recipe ->
            if (recipe.text.isEmpty())
                ValidationResult.Invalid("Text should not be empty")
            else if (recipe.user_id < 1)
                ValidationResult.Invalid("User ID should be greater or equal 1")
            else ValidationResult.Valid
        }
    }
}