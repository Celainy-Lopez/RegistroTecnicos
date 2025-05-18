package edu.ucne.registrotecnico.presentation.prioridades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.repository.PrioridadesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PrioridadesViewModel(
    private val prioridadesRepository: PrioridadesRepository
) : ViewModel() {
    fun savePrioridad(prioridad: PrioridadEntity) {
        viewModelScope.launch {
            prioridadesRepository.save(prioridad)
        }
    }

    suspend fun findPrioridad(id: Int): PrioridadEntity? {
        return prioridadesRepository.find(id)
    }

    fun deletePrioridad(prioridad: PrioridadEntity) {
        viewModelScope.launch {
            prioridadesRepository.delete(prioridad)
        }
    }

    val prioridades: StateFlow<List<PrioridadEntity>> = prioridadesRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}