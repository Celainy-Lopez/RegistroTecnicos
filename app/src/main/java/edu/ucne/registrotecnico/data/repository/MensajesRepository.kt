package edu.ucne.registrotecnico.data.repository

import edu.ucne.registrotecnico.data.local.dao.MensajeDao
import edu.ucne.registrotecnico.data.local.entities.MensajeEntity
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MensajesRepository @Inject constructor(
    private val dao: MensajeDao
) {
    suspend fun save(mensaje : MensajeEntity) = dao.save(mensaje)

    suspend fun find(id: Int): MensajeEntity? = dao.find(id)

    suspend fun delete(mensaje: MensajeEntity) = dao.delete(mensaje)

    fun getAll(id:Int) : Flow<List<MensajeEntity>> = dao.getAll(id)
}