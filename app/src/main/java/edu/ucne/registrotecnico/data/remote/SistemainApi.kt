package edu.ucne.registrotecnico.data.remote

import edu.ucne.registrotecnico.data.remote.dto.SistemaDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SistemainApi {
    @GET("api/Sistemas")
    suspend fun getSistemas(): List<SistemaDto>

    @GET("api/Sistemas/{id}")
    suspend fun getSistema(@Path("id") id: Int): List<SistemaDto>

    @PUT("api/Sistemas/{id}")
    suspend fun updateSistema(@Body sistemaDto: SistemaDto): SistemaDto

    @POST("api/Sistemas")
    suspend fun saveSistema(@Body sistemaDto: SistemaDto): SistemaDto

    @DELETE("api/Sistemas/{id}")
    suspend fun deleteSistema(@Path("id") id: Int): Response<Unit>
}