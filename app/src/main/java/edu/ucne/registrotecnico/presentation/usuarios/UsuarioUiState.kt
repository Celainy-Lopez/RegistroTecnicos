package edu.ucne.registrotecnico.presentation.usuarios

import edu.ucne.registrotecnico.data.remote.dto.UsuarioDto


data class UsuarioUiState(
    val usuarioId: Int? = null,
    val nombre: String = "",
    val balance: Double = 0.0,

    val errorMessage: String? = null,
    val errorNombre: String? = null,
    val errorBalance: String? = null,

    val successMessage: String? = null,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,

    val usuarios: List<UsuarioDto> = emptyList()
)