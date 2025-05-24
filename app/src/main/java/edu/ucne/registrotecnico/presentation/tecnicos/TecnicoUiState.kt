package edu.ucne.registrotecnico.presentation.tecnicos

import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity

data class TecnicoUiState (
    val tecnicoId: Int? = null,
    val nombres: String = "",
    var sueldo: Double = 0.0,
    val errorMessage: String? = null,
    val tecnicos: List<TecnicoEntity> = emptyList()
)