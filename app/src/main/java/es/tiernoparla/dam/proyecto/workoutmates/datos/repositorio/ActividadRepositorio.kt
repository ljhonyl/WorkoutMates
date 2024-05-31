package es.tiernoparla.dam.proyecto.workoutmates.datos.repositorio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.dao.ActividadDAO
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.ActividadEntidad
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.aActividadEntidad
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Actividad
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.aDominio
import java.util.Date
import javax.inject.Inject

class ActividadRepositorio @Inject constructor(private val actividadDAO: ActividadDAO) {
    private val _contadorPasos = MutableLiveData<Int>()
    val contadorPasos: LiveData<Int> get() = _contadorPasos

    private val _distanciaRecorrida = MutableLiveData<Double>()
    val distanciaRecorrida: LiveData<Double> get() = _distanciaRecorrida

    fun actualizarContadorPasos(pasos: Int) {
        _contadorPasos.value = pasos
    }

    fun actualizarDistanciaRecorrida(distancia: Double) {
        _distanciaRecorrida.value = distancia
    }

    suspend fun insertarActividad(actividadEntidad: ActividadEntidad) {
        actividadDAO.insertar(actividadEntidad)
    }

    suspend fun actualizarActividad(actividad: Actividad) {
        val actividadEntidad=actividad.aActividadEntidad()
        actividadDAO.actualizarActividadPorFecha(
            actividadEntidad.pasos,
            actividadEntidad.kilometros,
            actividadEntidad.calorias,
            actividadEntidad.fecha
        )
    }

    suspend fun obtenerActividades(): List<Actividad> {
        return actividadDAO.obtenerActividades().map { it.aDominio() }
    }

    suspend fun obtenerActividadPorFecha(fecha: String): Actividad? {
        return actividadDAO.obtenerActividadPorFecha(fecha)?.aDominio()
    }
}