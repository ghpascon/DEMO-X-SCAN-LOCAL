package com.smartx.rfidreader.core.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface XtrackObjectDao {

    @Query("SELECT * FROM xtrack_objects ORDER BY description ASC")
    fun allFlow(): Flow<List<XtrackObjectEntity>>

    /** Lookup O(1) por EPC — case-insensitive pois leitores e servidor podem ter casing diferente. */
    @Query("SELECT * FROM xtrack_objects WHERE epc = :epc COLLATE NOCASE LIMIT 1")
    suspend fun findByEpc(epc: String): XtrackObjectEntity?

    @Query("SELECT * FROM xtrack_objects WHERE locationId = :locationId ORDER BY description ASC")
    suspend fun findByLocation(locationId: String): List<XtrackObjectEntity>

    @Query("SELECT COUNT(*) FROM xtrack_objects")
    suspend fun count(): Int

    /**
     * Query paginada com filtro dinâmico por campo.
     * filterField: nome da coluna SQLite ("description", "epc", "locationId", "active", "lastLocation").
     * Quando filterField ou filterValue são vazios, retorna todos os registros sem filtro.
     */
    @Query("""
        SELECT * FROM xtrack_objects 
        WHERE 
            (:filterField = '' OR :filterValue = '') OR
            (:filterField = 'description' AND description LIKE '%' || :filterValue || '%') OR
            (:filterField = 'epc' AND epc LIKE '%' || :filterValue || '%') OR
            (:filterField = 'locationId' AND locationId LIKE '%' || :filterValue || '%') OR
            (:filterField = 'active' AND active LIKE '%' || :filterValue || '%') OR
            (:filterField = 'lastLocation' AND lastLocation LIKE '%' || :filterValue || '%')
        ORDER BY description ASC 
        LIMIT :limit OFFSET :offset
    """)
    suspend fun queryPaged(filterField: String, filterValue: String, limit: Int, offset: Int): List<XtrackObjectEntity>

    @Query("""
        SELECT COUNT(*) FROM xtrack_objects 
        WHERE 
            (:filterField = '' OR :filterValue = '') OR
            (:filterField = 'description' AND description LIKE '%' || :filterValue || '%') OR
            (:filterField = 'epc' AND epc LIKE '%' || :filterValue || '%') OR
            (:filterField = 'locationId' AND locationId LIKE '%' || :filterValue || '%') OR
            (:filterField = 'active' AND active LIKE '%' || :filterValue || '%') OR
            (:filterField = 'lastLocation' AND lastLocation LIKE '%' || :filterValue || '%')
    """)
    suspend fun countFiltered(filterField: String, filterValue: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(objects: List<XtrackObjectEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: XtrackObjectEntity)

    @Update
    suspend fun update(obj: XtrackObjectEntity)

    @Delete
    suspend fun delete(obj: XtrackObjectEntity)

    @Query("DELETE FROM xtrack_objects")
    suspend fun deleteAll()
}
