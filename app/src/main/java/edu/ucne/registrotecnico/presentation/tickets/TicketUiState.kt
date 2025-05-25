package edu.ucne.registrotecnico.presentation.tickets

import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import java.util.Date

data class TicketUiState(
    val ticketId: Int? = null,
    var fecha: Date = Date(),
    val prioridadId: Int = 0,
    val cliente: String = "",
    val asunto: String = "",
    val descripcion: String = "",
    val tecnicoId: Int = 0,
    val errorMessage: String? = null,
    val tickets: List<TicketEntity> = emptyList(),
    val prioridades: List<PrioridadEntity> = emptyList(),
    val tecnicos: List<TecnicoEntity> = emptyList(),

    )