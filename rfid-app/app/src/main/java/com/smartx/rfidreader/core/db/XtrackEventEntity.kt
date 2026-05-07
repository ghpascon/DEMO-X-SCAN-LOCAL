package com.smartx.rfidreader.core.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Evento específico da integração Xtrack.
 *
 * Tipos:
 *  - "change_location"   → movimentação de objetos; sincroniza via API MoveLocation do Xtrack
 *  - "location_inventory"→ inventário de local; sincroniza via URL de webhook
 *
 * Para "change_location", tagsJson é um JSON array:
 *   [{"epc":"...","idcode":"...","description":"..."}, ...]
 *
 * Para "location_inventory", tagsJson é um JSON object:
 *   {"location_id":"...","location_name":"...","total":N,"found":N,
 *    "found_tags":[...],"missing_tags":[...]}
 */
@Entity(tableName = "xtrack_events")
data class XtrackEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val deviceId: String,
    val eventType: String,
    val locationId: String,
    val locationName: String,
    val tagsJson: String,
    val savedAt: String,
    val isSynced: Boolean = false,
    val syncedAt: String = ""
) {
    val tagCount: Int
        get() = try {
            when {
                tagsJson.trimStart().startsWith("[") -> org.json.JSONArray(tagsJson).length()
                else -> org.json.JSONObject(tagsJson).optInt("found", 0)
            }
        } catch (_: Exception) { 0 }
}
