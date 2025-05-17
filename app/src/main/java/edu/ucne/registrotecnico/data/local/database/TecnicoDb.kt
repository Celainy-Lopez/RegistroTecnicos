package edu.ucne.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrotecnico.data.local.dao.PrioridadDao
import edu.ucne.registrotecnico.data.local.dao.TecnicoDao
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity

@Database(
    entities = [
        TecnicoEntity::class,
        PrioridadEntity:: class
    ],
    version = 2,
    exportSchema = false
)
abstract class TecnicoDb : RoomDatabase() {
    abstract fun TecnicoDao(): TecnicoDao
    abstract fun PrioridadDao(): PrioridadDao
}