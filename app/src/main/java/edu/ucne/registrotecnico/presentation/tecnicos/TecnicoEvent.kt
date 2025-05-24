package edu.ucne.registrotecnico.presentation.tecnicos

sealed interface TecnicoEvent {
    data class TecnicoChange(val tecnicoId: Int) : TecnicoEvent
    data class  NombresChange(val nombres: String) : TecnicoEvent
    data class SueldoChange(val sueldo: Double) : TecnicoEvent
    data object Save: TecnicoEvent
    data object Delete: TecnicoEvent
    data object New: TecnicoEvent
}