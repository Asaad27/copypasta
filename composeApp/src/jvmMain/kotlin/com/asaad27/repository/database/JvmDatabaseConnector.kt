package com.asaad27.repository.database

import com.asaad27.repository.IDatabaseConnector
import org.jetbrains.exposed.sql.Database
import java.util.Properties

class JvmDatabaseConnector: IDatabaseConnector {
    private val properties: Properties by lazy {
        loadProperties()
    }

    private val uri: String by lazy {
        properties.getProperty("db.url", "jdbc:sqlite::memory:clipboard")
    }

    private val driver: String by lazy {
        properties.getProperty("db.driver", "org.sqlite.JDBC")
    }

    private val user: String by lazy {
        properties.getProperty("db.user", "root")
    }

    private val password: String by lazy {
        properties.getProperty("db.password", "")
    }

    override fun connect() {
        try {
            Database.connect(uri, driver = driver, user = user, password = password)
        } catch (e: Exception) {
            throw Exception("Failed to connect to the database", e)
        }
    }

    private fun loadProperties(): Properties {
        val properties = Properties()
        javaClass.classLoader?.getResourceAsStream("dbconfig.properties")?.use { inputStream ->
            properties.load(inputStream)
        }
        return properties
    }

    private fun decryptPassword(encryptedPassword: String): String {
        TODO("Not yet implemented")

    }
}