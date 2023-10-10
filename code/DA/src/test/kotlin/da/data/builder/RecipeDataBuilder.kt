package da.data.builder

import bl.entities.*
import da.data.mother.CommentDataMother
import da.data.mother.IngredientInStageDataMother
import da.data.mother.StageDataMother
import da.data.mother.UserDataMother
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecipeDataBuilder {
    private var recipe = Recipe(
        1,
        "name1",
        "desc1",
        1,
        1,
        PFC(1, 1, 1),
        LocalDateTime.parse("2023-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
        RecipeState.PUBLISHED,
        listOf(StageDataMother.regularStage()),
        listOf(CommentDataMother.regularComment()),
        UserDataMother.admin(),
        listOf(IngredientInStageDataMother.regularIngredient())
    )

    fun withId(id: Int): RecipeDataBuilder {
        this.recipe.id = id
        return this;
    }

    fun withName(name: String): RecipeDataBuilder {
        this.recipe.name = name
        return this;
    }

    fun withDesc(desc: String): RecipeDataBuilder {
        this.recipe.description = desc
        return this;
    }

    fun withTime(time: Int): RecipeDataBuilder {
        this.recipe.time = time
        return this;
    }

    fun withServNum(servNum: Int): RecipeDataBuilder {
        this.recipe.servingsNum = servNum
        return this;
    }

    fun withPFC(pfc: PFC): RecipeDataBuilder {
        this.recipe.pfc = pfc
        return this;
    }

    fun withDate(date: LocalDateTime): RecipeDataBuilder {
        this.recipe.date = date
        return this;
    }

    fun withState(state: RecipeState): RecipeDataBuilder {
        this.recipe.state = state
        return this;
    }

    fun withStages(stages: List<Stage>): RecipeDataBuilder {
        this.recipe.stages = stages
        return this;
    }

    fun withComments(comments: List<Comment>): RecipeDataBuilder {
        this.recipe.comments = comments
        return this;
    }

    fun withOwner(user: User): RecipeDataBuilder {
        this.recipe.owner = user
        return this;
    }

    fun withIngredients(ingredientsInStage: List<IngredientInStage>): RecipeDataBuilder {
        this.recipe.ingredients = ingredientsInStage
        return this;
    }

    fun build(): Recipe {
        return recipe
    }

}