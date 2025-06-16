package edu.ucne.registrotecnico.presentation.sistema

import edu.ucne.registrotecnico.data.remote.Resource
import edu.ucne.registrotecnico.data.remote.dto.SistemaDto
import edu.ucne.registrotecnico.data.repository.SistemasRepository
import edu.ucne.registrotecnico.presentation.UiEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnico.presentation.prioridades.PrioridadEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SistemasViewModel @Inject constructor(
    private val sistemasRepository: SistemasRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SistemaUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getSistemas()
    }

    fun onEvent(event: SistemaEvent) {
        when (event) {
            is SistemaEvent.SistemaChange -> onSistemaIdChange(event.sistemaId)
            is SistemaEvent.NombreChange -> onNombreChange(event.nombre)
            is SistemaEvent.DescripcionChange -> onDescripcionChange(event.descripcion)
            is SistemaEvent.CostoChange -> onCostoChange(event.costo)

            SistemaEvent.GetSistemas -> getSistemas()
            SistemaEvent.PostSistema -> addSistema()
            SistemaEvent.Nuevo -> nuevo()
            SistemaEvent.CleanErrorMessageNombre -> CleanErrorMessageNombre()
            SistemaEvent.CleanErrorMessageDescripcion -> CleanErrorMessageDescripcion()
            SistemaEvent.CleanErrorMessageCosto -> CleanErrorMessageCosto()

            SistemaEvent.ResetSuccessMessage -> _uiState.update {
                it.copy(
                    isSuccess = false,
                    successMessage = null
                )
            }

            is SistemaEvent.GetSistema -> findSistema(event.id)
        }
    }

    private fun addSistema() {
        viewModelScope.launch {
            var error = false

            if (_uiState.value.nombre.isNullOrBlank()) {
                _uiState.update {
                    it.copy(errorNombre = "Campo obligatorio*")
                }
                error = true
            }

            if (_uiState.value.descripcion.isNullOrBlank()) {
                _uiState.update {
                    it.copy(errorDescripcion = "Campo obligatorio*")
                }
                error = true
            }

            if (_uiState.value.costo <= 0.0) {
                _uiState.update {
                    it.copy(errorCosto = "Ingrese un valor mayor que 0*")
                }
                error = true
            }

            if (_uiState.value.costo.toString().isNullOrBlank()) {
                _uiState.update {
                    it.copy(errorCosto = "Campo obligatorio*")
                }
                error = true
            }
            if (error) return@launch
            try {
                sistemasRepository.saveSistema(_uiState.value.toEntity())
                _uiState.update {
                    it.copy(
                        isSuccess = true,
                        successMessage = "Sistema guardado exitosamente",
                        errorMessage = null
                    )
                }

                getSistemas()
                nuevo()

                delay(3000)
                _uiEvent.send(UiEvent.NavigateUp)
            } catch (e: retrofit2.HttpException) {
                if (e.code() == 500) {
                    _uiState.update {
                        it.copy(
                            isSuccess = true,
                            successMessage = "Sistema guardado, sincronización fallida(Err.500).",
                            errorMessage = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Error en la API: ${e.code()} - ${e.message}",
                            isSuccess = false
                        )
                    }
                    return@launch
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al guardar: ${e.localizedMessage}",
                        isSuccess = false
                    )
                }
            }
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }

    private fun getSistemas() {
        viewModelScope.launch {
            sistemasRepository.getSistemas().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                sistemas = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = result.message ?: "Error desconocido",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun nuevo() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    nombre = "",
                    descripcion = "",
                    costo = 0.0,
                    errorNombre = "",
                    errorDescripcion = "",
                    errorCosto = "",
                    errorMessage = "",
                )
            }
        }
    }

    fun findSistema(sistemaId: Int) {
        viewModelScope.launch {
            if (sistemaId > 0) {
                sistemasRepository.getSistema(sistemaId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val sistema = resource.data?.firstOrNull()
                            _uiState.update {
                                it.copy(
                                    sistemaId = sistema?.sistemaId,
                                    nombre = sistema?.nombre ?: "",
                                    descripcion = sistema?.descripcion ?: "",
                                    costo = sistema?.costo ?: 0.0
                                )
                            }
                        }

                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(errorMessage = resource.message)
                            }
                        }

                        is Resource.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                    }
                }
            }
        }
    }

    private fun onSistemaIdChange(id: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(sistemaId = id)
            }
        }
    }

    private fun onNombreChange(nombre: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(nombre = nombre)
            }
        }
    }

    private fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }


    private fun onCostoChange(costo: Double) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(costo = costo)
            }
        }
    }


    private fun CleanErrorMessageNombre() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorNombre = "")
            }
        }
    }

    private fun CleanErrorMessageDescripcion() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorDescripcion = "")
            }
        }
    }

    private fun CleanErrorMessageCosto() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorCosto = "")
            }
        }
    }


    fun SistemaUiState.toEntity() = SistemaDto(
        sistemaId = sistemaId,
        nombre = nombre ?: "",
        descripcion = descripcion ?: "",
        costo = costo ?: 0.0
    )
}