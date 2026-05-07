package com.smartx.rfidreader.core.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        EventEntity::class,
        XtrackObjectEntity::class,
        XtrackLocationEntity::class,
        XtrackEventEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
    abstract fun xtrackObjectDao(): XtrackObjectDao
    abstract fun xtrackLocationDao(): XtrackLocationDao
    abstract fun xtrackEventDao(): XtrackEventDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rfid_reader.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
