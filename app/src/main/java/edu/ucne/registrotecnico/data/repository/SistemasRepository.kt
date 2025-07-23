package edu.ucne.registrotecnico.data.repository

import edu.ucne.registrotecnico.data.local.dao.SistemaDao
import edu.ucne.registrotecnico.data.local.entities.SistemaEntity
import edu.ucne.registrotecnico.data.remote.RemoteDataSource
import edu.ucne.registrotecnico.data.remote.Resource
import edu.ucne.registrotecnico.data.remote.dto.SistemaDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class SistemasRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val sistemaDao: SistemaDao
) {

    fun getSistemas(): Flow<Resource<List<SistemaDto>>> = flow {
        var sistemasDto: List<SistemaEntity> = emptyList()
        try {
            emit(Resource.Loading())
            val sistemas = remoteDataSource.getSistemas()
            val sistemasEntity = sistemas.map {
                it.toEntity()
            }
            sistemaDao.save(sistemasEntity)
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            //emit(Resource.Error("Error desconocido: ${e.message}"))
        }
        sistemasDto = sistemaDao.getAll()
        val listSistemaDto = sistemasDto.map {
            it.toDto()
        }
        emit(Resource.Success(listSistemaDto))
    }

    suspend fun saveSistema(sistemaDto: SistemaDto) = remoteDataSource.saveSistema(sistemaDto)

    suspend fun editSistema(sistemaDto: SistemaDto) = remoteDataSource.updateSistema(sistemaDto)

    suspend fun deleteSistema(id: Int) = remoteDataSource.deleteSistema(id)

    fun getSistema(sistemaId: Int): Flow<Resource<List<SistemaDto>>> = flow {
        try {
            emit(Resource.Loading())
            val sistema = remoteDataSource.getSistema(sistemaId)
            emit(Resource.Success(sistema))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.message}"))
        }
    }


    private fun SistemaDto.toEntity() = SistemaEntity(
        sistemaId = sistemaId,
        nombre = nombre ?: "",
        descripcion = descripcion?: "",
        costo = costo ?: 0.0
    )

    private fun SistemaEntity.toDto() = SistemaDto(
        sistemaId = sistemaId,
        nombre = nombre ?: "",
        descripcion = descripcion?: "",
        costo = costo ?: 0.0
    )
}