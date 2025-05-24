package edu.ucne.registrotecnico.presentation.prioridades

import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity

data class PrioridadUiState (
    val prioridadId: Int? = null,
    val descripcion: String = "",
    val errorMessage: String? = null,
    val prioridades: List<PrioridadEntity> = emptyList()
)
