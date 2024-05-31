package es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.firebase

import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.ActividadEntidad
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Actividad

class ActividadFirebase(
    val pasos: Int,
    val kilometros: Double,
    val calorias: Double
) {
    // Constructor vacío necesario para Firebase Database
    constructor() : this(0, 0.0, 0.0)

    fun aActividadFirebase() = ActividadFirebase(pasos = pasos, kilometros = kilometros, calorias = calorias)
}