package edu.ucne.registrotecnico.presentation.usuarios

sealed interface UsuarioEvent {
    data class UsuarioChange(val usuarioId: Int): UsuarioEvent
    data class NombreChange(val nombre: String): UsuarioEvent
    data class BalanceChange(val balance: Double ): UsuarioEvent

    data object GetUsuarios: UsuarioEvent
    data object PostUsuario: UsuarioEvent
    data object Nuevo: UsuarioEvent
    data class GetUsuario(val id: Int): UsuarioEvent

    data object CleanErrorMessageNombre: UsuarioEvent
    data object CleanErrorMessageBalance: UsuarioEvent
    data object ResetSuccessMessage: UsuarioEvent
}