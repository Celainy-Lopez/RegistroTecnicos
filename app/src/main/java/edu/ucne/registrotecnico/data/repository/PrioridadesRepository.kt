package edu.ucne.registrotecnico.data.repository

import edu.ucne.registrotecnico.data.local.dao.PrioridadDao
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PrioridadesRepository @Inject constructor(
    private val dao: PrioridadDao
) {
    suspend fun save(prioridad: PrioridadEntity) = dao.save(prioridad)

    suspend fun find(id: Int): PrioridadEntity? = dao.find(id)

    suspend fun delete(prioridad: PrioridadEntity) = dao.delete(prioridad)

    fun getAll(): Flow<List<PrioridadEntity>> = dao.getAll()
}