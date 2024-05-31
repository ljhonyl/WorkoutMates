package es.tiernoparla.dam.proyecto.workoutmates.presentacion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tiernoparla.dam.proyecto.workoutmates.core.WorkoutMatesApp.Companion.preferencias
import es.tiernoparla.dam.proyecto.workoutmates.dominio.casosdeuso.ConsultarActividad
import es.tiernoparla.dam.proyecto.workoutmates.dominio.casosdeuso.RegistrarActividad
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Actividad
import es.tiernoparla.dam.proyecto.workoutmates.datos.repositorio.ActividadRepositorio
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class InicioViewModel @Inject constructor(
    private val consultarActividad: ConsultarActividad,
    private val registrarActividad: RegistrarActividad,
    private val repositorio: ActividadRepositorio
) : ViewModel() {

    private val _actividadHoy = MutableLiveData<Actividad>()
    val actividadHoy: LiveData<Actividad> get() = _actividadHoy

    private val _actividadesAnteriores = MutableLiveData<List<Actividad>>()
    val actividadesAnteriores: LiveData<List<Actividad>> get() = _actividadesAnteriores

    private val _nombreUsuario = MutableLiveData<String>()
    val nombreUsuario: LiveData<String> get() = _nombreUsuario

    val contadorPasos: LiveData<Int> get() = repositorio.contadorPasos
    val distanciaRecorrida: LiveData<Double> get() = repositorio.distanciaRecorrida

    private val formatoFecha = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    init {
        _nombreUsuario.value = preferencias.getNombre()

        // Observar cambios en los sensores y actualizar el UI en consecuencia
        contadorPasos.observeForever { pasos ->
            // Aquí podrías hacer cualquier lógica adicional con los pasos
        }

        distanciaRecorrida.observeForever { distancia ->
            // Aquí podrías hacer cualquier lógica adicional con la distancia
        }

        getActividades()
    }

    private fun getActividades() {
        viewModelScope.launch {
            val todasLasActividades = consultarActividad.obtenerTodasLasActividades()

            val fechaActual = obtenerFechaActualFormateada()

            val actividadesDeHoy = todasLasActividades.filter { actividad ->
                actividad.fecha == fechaActual
            }
            _actividadHoy.value = actividadesDeHoy[0]

            val actividadesDeDiasAnteriores = todasLasActividades.filter { actividad ->
                actividad.fecha != fechaActual
            }
            _actividadesAnteriores.value = actividadesDeDiasAnteriores

            if (actividadesDeHoy.isEmpty()) {
                // Si no hay actividades para hoy, crear una instancia de Actividad con valores predeterminados
                val actividadPorDefecto = Actividad(0, 0.0, 0.0, fechaActual)
                _actividadHoy.value = actividadPorDefecto
            } else {
                _actividadHoy.value = actividadesDeHoy.first()
            }
        }
    }

    fun setActividad(actividad: Actividad) {
        viewModelScope.launch {
            val actividadExistente = consultarActividad.getActividadPorFecha(actividad.fecha)
            if (actividadExistente != null) {
                registrarActividad.actualizar(actividad)
            } else {
                registrarActividad.registrar(actividad)
            }
        }
    }
    private fun obtenerFechaActualFormateada(): String {
        val hoy = Date()
        val formatoFecha = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatoFecha.format(hoy)
    }

    fun actualizarContadorPasos(pasos: Int) {
        repositorio.actualizarContadorPasos(pasos)
    }

    fun actualizarDistanciaRecorrida(distancia: Double) {
        repositorio.actualizarDistanciaRecorrida(distancia)
    }
}