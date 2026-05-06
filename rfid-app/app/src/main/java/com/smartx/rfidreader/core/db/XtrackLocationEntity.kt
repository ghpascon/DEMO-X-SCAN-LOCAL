package com.smartx.rfidreader.core.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Localização cadastrada no Xtrack.
 * Índice por name facilita buscas textuais futuras.
 */
@Entity(
    tableName = "xtrack_locations",
    indices = [Index(value = ["name"])]
)
data class XtrackLocationEntity(
    @PrimaryKey val id: String,
    val name: String = ""
)
