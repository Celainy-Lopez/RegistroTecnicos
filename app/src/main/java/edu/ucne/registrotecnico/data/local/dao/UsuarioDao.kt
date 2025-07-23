package edu.ucne.registrotecnico.data.local.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrotecnico.data.local.entities.UsuarioEntity

@Dao
interface UsuarioDao {
    @Upsert()
    suspend fun save(usuarios: List<UsuarioEntity>)
    @Query(
        """
        SELECT * 
        FROM Usuarios 
        WHERE usuarioId=:id  
        LIMIT 1
        """
    )
    suspend fun find(id: Int): UsuarioEntity?
    @Delete
    suspend fun delete(usuario: UsuarioEntity)
    @Query("SELECT * FROM Usuarios")
    suspend fun getAll(): List<UsuarioEntity>
}