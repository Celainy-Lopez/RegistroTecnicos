package edu.ucne.registrotecnico.data.remote

import edu.ucne.registrotecnico.data.remote.dto.UsuarioDto
import javax.inject.Inject

class RemoteDataSourceUsuarios @Inject constructor(
    private val usuarioinApi: UsuarioinApi
){
    suspend fun getUsuarios()= usuarioinApi.getUsuarios()

    suspend fun updateUsuario(usuarioDto: UsuarioDto)= usuarioinApi.updateUsuario(usuarioDto)

    suspend fun saveUsuario(usuarioDto: UsuarioDto)= usuarioinApi.saveUsuario(usuarioDto)

    suspend fun deleteUsuario(id: Int)= usuarioinApi.deleteUsuario(id)

    suspend fun getUsuario(id: Int)= usuarioinApi.getUsuario(id)
}