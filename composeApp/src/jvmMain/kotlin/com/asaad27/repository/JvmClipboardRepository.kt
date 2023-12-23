package com.asaad27.repository


import com.asaad27.adapter.toClipboardModel
import com.asaad27.cache.JvmCache
import com.asaad27.cache.strategy.LruStrategy
import com.asaad27.model.ClipboardModel
import com.asaad27.repository.database.ClipboardEntity
import com.asaad27.repository.database.ClipboardTable
import com.asaad27.utils.md5
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction


class JvmClipboardRepository(
    driverManager: IDatabaseConnector
) : IClipboardRepository {

    private val jvmCache: JvmCache<String, ClipboardEntity>

    init {
        driverManager.connect()
        createSchemaIfNotExist()
        jvmCache = JvmCache(LruStrategy())
    }

    override suspend fun save(content: ClipboardModel): ClipboardModel {
        val contentHash = content.fullContent.md5()
        val existingElement = jvmCache.get(contentHash)

        return transaction {
            existingElement?.let {
                it.updatedAt = content.lastUpdated
                return@transaction it
            }

            ClipboardEntity.new {
                this.content = content.fullContent
                createdAt = content.lastUpdated
                updatedAt = content.lastUpdated

            }
        }.also { jvmCache.put(contentHash, it) }.toClipboardModel()
    }

    override suspend fun getAll(): List<ClipboardModel> {
        return transaction {
            ClipboardTable.selectAll()
                .distinct()
                .map {
                    ClipboardEntity.wrapRow(it)
                }
        }.map { it.toClipboardModel() }
    }

    override fun search(query: String): Flow<ClipboardModel> = channelFlow {
        newSuspendedTransaction(Dispatchers.IO) {
            ClipboardTable
                .select { ClipboardTable.content like "%$query%" }
                .forEach { row ->
                    val clipboardEntity = ClipboardEntity.wrapRow(row)
                    send(clipboardEntity.toClipboardModel())
                }
        }
    }

    override suspend fun getClipboardContentByContent(fullContent: String): ClipboardModel? {
        val contentHash = fullContent.md5()
        val existingElement = jvmCache.get(contentHash)
        existingElement?.let {
            return it.toClipboardModel()
        }
        return transaction {
            ClipboardTable
                .select { ClipboardTable.content eq fullContent }
                .map { ClipboardEntity.wrapRow(it) }
                .firstOrNull()
        }?.toClipboardModel()
    }

    private fun createSchemaIfNotExist() {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.createMissingTablesAndColumns(*arrayOf(ClipboardTable))
        }
    }
}
