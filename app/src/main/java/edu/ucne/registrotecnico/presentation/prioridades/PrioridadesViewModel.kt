package edu.ucne.registrotecnico.presentation.prioridades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.repository.PrioridadesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrioridadesViewModel @Inject constructor(
    private val prioridadesRepository: PrioridadesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrioridadUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPrioridades()
    }

    fun onEvent(event: PrioridadEvent) {
        when (event) {
            is PrioridadEvent.PrioridadChange -> onPrioridadIdChange(event.prioridadId)
            is PrioridadEvent.DescripcionChange -> onDescripcionChange(event.descripcion)
            PrioridadEvent.Save -> savePrioridad()
            PrioridadEvent.Delete -> deletePrioridad()
            PrioridadEvent.New -> nuevo()
        }
    }


    private fun savePrioridad() {
        viewModelScope.launch {
            if (_uiState.value.descripcion.isNullOrBlank()){
                _uiState.update {
                    it.copy(errorMessage = "Campos vacios")
                }
            } else{
                prioridadesRepository.save(_uiState.value.toEntity())
            }
        }
    }

    private fun nuevo() {
        _uiState.update {
            it.copy(
                prioridadId = null,
                descripcion = "",
                errorMessage = null
            )
        }
    }


    fun findPrioridad(prioridadId: Int) {
        viewModelScope.launch {
            if (prioridadId > 0) {
                val prioridad = prioridadesRepository.find(prioridadId)
                _uiState.update {
                    it.copy(
                        prioridadId = prioridad?.prioridadId,
                        descripcion = prioridad?.descripcion ?: ""
                    )
                }
            }
        }
    }


    fun deletePrioridad() {
        viewModelScope.launch {
            prioridadesRepository.delete(_uiState.value.toEntity())
        }
    }

    val prioridades: StateFlow<List<PrioridadEntity>> = prioridadesRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private fun getPrioridades() {
        viewModelScope.launch {
            prioridadesRepository.getAll().collect { prioridades ->
                _uiState.update {
                    it.copy(prioridades = prioridades)
                }
            }
        }
    }

    private fun onPrioridadIdChange(prioridadId: Int) {
        _uiState.update {
            it.copy(prioridadId = prioridadId)
        }
    }


    private fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }


    fun PrioridadUiState.toEntity() = PrioridadEntity(
        prioridadId = prioridadId,
        descripcion = descripcion ?: ""
    )

}