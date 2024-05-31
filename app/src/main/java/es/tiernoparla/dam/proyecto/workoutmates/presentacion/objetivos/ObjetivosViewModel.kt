package es.tiernoparla.dam.proyecto.workoutmates.presentacion.objetivos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.tiernoparla.dam.proyecto.workoutmates.dominio.casosdeuso.ConsultarObjetivo
import es.tiernoparla.dam.proyecto.workoutmates.dominio.casosdeuso.RegistrarObjetivo
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Objetivos
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ObjetivosViewModel @Inject constructor(
    private val registrarObjetivo: RegistrarObjetivo,
    private val consultarObjetivo: ConsultarObjetivo
) : ViewModel() {
    private val formatoFecha = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    fun registrarObjetivos(pasos: Int, kilometros: Double, calorias: Double) {
        val fecha=obtenerFechaActualFormateada()
        viewModelScope.launch {
            registrarObjetivo.insertar(Objetivos(pasos, kilometros, calorias), fecha)
        }
    }

    fun actualizarPasos(pasos: Int){
        viewModelScope.launch {
            val fecha=obtenerFechaActualFormateada()
            val objetivos= consultarObjetivo.getObjetivo(fecha)
            if (objetivos!=null){
                registrarObjetivo.actualizarPasos(pasos,fecha)
            }
            else{
                registrarObjetivo.insertar(Objetivos(pasos,0.0,0.0),fecha)
            }
        }
    }

    fun actualizarKilometros(kilometros: Double){
        viewModelScope.launch {
            val fecha = obtenerFechaActualFormateada()
            val objetivos = consultarObjetivo.getObjetivo(fecha)
            if (objetivos != null){
                registrarObjetivo.actualizarKilometros(kilometros, fecha)
            } else {
                registrarObjetivo.insertar(Objetivos(pasos = 0, kilometros = kilometros, calorias = 0.0), fecha)
            }
        }
    }

    fun actualizarCalorias(calorias: Double){
        viewModelScope.launch {
            val fecha = obtenerFechaActualFormateada()
            val objetivos = consultarObjetivo.getObjetivo(fecha)
            if (objetivos != null){
                registrarObjetivo.actualizarCalorias(calorias, fecha)
            } else {
                registrarObjetivo.insertar(Objetivos(pasos = 0, kilometros = 0.0, calorias = calorias), fecha)
            }
        }
    }

    fun obtenerObjetivos() {
        val fecha=obtenerFechaActualFormateada()
        viewModelScope.launch {
            consultarObjetivo.getObjetivo(fecha)
        }
    }
    private fun obtenerFechaActualFormateada(): String {
        val hoy = Date()
        return formatoFecha.format(hoy)
    }
}