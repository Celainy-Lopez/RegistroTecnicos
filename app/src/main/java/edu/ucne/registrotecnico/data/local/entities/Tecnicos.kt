package edu.ucne.registrotecnico.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tecnicos")

data class TecnicoEntity (
    @PrimaryKey
    val tecnicoId: Int? = null,
    val nombres: String = "",
    val sueldo: Double = 0.0

)