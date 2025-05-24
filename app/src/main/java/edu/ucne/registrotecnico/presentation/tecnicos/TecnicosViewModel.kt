package edu.ucne.registrotecnico.presentation.tecnicos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.repository.TecnicosRepository
import edu.ucne.registrotecnico.presentation.prioridades.PrioridadUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TecnicosViewModel @Inject constructor(
    private val tecnicosRepository: TecnicosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TecnicoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTecnicos()
    }

    fun onEvent(event: TecnicoEvent) {
        when (event) {
            is TecnicoEvent.TecnicoChange -> onTecnicoIdChange(event.tecnicoId)
            is TecnicoEvent.NombresChange -> onNombresChange((event.nombres))
            is TecnicoEvent.SueldoChange -> onSueldoChange(event.sueldo)
            TecnicoEvent.Save -> saveTecnico()
            TecnicoEvent.New -> nuevo()
            TecnicoEvent.Delete -> deleteTecnico()
        }
    }

    private fun saveTecnico() {
        viewModelScope.launch {
            if (_uiState.value.nombres.isNullOrBlank() && _uiState.value.sueldo <= 0.0) {
                _uiState.update {
                    it.copy(errorMessage = "Campos vacios")
                }
            } else {
                tecnicosRepository.save(_uiState.value.toEntity())
            }
        }
    }

    private fun nuevo() {
        _uiState.update {
            it.copy(
                tecnicoId = null,
                nombres = "",
                sueldo = 0.0,
                errorMessage = null
            )
        }
    }


    fun findTecnico(tecnicoId: Int) {
        viewModelScope.launch {
            if (tecnicoId > 0) {
                val tecnico = tecnicosRepository.find(tecnicoId)
                _uiState.update {
                    it.copy(
                        tecnicoId = tecnico?.tecnicoId,
                        nombres = tecnico?.nombres ?: "",
                        sueldo = tecnico?.sueldo ?: 0.0
                    )
                }
            }
        }
    }


    fun deleteTecnico() {
        viewModelScope.launch {
            tecnicosRepository.delete(_uiState.value.toEntity())
        }
    }

    val tecnicos: StateFlow<List<TecnicoEntity>> = tecnicosRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private fun getTecnicos() {
        viewModelScope.launch {
            tecnicosRepository.getAll().collect { tecnicos ->
                _uiState.update {
                    it.copy(tecnicos = tecnicos)
                }
            }
        }
    }

    private fun onTecnicoIdChange(tecnicoId: Int) {
        _uiState.update {
            it.copy(tecnicoId = tecnicoId)
        }
    }


    private fun onNombresChange(nombres: String) {
        _uiState.update {
            it.copy(nombres = nombres)
        }
    }

    private fun onSueldoChange(sueldo: Double) {
        _uiState.update {
            it.copy(sueldo = sueldo)
        }
    }


    fun TecnicoUiState.toEntity() = TecnicoEntity(
        tecnicoId = tecnicoId,
        nombres = nombres,
        sueldo = sueldo
    )

}