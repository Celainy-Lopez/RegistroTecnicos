package edu.ucne.registrotecnico.presentation.usuarios

import edu.ucne.registrotecnico.data.remote.Resource
import edu.ucne.registrotecnico.presentation.UiEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnico.data.remote.dto.UsuarioDto
import edu.ucne.registrotecnico.data.repository.UsuariosRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuariosViewModel @Inject constructor(
    private val usuariosRepository: UsuariosRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        getUsuarios()
    }

    fun onEvent(event: UsuarioEvent) {
        when (event) {
            is UsuarioEvent.UsuarioChange -> onUsuarioIdChange(event.usuarioId)
            is UsuarioEvent.NombreChange -> onNombreChange(event.nombre)
            is UsuarioEvent.BalanceChange -> onBalanceChange(event.balance)
            is UsuarioEvent.GetUsuario -> findUsuario(event.id)


            UsuarioEvent.GetUsuarios -> getUsuarios()
            UsuarioEvent.PostUsuario -> addUsuario()
            UsuarioEvent.Nuevo -> nuevo()

            UsuarioEvent.CleanErrorMessageNombre -> cleanErrorMessageNombre()
            UsuarioEvent.CleanErrorMessageBalance -> cleanErrorMessageCosto()

            UsuarioEvent.ResetSuccessMessage -> _uiState.update {
                it.copy(
                    isSuccess = false,
                    successMessage = null
                )
            }
        }
    }

    private fun addUsuario() {
        viewModelScope.launch {
            var error = false

            if (_uiState.value.nombre.isNullOrBlank()) {
                _uiState.update {
                    it.copy(errorNombre = "Campo obligatorio*")
                }
                error = true
            }

            if (_uiState.value.balance <= 0.0) {
                _uiState.update {
                    it.copy(errorBalance = "Ingrese un valor mayor que 0*")
                }
                error = true
            }

            if (_uiState.value.balance.toString().isNullOrBlank()) {
                _uiState.update {
                    it.copy(errorBalance = "Campo obligatorio*")
                }
                error = true
            }
            if (error) return@launch
            try {
                usuariosRepository.saveUsuario(_uiState.value.toEntity())
                _uiState.update {
                    it.copy(
                        isSuccess = true,
                        successMessage = "Usuario guardado exitosamente",
                        errorMessage = null
                    )
                }

                getUsuarios()
                nuevo()

                delay(3000)
                _uiEvent.emit(UiEvent.NavigateUp)
            } catch (e: retrofit2.HttpException) {
                if (e.code() == 500) {
                    _uiState.update {
                        it.copy(
                            isSuccess = true,
                            successMessage = "Usuario guardado, sincronización fallida(Err.500).",
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
            _uiEvent.emit(UiEvent.NavigateUp)
        }
    }

    private fun getUsuarios() {
        viewModelScope.launch {
            usuariosRepository.getUsuarios().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                usuarios = result.data ?: emptyList(),
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
                    balance = 0.0,
                    errorNombre = "",
                    errorBalance = "",
                    errorMessage = "",
                )
            }
        }
    }

    fun findUsuario(usuarioId: Int) {
        viewModelScope.launch {
            if (usuarioId > 0) {
                usuariosRepository.getUsuario(usuarioId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val usuario = resource.data?.firstOrNull()
                            _uiState.update {
                                it.copy(
                                    usuarioId = usuario?.usuarioId,
                                    nombre = usuario?.nombre ?: "",
                                    balance = usuario?.balance ?: 0.0
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

    private fun onUsuarioIdChange(id: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(usuarioId = id)
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

    private fun onBalanceChange(balance: Double) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(balance = balance)
            }
        }
    }


    private fun cleanErrorMessageNombre() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorNombre = "")
            }
        }
    }


    private fun cleanErrorMessageCosto() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorBalance = "")
            }
        }
    }


    fun UsuarioUiState.toEntity() = UsuarioDto(
        usuarioId = usuarioId,
        nombre = nombre ?: "",
        balance = balance ?: 0.0
    )
}