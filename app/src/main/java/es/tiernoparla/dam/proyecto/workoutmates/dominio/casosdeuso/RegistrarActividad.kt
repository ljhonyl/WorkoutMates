package es.tiernoparla.dam.proyecto.workoutmates.dominio.casosdeuso

import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.firebase.ActividadFirebase
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.aActividadEntidad
import es.tiernoparla.dam.proyecto.workoutmates.datos.repositorio.ActividadRepositorio
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Actividad
import javax.inject.Inject

class RegistrarActividad @Inject constructor(private val actividadRepositorio: ActividadRepositorio) {
    suspend fun registrar(actividad: Actividad) {
        actividadRepositorio.insertarActividad(actividad.aActividadEntidad())
    }

    suspend fun actualizar(actividad: Actividad){
        actividadRepositorio.actualizarActividad(actividad)
    }

    fun sincronizarConFirebase(idNumero: String, actividad: Actividad){
        val actividadFirebase=ActividadFirebase(actividad.pasos,actividad.kilometros,actividad.calorias)
        actividadRepositorio.sincronizarConFirebase(idNumero,actividadFirebase,actividad.fecha)
    }
}