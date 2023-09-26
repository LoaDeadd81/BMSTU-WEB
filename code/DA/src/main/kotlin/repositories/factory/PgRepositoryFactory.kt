package da.repositories.factory

import bl.repositories.factory.IRepositoryFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import da.repositories.PgCommentRepository
import da.repositories.PgIngredientRepository
import da.repositories.PgRecipeRepository
import da.repositories.PgUserRepository
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory

class PgRepositoryFactory(private val schemaRep: String) : IRepositoryFactory {

    val logger = LoggerFactory.getLogger("mainLogger")

    init {
        val dotenv = dotenv()
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = "jdbc:" + dotenv["DB_CONNECT"] + "://" + dotenv["DB_HOST"] + ":" +
                    dotenv["DB_PORT"] + "/" + dotenv["DB_NAME"] + "?currentSchema=" + schemaRep
            driverClassName = "org.postgresql.Driver"
            username = dotenv["DB_USER"]
            password = dotenv["DB_PASSWORD"]
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"

            validate()
        }

        Database.connect(HikariDataSource(hikariConfig))

        logger.info("DB with config connected {}", hikariConfig)
    }

    override fun createCommentRepository(): PgCommentRepository = PgCommentRepository()

    override fun createIngredientRepository(): PgIngredientRepository = PgIngredientRepository()

    override fun createRecipeRepository(): PgRecipeRepository = PgRecipeRepository()

    override fun createUserRepository(): PgUserRepository = PgUserRepository()
}