package edu.ucne.registrotecnico.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LowPriority
import androidx.compose.material.icons.filled.People
import edu.ucne.registrotecnico.presentation.BottomNavItem
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object TecnicoList : Screen()
    @Serializable
    data object PrioridadList: Screen()
    @Serializable
    data object TicketList: Screen()
    @Serializable
    data object SistemaList: Screen()
    @Serializable
    data object Home: Screen()
    @Serializable
    data object UsuarioList: Screen()

    @Serializable
    data class Tecnico(val tecnicoId: Int?) : Screen()
    @Serializable
    data class Prioridad(val prioridadId: Int?) : Screen()
    @Serializable
    data class Ticket(val ticketId: Int?) : Screen()
    @Serializable
    data class Mensaje(val ticketId: Int?) : Screen()
    @Serializable
    data class Sistema(val sistemaId: Int?) : Screen()
    @Serializable
    data class Usuario(val usuarioId: Int?) : Screen()
}