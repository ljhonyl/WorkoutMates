package es.tiernoparla.dam.proyecto.workoutmates.presentacion.objetivos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tiernoparla.dam.proyecto.workoutmates.dominio.casosdeuso.ConsultarActividad
import es.tiernoparla.dam.proyecto.workoutmates.dominio.casosdeuso.ConsultarObjetivo
import es.tiernoparla.dam.proyecto.workoutmates.dominio.casosdeuso.RegistrarObjetivo
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Actividad
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Objetivos
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ObjetivosViewModel @Inject constructor(
    private val registrarObjetivo: RegistrarObjetivo,
    private val consultarObjetivo: ConsultarObjetivo,
    private val consultarActividad: ConsultarActividad
) : ViewModel() {
    private val _objetivos = MutableLiveData<Objetivos>()
    val objetivos: LiveData<Objetivos> get() = _objetivos

    lateinit var actividad: Actividad
    private val formatoFecha = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())


    fun actualizarPasos(pasos: Int) {
        viewModelScope.launch {
            val fecha = obtenerFechaActualFormateada()
            val objetivos = consultarObjetivo.getObjetivo(fecha)
            if (objetivos != null) {
                registrarObjetivo.actualizarPasos(pasos, fecha)
            } else {
                registrarObjetivo.insertar(Objetivos(pasos, 0.0, 0.0), fecha)
            }
            obtenerObjetivos()
        }
    }

    fun actualizarKilometros(kilometros: Double) {
        viewModelScope.launch {
            val fecha = obtenerFechaActualFormateada()
            val objetivos = consultarObjetivo.getObjetivo(fecha)
            if (objetivos != null) {
                registrarObjetivo.actualizarKilometros(kilometros, fecha)
            } else {
                registrarObjetivo.insertar(Objetivos(0, kilometros, 0.0), fecha)
            }
            obtenerObjetivos()
        }
    }

    fun actualizarCalorias(calorias: Double) {
        viewModelScope.launch {
            val fecha = obtenerFechaActualFormateada()
            val objetivos = consultarObjetivo.getObjetivo(fecha)
            if (objetivos != null) {
                registrarObjetivo.actualizarCalorias(calorias, fecha)
            } else {
                registrarObjetivo.insertar(Objetivos(0, 0.0, calorias), fecha)
            }
            obtenerObjetivos()
        }
    }

    fun obtenerObjetivos() {
        val fecha = obtenerFechaActualFormateada()
        viewModelScope.launch {
            val objetivo = consultarObjetivo.getObjetivo(fecha)
            if (objetivo != null) {
                _objetivos.value = objetivo
            } else {
                val objetivoAux = Objetivos(0, 0.0, 0.0)
                registrarObjetivo.insertar(Objetivos(0, 0.0, 0.0), fecha)
                _objetivos.value = objetivoAux
            }
        }
    }

     fun getActividad() {
        val fecha = obtenerFechaActualFormateada()
        viewModelScope.launch {
            val actividadDB = consultarActividad.getActividadPorFecha(fecha)
            actividad = actividadDB ?: Actividad(0, 0.0, 0.0, fecha)
            Log.d("OBJETIVOS", "Actividad guardada: "+ actividad.toString())

        }
    }

    private fun obtenerFechaActualFormateada(): String {
        val hoy = Date()
        return formatoFecha.format(hoy)
    }
}
