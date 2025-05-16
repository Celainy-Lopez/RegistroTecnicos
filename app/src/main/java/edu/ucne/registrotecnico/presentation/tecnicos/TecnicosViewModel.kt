package edu.ucne.registrotecnico.presentation.tecnicos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.repository.TecnicosRepository
import kotlinx.coroutines.launch

class TecnicosViewModel(
    private val tecnicosRepository: TecnicosRepository
) : ViewModel() {
    fun saveTecnico(tecnico: TecnicoEntity) {
        viewModelScope.launch {
            tecnicosRepository.save(tecnico)
        }
    }

    suspend fun findTecnico(id: Int): TecnicoEntity? {
        return tecnicosRepository.find(id)
    }

    fun deleteTecnico(tecnico: TecnicoEntity) {
        viewModelScope.launch {
            tecnicosRepository.delete(tecnico)
        }
    }
}