package es.tiernoparla.dam.proyecto.workoutmates.dominio.casosdeuso

import es.tiernoparla.dam.proyecto.workoutmates.datos.repositorio.ObjetivoRepositorio
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Objetivos
import javax.inject.Inject

class RegistrarObjetivo @Inject constructor(private val objetivoRepositorio: ObjetivoRepositorio){
    suspend fun insertar(objetivos: Objetivos, fecha: String){
        objetivoRepositorio.guardarObjetivos(objetivos,fecha)
    }

    suspend fun actualizarPasos(pasos: Int, fecha: String){
        objetivoRepositorio.actualizarPasos(pasos, fecha)
    }

    suspend fun actualizarKilometros(kilometros: Double, fecha: String){
        objetivoRepositorio.actualizarKilometros(kilometros, fecha)
    }

    suspend fun actualizarCalorias(calorias: Double, fecha: String){
        objetivoRepositorio.actualizarCalorias(calorias, fecha)
    }
}