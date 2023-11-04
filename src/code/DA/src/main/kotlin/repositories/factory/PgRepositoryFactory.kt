package da.repositories.factory

import bl.repositories.factory.IRepositoryFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import da.repositories.PgCommentRepository
import da.repositories.PgIngredientRepository
import da.repositories.PgRecipeRepository
import da.repositories.PgUserRepository
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory

class PgRepositoryFactory(private val schemaRep: String) : IRepositoryFactory {

    val logger = LoggerFactory.getLogger("mainLogger")

    init {
        val DB_CONNECT = System.getenv("DB_CONNECT") ?: throw Exception("no DB_CONNECT in env")
        val DB_HOST = System.getenv("DB_HOST") ?: throw Exception("no DB_HOST in env")
        println("DB_HOST: $DB_HOST")
        val DB_PORT = System.getenv("DB_PORT") ?: throw Exception("no DB_PORT in env")
        val DB_NAME = System.getenv("DB_NAME") ?: throw Exception("no DB_NAME in env")

        val hikariConfig = HikariConfig().apply {
            jdbcUrl = "jdbc:$DB_CONNECT://$DB_HOST:$DB_PORT/$DB_NAME?currentSchema=$schemaRep"
            driverClassName = "org.postgresql.Driver"
            username = System.getenv("DB_USER") ?: throw Exception("no DB_USER in env")
            password = System.getenv("DB_PASSWORD") ?: throw Exception("no DB_PASSWORD in env")
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