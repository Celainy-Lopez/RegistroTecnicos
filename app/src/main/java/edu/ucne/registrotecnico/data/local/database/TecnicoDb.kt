package edu.ucne.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrotecnico.data.local.dao.TecnicoDao
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity

@Database(
    entities = [
        TecnicoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TecnicoDb : RoomDatabase() {
    abstract fun TecnicoDao(): TecnicoDao
}