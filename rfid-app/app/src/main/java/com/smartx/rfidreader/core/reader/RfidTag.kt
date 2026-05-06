package com.smartx.rfidreader.core.reader

import java.util.Date

/**
 * Representa uma tag RFID lida pelo leitor.
 *
 * [description] é preenchido em background consultando o banco de dados Xtrack:
 *   - null  → ainda buscando
 *   - ""    → não encontrado no banco Xtrack
 *   - else  → descrição do objeto cadastrado
 */
data class RfidTag(
    val epc: String,
    val rssi: String = "",
    val tid: String = "",
    val user: String = "",
    val antenna: Int = 1,
    val readCount: Int = 1,
    val timestamp: Date = Date(),
    val description: String? = null
)
