package edu.ucne.registrotecnico.presentation.Mensajes

import edu.ucne.registrotecnico.data.local.entities.MensajeEntity
import java.util.Date

data class MensajeUiState (
    val mensajeId: Int? = null,
    val fecha: Date = Date(),
    val contenido: String = "",
    val remitente : String =  "",
    val tipoRemitente: String? = null,
    val ticketId: Int,
    val errorMessage: String? = null,
    val mensajes: List<MensajeEntity> = emptyList()
)