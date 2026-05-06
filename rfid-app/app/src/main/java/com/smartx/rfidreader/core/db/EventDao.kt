package com.smartx.rfidreader.core.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM rfid_events ORDER BY savedAt DESC")
    fun allFlow(): Flow<List<EventEntity>>

    @Query("SELECT * FROM rfid_events WHERE isSynced = 0 ORDER BY savedAt ASC")
    suspend fun pending(): List<EventEntity>

    @Query("SELECT COUNT(*) FROM rfid_events WHERE isSynced = 0")
    fun pendingCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM rfid_events")
    fun totalCountFlow(): Flow<Int>

    /**
     * Busca o último inventário de local não sincronizado para um dado location_id.
     * O location_id fica embutido no campo tagsJson como "location_id":"<id>".
     */
    @Query("""
        SELECT * FROM rfid_events 
        WHERE eventType = 'location_inventory' 
          AND isSynced = 0
          AND tagsJson LIKE '%"location_id":"' || :locationId || '"%'
        ORDER BY savedAt DESC 
        LIMIT 1
    """)
    suspend fun findPendingLocationInventory(locationId: String): EventEntity?

    @Insert
    suspend fun insert(event: EventEntity): Long

    @Query("SELECT * FROM rfid_events WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): EventEntity?

    @Update
    suspend fun update(event: EventEntity)

    @Delete
    suspend fun delete(event: EventEntity)

    @Query("DELETE FROM rfid_events")
    suspend fun deleteAll()
}
