package es.tiernoparla.dam.proyecto.workoutmates.dominio.casosdeuso

import es.tiernoparla.dam.proyecto.workoutmates.datos.repositorio.ActividadRepositorio
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Actividad
import javax.inject.Inject

class ConsultarActividad @Inject constructor(private val repositorio: ActividadRepositorio) {
    suspend fun obtenerTodasLasActividades(): List<Actividad> {
        return repositorio.obtenerActividades()
    }

    suspend fun getActividadPorFecha(fecha: String): Actividad? {
        return repositorio.obtenerActividadPorFecha(fecha)
    }
}