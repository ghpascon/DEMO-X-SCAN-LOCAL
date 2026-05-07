package com.smartx.rfidreader.core.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface XtrackEventDao {

    @Query("SELECT * FROM xtrack_events ORDER BY savedAt DESC")
    fun allFlow(): Flow<List<XtrackEventEntity>>

    @Query("SELECT * FROM xtrack_events WHERE isSynced = 0 ORDER BY savedAt ASC")
    suspend fun pending(): List<XtrackEventEntity>

    @Query("SELECT COUNT(*) FROM xtrack_events WHERE isSynced = 0")
    fun pendingCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM xtrack_events")
    fun totalCountFlow(): Flow<Int>

    /**
     * Busca o último inventário de local não sincronizado para um dado locationId.
     */
    @Query("""
        SELECT * FROM xtrack_events
        WHERE eventType = 'location_inventory'
          AND isSynced = 0
          AND locationId = :locationId
        ORDER BY savedAt DESC
        LIMIT 1
    """)
    suspend fun findPendingLocationInventory(locationId: String): XtrackEventEntity?

    @Insert
    suspend fun insert(event: XtrackEventEntity): Long

    @Query("SELECT * FROM xtrack_events WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): XtrackEventEntity?

    @Update
    suspend fun update(event: XtrackEventEntity)

    @Delete
    suspend fun delete(event: XtrackEventEntity)

    @Query("DELETE FROM xtrack_events")
    suspend fun deleteAll()
}
