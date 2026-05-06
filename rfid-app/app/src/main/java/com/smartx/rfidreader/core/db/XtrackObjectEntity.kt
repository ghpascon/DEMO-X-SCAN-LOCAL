package com.smartx.rfidreader.core.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Representa uma identificação (tag/código) associada a um objeto do Xtrack.
 *
 * O EPC (chave primária) vem do campo IDCODE do comando GetIdentification — é o
 * identificador real lido pelo leitor RFID.
 * Um mesmo objeto pode ter múltiplos registros (um por EPC diferente) — nunca
 * sobrescreve: cada EPC é único na tabela.
 *
 * Campos:
 *  - epc          → IDCODE do GetIdentification (EPC real da tag)
 *  - objectId     → OBJECT_ID do GetIdentification (referencia o ID do GetObject)
 *  - idcode       → IDCODE do GetObject (código interno do objeto)
 *  - description  → DESCRIPTION do GetObject
 *  - locationId   → LOCATION_ID do GetObject
 *  - active       → ACTIVE do GetObject
 *  - lastSeen, homeLocationId, lastModified, lastLocation → campos extras do GetObject
 */
@Entity(
    tableName = "xtrack_objects",
    indices = [
        Index(value = ["epc"], unique = true),
        Index(value = ["objectId"]),
        Index(value = ["locationId"]),
        Index(value = ["homeLocationId"])
    ]
)
data class XtrackObjectEntity(
    @PrimaryKey val epc: String,
    val objectId: String = "",
    val idcode: String = "",
    val description: String = "",
    val active: String = "",
    val locationId: String = "",
    val lastSeen: String = "",
    val homeLocationId: String = "",
    val lastModified: String = "",
    val lastLocation: String = "",
    val syncedAt: String = ""
)
