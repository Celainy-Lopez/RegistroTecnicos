package edu.ucne.registrotecnico.presentation.Mensajes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.PrimaryKey
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnico.data.local.entities.MensajeEntity
import edu.ucne.registrotecnico.data.repository.MensajesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import kotlin.String

@HiltViewModel
class MensajesViewModel @Inject constructor(
    private val mensajesRepository: MensajesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(MensajeUiState(
        ticketId = 0
    ))
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: MensajeEvent){
        when(event) {
            is MensajeEvent.MensajeChange -> onMensajeIdChange(event.mensajeId)
            is MensajeEvent.FechaChange -> onFechaChange(event.fecha)
            is MensajeEvent.ContenidoChange -> onContenidoChange(event.contenido)
            is MensajeEvent.RemitenteChange -> onRemitenteChange(event.remitente)
            is MensajeEvent.TipoRemitenteChange -> onTipoRemitenteChange(event.tipoRemitente)
            is MensajeEvent.TicketChange -> onTicketIdChange(event.ticketId)

            MensajeEvent.Save -> saveMensaje()
            MensajeEvent.New -> nuevo()
            MensajeEvent.Delete -> deleteMensaje()
        }
    }

    private fun saveMensaje() {
        viewModelScope.launch {
            if (_uiState.value.contenido.isNullOrBlank()){
                _uiState.update {
                    it.copy(errorMessage = "Campo vacio!!!")
                }
            }
            else{
                mensajesRepository.save(_uiState.value.toEntity())
                getMensajes(_uiState.value.ticketId ?: 0) //
                _uiState.update {
                    it.copy(contenido = "")
                }
            }
        }
    }


    fun getMensajes(ticketId: Int) {
        viewModelScope.launch {
            mensajesRepository.getAll(ticketId).collect { mensajes ->
                _uiState.update {
                    it.copy(mensajes = mensajes)
                }
            }
        }
    }

    private fun nuevo(){
        _uiState.update {
            it.copy(
                mensajeId = null,
                fecha = Date(),
                contenido = "",
                remitente = "",
                ticketId = 0,
            )
        }
    }

    private fun deleteMensaje() {
        viewModelScope.launch {
            mensajesRepository.delete(_uiState.value.toEntity())
        }
    }


    private fun onFechaChange(fecha: Date) {
        _uiState.update {
            it.copy(fecha = fecha)
        }
    }

    private fun onMensajeIdChange(id: Int) {
        _uiState.update {
            it.copy(mensajeId = id)
        }
    }

    private fun onContenidoChange(contenido: String){
        _uiState.update {
            it.copy(contenido = contenido)
        }
    }

    private fun onRemitenteChange(remitente: String){
        _uiState.update {
            it.copy(remitente = remitente)
        }
    }

    private fun onTipoRemitenteChange(tipoRemitente: String){
        _uiState.update {
            it.copy(tipoRemitente = tipoRemitente)
        }
    }

    private fun onTicketIdChange(ticketId: Int) {
        _uiState.update {
            it.copy(ticketId = ticketId)
        }
        getMensajes(ticketId)
    }
}

fun MensajeUiState.toEntity() = MensajeEntity(
    mensajeId = mensajeId,
    fecha = fecha ?: Date(),
    contenido = contenido ?: "",
    remitente = remitente ?: "",
    tipoRemitente = tipoRemitente ?: "",
    ticketId = ticketId ?: 0
)