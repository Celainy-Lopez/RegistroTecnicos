package edu.ucne.registrotecnico.presentation.sistema

sealed interface SistemaEvent {
    data class SistemaChange(val sistemaId: Int): SistemaEvent
    data class NombreChange(val nombre: String): SistemaEvent
    data class DescripcionChange(val descripcion: String): SistemaEvent
    data class CostoChange(val costo: Double ): SistemaEvent

    data object GetSistemas: SistemaEvent
    data object PostSistema: SistemaEvent
    data object Nuevo: SistemaEvent
    data class GetSistema(val id: Int): SistemaEvent

    data object CleanErrorMessageNombre: SistemaEvent
    data object CleanErrorMessageDescripcion: SistemaEvent
    data object CleanErrorMessageCosto: SistemaEvent
    data object ResetSuccessMessage: SistemaEvent
}