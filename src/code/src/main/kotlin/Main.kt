package main

import api.RestApi
import bl.managers.CommentManager
import bl.managers.IngredientManager
import bl.managers.RecipeManager
import bl.managers.UserManager
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.FileAppender
import ch.qos.logback.core.util.StatusPrinter
import com.zaxxer.hikari.pool.HikariPool
import da.repositories.factory.PgRepositoryFactory
import org.slf4j.LoggerFactory

fun getLogLevel(level: String): Level {
    return when (level) {
        "ALL" -> Level.ALL
        "TRACE" -> Level.TRACE
        "DEBUG" -> Level.DEBUG
        "WARN" -> Level.WARN
        "ERROR" -> Level.ERROR
        else -> Level.ALL
    }
}

fun configLoggers(level: String) {
    val logLevel = getLogLevel(level)
    val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext

    val fileAppender = FileAppender<ILoggingEvent>()
    fileAppender.context = loggerContext
    fileAppender.name = "mainAppender"
    fileAppender.file = "log/main.log"


    val encoder = PatternLayoutEncoder()
    encoder.context = loggerContext
    encoder.pattern = "%d{yyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"
    encoder.start()

    fileAppender.encoder = encoder
    fileAppender.start()

    val hikariLogger = loggerContext.getLogger("com.zaxxer.hikari") as Logger
    hikariLogger.level = logLevel
    hikariLogger.addAppender(fileAppender)

    val exposedLogger = loggerContext.getLogger("Exposed") as Logger
    exposedLogger.level = logLevel
    exposedLogger.addAppender(fileAppender)

    val mainLogger = loggerContext.getLogger("mainLogger") as Logger
    mainLogger.level = logLevel
    mainLogger.addAppender(fileAppender)

    StatusPrinter.print(loggerContext)

    mainLogger.info("Start logging")
}

fun main() {
    val logger = LoggerFactory.getLogger("mainLogger")

    try {
        configLoggers("ALL")

        val main_shema = System.getenv("MAIN_SCHEMA") ?: throw Exception("no MAIN_SCHEMA in env")
        val repositoryFactory = PgRepositoryFactory(main_shema)
        CommentManager.registerRepository(repositoryFactory.createCommentRepository())
        IngredientManager.registerRepository(repositoryFactory.createIngredientRepository())
        RecipeManager.registerRepository(repositoryFactory.createRecipeRepository())
        UserManager.registerRepository(repositoryFactory.createUserRepository())

        val api = RestApi()
        api.run()

    } catch (e: HikariPool.PoolInitializationException) {
        println("Не удалось подключиться к БД")
        println(e.message)
        logger.error("Failed to connect to the database", e)
    } catch (e: Exception) {
        println("Ошибка")
        println(e.message)
        logger.error("Exception", e)
    }
}