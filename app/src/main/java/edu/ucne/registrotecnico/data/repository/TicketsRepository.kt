package edu.ucne.registrotecnico.data.repository

import edu.ucne.registrotecnico.data.local.dao.TicketDao
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import kotlinx.coroutines.flow.Flow

class TicketsRepository(
    private val dao: TicketDao
) {
    suspend fun save(ticket: TicketEntity) = dao.save(ticket)

    suspend fun find(id: Int): TicketEntity? = dao.find(id)

    suspend fun delete(ticket: TicketEntity) = dao.delete(ticket)

    fun getAll(): Flow<List<TicketEntity>> = dao.getAll()
}