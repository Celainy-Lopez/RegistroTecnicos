package edu.ucne.registrotecnico.data.remote.dto

data class SistemaDto(
    val sistemaId: Int?,
    val nombre: String,
    val descripcion: String,
    val costo: Double
)