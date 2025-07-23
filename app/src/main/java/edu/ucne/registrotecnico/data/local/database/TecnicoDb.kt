package edu.ucne.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import edu.ucne.registrotecnico.data.local.dao.MensajeDao
import edu.ucne.registrotecnico.data.local.dao.PrioridadDao
import edu.ucne.registrotecnico.data.local.dao.SistemaDao
import edu.ucne.registrotecnico.data.local.dao.TecnicoDao
import edu.ucne.registrotecnico.data.local.dao.TicketDao
import edu.ucne.registrotecnico.data.local.dao.UsuarioDao
import edu.ucne.registrotecnico.data.local.entities.MensajeEntity
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.data.local.entities.SistemaEntity
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import edu.ucne.registrotecnico.data.local.entities.UsuarioEntity
import java.util.Date


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

@Database(
    entities = [
        TecnicoEntity::class,
        PrioridadEntity:: class,
        TicketEntity:: class,
        MensajeEntity:: class,
        SistemaEntity:: class,
        UsuarioEntity::class
    ],
    version = 8,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class TecnicoDb : RoomDatabase() {
    abstract fun TecnicoDao(): TecnicoDao
    abstract fun PrioridadDao(): PrioridadDao
    abstract fun TicketDao(): TicketDao
    abstract fun MensajeDao() : MensajeDao
    abstract fun SistemaDao(): SistemaDao
    abstract fun UsuarioDao(): UsuarioDao
}