package es.tiernoparla.dam.proyecto.workoutmates.dominio.casosdeuso

import es.tiernoparla.dam.proyecto.workoutmates.datos.repositorio.ObjetivoRepositorio
import javax.inject.Inject

class ConsultarObjetivo @Inject constructor(private val objetivoRepositorio: ObjetivoRepositorio) {
    suspend fun getObjetivo(fecha: String){
        objetivoRepositorio.obtenerObjetivos(fecha)
    }
}