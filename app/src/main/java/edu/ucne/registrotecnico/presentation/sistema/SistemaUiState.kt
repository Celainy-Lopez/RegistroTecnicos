package edu.ucne.registrotecnico.presentation.sistema

import edu.ucne.registrotecnico.data.remote.dto.SistemaDto

data class SistemaUiState(
    val sistemaId: Int? = null,
    val nombre: String = "",
    val descripcion: String = "",
    val costo: Double = 0.0,

    val errorMessage: String? = null,
    val errorNombre: String? = null,
    val errorDesripcion: String? = null,
    val errorCosto: String? = null,

    val successMessage: String? = null,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,

    val sistemas: List<SistemaDto> = emptyList()
)