package edu.ucne.registrotecnico.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrotecnico.data.local.entities.SistemaEntity

@Dao
interface SistemaDao{
    @Upsert
    suspend fun save(sistema: List<SistemaEntity>)

    @Query("""
        SELECT *
            FROM Sistemas
            WHERE sistemaId =:id
            limit 1
    """)
    suspend fun find(id: Int) : SistemaEntity?

    @Delete
    suspend fun delete(sistema: SistemaEntity)

    @Query("SELECT * FROM Sistemas")
    suspend fun getAll(): List<SistemaEntity>
}