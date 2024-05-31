package es.tiernoparla.dam.proyecto.workoutmates.datos.repositorio

import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.dao.ObjetivoDAO
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.aObjetivosEntidad
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Objetivos
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.aDominio
import javax.inject.Inject

class ObjetivoRepositorio @Inject constructor(private val objetivoDAO: ObjetivoDAO) {
    suspend fun guardarObjetivos(objetivo: Objetivos, fecha: String) {
        objetivoDAO.insertar(objetivo.aObjetivosEntidad(fecha))
    }

    suspend fun obtenerObjetivos(fecha: String): Objetivos? {
       return objetivoDAO.obtenerObjetivosPorFecha(fecha)?.aDominio()
    }

    suspend fun actualizarPasos(pasos: Int, fecha: String) {
        objetivoDAO.actualizarPasos(pasos, fecha)
    }

    suspend fun actualizarKilometros(kilometros: Double, fecha: String) {
        objetivoDAO.actualizarKilometros(kilometros, fecha)
    }

    suspend fun actualizarCalorias(calorias: Double, fecha: String) {
        objetivoDAO.actualizarCalorias(calorias, fecha)
    }
}