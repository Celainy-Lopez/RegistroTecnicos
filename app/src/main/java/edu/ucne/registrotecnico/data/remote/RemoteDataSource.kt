package edu.ucne.registrotecnico.data.remote

import edu.ucne.registrotecnico.data.remote.dto.SistemaDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val sistemainApi: SistemainApi
){
    suspend fun getSistemas()= sistemainApi.getSistemas()

    suspend fun updateSistema(sistemaDto: SistemaDto)= sistemainApi.updateSistema(sistemaDto)

    suspend fun saveSistema(sistemaDto: SistemaDto)= sistemainApi.saveSistema(sistemaDto)

    suspend fun deleteSistema(id: Int)= sistemainApi.deleteSistema(id)

    suspend fun getSistema(id: Int)= sistemainApi.getSistema(id)
}