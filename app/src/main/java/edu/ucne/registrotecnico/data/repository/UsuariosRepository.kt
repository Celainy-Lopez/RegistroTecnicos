package edu.ucne.registrotecnico.data.repository

import edu.ucne.registrotecnico.data.local.dao.UsuarioDao
import edu.ucne.registrotecnico.data.local.entities.UsuarioEntity
import edu.ucne.registrotecnico.data.remote.RemoteDataSourceUsuarios
import edu.ucne.registrotecnico.data.remote.Resource
import edu.ucne.registrotecnico.data.remote.dto.UsuarioDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class UsuariosRepository @Inject constructor(
    private val remoteDataSourceUsuarios: RemoteDataSourceUsuarios,
    private val usuarioDao: UsuarioDao
) {

    fun getUsuarios(): Flow<Resource<List<UsuarioDto>>> = flow {
        var usuariosDto: List<UsuarioEntity> = emptyList()
        try {
            emit(Resource.Loading())
            val usuarios = remoteDataSourceUsuarios.getUsuarios()
            val usuariosEntity = usuarios.map {
                it.toEntity()
            }
            usuarioDao.save(usuariosEntity)
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
        }
        usuariosDto = usuarioDao.getAll()
        val listUsuarioDto = usuariosDto.map {
            it.toDto()
        }
        emit(Resource.Success(listUsuarioDto))
    }

    suspend fun saveUsuario(usuarioDto: UsuarioDto) = remoteDataSourceUsuarios.saveUsuario(usuarioDto)

    suspend fun editUsuario(usuarioDto: UsuarioDto) = remoteDataSourceUsuarios.updateUsuario(usuarioDto)

    suspend fun deleteUsuario(id: Int) = remoteDataSourceUsuarios.deleteUsuario(id)

    fun getUsuario(usuarioId: Int): Flow<Resource<List<UsuarioDto>>> = flow {
        try {
            emit(Resource.Loading())
            val usuario = remoteDataSourceUsuarios.getUsuario(usuarioId)
            emit(Resource.Success(usuario))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.message}"))
        }
    }


    private fun UsuarioDto.toEntity() = UsuarioEntity(
        usuarioId = usuarioId,
        nombre = nombre ?: "",
        balance = balance ?: 0.0
    )

    private fun UsuarioEntity.toDto() = UsuarioDto(
        usuarioId = usuarioId,
        nombre = nombre ?: "",
        balance = balance ?: 0.0
    )
}