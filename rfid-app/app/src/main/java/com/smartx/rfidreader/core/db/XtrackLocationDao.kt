package com.smartx.rfidreader.core.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface XtrackLocationDao {

    @Query("SELECT * FROM xtrack_locations ORDER BY name ASC")
    fun allFlow(): Flow<List<XtrackLocationEntity>>

    @Query("SELECT * FROM xtrack_locations ORDER BY name ASC")
    suspend fun getAll(): List<XtrackLocationEntity>

    @Query("SELECT * FROM xtrack_locations WHERE id = :id LIMIT 1")
    suspend fun findById(id: String): XtrackLocationEntity?

    @Query("SELECT COUNT(*) FROM xtrack_locations")
    suspend fun count(): Int

    /** Pesquisa por nome com paginação. search vazio retorna todos. */
    @Query("""
        SELECT * FROM xtrack_locations 
        WHERE :search = '' OR name LIKE '%' || :search || '%'
        ORDER BY name ASC 
        LIMIT :limit OFFSET :offset
    """)
    suspend fun searchPaged(search: String, limit: Int, offset: Int): List<XtrackLocationEntity>

    @Query("SELECT COUNT(*) FROM xtrack_locations WHERE :search = '' OR name LIKE '%' || :search || '%'")
    suspend fun countSearch(search: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locations: List<XtrackLocationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: XtrackLocationEntity)

    @Query("DELETE FROM xtrack_locations")
    suspend fun deleteAll()
}
