//package bl.repository
//
//import bl.entities.*
//import java.time.LocalDateTime
//
//var UserMockData: Array<User> = arrayOf(
//    User(0u, "login0", "password0", true),
//    User(1u, "login1", "password1", false),
//    User(2u, "login2", "password2", false),
//    User(3u, "login3", "password3", false),
//    User(4u, "login4", "password4", false)
//)
//
//var CommentMockData: Array<Comment> = arrayOf(
//    Comment(0u, LocalDateTime.now(), "text0", UserMockData[0]),
//    Comment(1u, LocalDateTime.now(), "text1", UserMockData[1]),
//    Comment(2u, LocalDateTime.now(), "text2", UserMockData[2]),
//    Comment(3u, LocalDateTime.now(), "text3", UserMockData[3]),
//    Comment(4u, LocalDateTime.now(), "text4", UserMockData[4])
//)
//
//var IngredientInStageMockData: Array<IngredientInStage> = arrayOf(
//    IngredientInStage(0u, "name0", IngredientType.MEAT, 987u, 789u, ProcessingType.WASH),
//    IngredientInStage(1u, "name1", IngredientType.MEAT, 987u, 789u, ProcessingType.WASH),
//    IngredientInStage(2u, "name2", IngredientType.MEAT, 987u, 789u, ProcessingType.WASH),
//    IngredientInStage(3u, "name3", IngredientType.MEAT, 987u, 789u, ProcessingType.WASH),
//    IngredientInStage(4u, "name4", IngredientType.MEAT, 987u, 789u, ProcessingType.WASH)
//)
//
//var IngredientMockData: Array<Ingredient> = arrayOf(
//    Ingredient(0u, "name0", IngredientType.MEAT, 987u),
//    Ingredient(1u, "name1", IngredientType.MEAT, 987u),
//    Ingredient(2u, "name2", IngredientType.MEAT, 987u),
//    Ingredient(3u, "name3", IngredientType.MEAT, 987u),
//    Ingredient(4u, "name4", IngredientType.MEAT, 987u)
//)
//
//var StageMockData: Array<Stage> = arrayOf(
//    Stage(0u, 30u, "description0", IngredientInStageMockData.toList()),
//    Stage(1u, 31u, "description1", IngredientInStageMockData.toList()),
//    Stage(2u, 32u, "description2", IngredientInStageMockData.toList()),
//    Stage(3u, 33u, "description3", IngredientInStageMockData.toList()),
//    Stage(4u, 34u, "description4", IngredientInStageMockData.toList())
//)
//
//var RecipeMockData: Array<Recipe> = arrayOf(
//    Recipe(
//        0u,
//        "name0",
//        "description0",
//        30u,
//        0u,
//        PFC(0, 0, 0),
//        LocalDateTime.now(),
//        true,
//        StageMockData.toList(),
//        CommentMockData.toList(),
//        UserMockData[0],
//        IngredientInStageMockData.toList()
//    ),
//    Recipe(
//        1u,
//        "name1",
//        "description1",
//        31u,
//        1u,
//        PFC(1, 1, 1),
//        LocalDateTime.now(),
//        false,
//        StageMockData.toList(),
//        CommentMockData.toList(),
//        UserMockData[1],
//        IngredientInStageMockData.toList()
//    ),
//    Recipe(
//        2u,
//        "name2",
//        "description2",
//        32u,
//        2u,
//        PFC(2, 2, 2),
//        LocalDateTime.now(),
//        false,
//        StageMockData.toList(),
//        CommentMockData.toList(),
//        UserMockData[2],
//        IngredientInStageMockData.toList()
//    ),
//    Recipe(
//        3u,
//        "name3",
//        "description3",
//        33u,
//        3u,
//        PFC(3, 3, 3),
//        LocalDateTime.now(),
//        false,
//        StageMockData.toList(),
//        CommentMockData.toList(),
//        UserMockData[3],
//        IngredientInStageMockData.toList()
//    ),
//    Recipe(
//        4u,
//        "name4",
//        "description4",
//        34u,
//        4u,
//        PFC(4, 4, 4),
//        LocalDateTime.now(),
//        false,
//        StageMockData.toList(),
//        CommentMockData.toList(),
//        UserMockData[4],
//        IngredientInStageMockData.toList()
//    ),
//    Recipe(
//        8u,
//        "name8",
//        "description8",
//        38u,
//        8u,
//        PFC(8, 8, 8),
//        LocalDateTime.now(),
//        false,
//        StageMockData.toList(),
//        CommentMockData.toList(),
//        UserMockData[4],
//        IngredientInStageMockData.toList()
//    )
//)
//
//var SavedRecipesMockData: Array<Pair<ULong, ULong>> = arrayOf(
//    Pair(0u, 0u),
//    Pair(0u, 1u),
//    Pair(0u, 2u),
//    Pair(2u, 3u),
//    Pair(4u, 1u)
//)
//
//var PublishQueue: Array<ULong> = arrayOf(1u, 8u)