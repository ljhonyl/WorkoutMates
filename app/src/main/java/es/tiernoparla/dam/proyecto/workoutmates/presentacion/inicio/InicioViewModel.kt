package es.tiernoparla.dam.proyecto.workoutmates.presentacion.inicio

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tiernoparla.dam.proyecto.workoutmates.WorkoutMatesApp.Companion.preferencias
import es.tiernoparla.dam.proyecto.workoutmates.dominio.casosdeuso.ConsultarActividad
import es.tiernoparla.dam.proyecto.workoutmates.dominio.casosdeuso.RegistrarActividad
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Actividad
import es.tiernoparla.dam.proyecto.workoutmates.servicios.sensores.ContadorActividadService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class InicioViewModel @Inject constructor(
    private val consultarActividad: ConsultarActividad,
    private val registrarActividad: RegistrarActividad,
    private val context: Context
) : ViewModel() {

    private val _actividadHoy = MutableLiveData<Actividad>()
    val actividadHoy: LiveData<Actividad> get() = _actividadHoy

    private val _actividadesAnteriores = MutableLiveData<List<Actividad>>()
    val actividadesAnteriores: LiveData<List<Actividad>> get() = _actividadesAnteriores

    private val _nombreUsuario = MutableLiveData<String>()
    val nombreUsuario: LiveData<String> get() = _nombreUsuario

    val contadorPasos: LiveData<Int> get() = ContadorActividadService._contadorPasos
    val distanciaRecorrida: LiveData<Double> get() = ContadorActividadService._distanciaRecorrida

    private val formatoFecha = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    init {
        _nombreUsuario.value = preferencias.getNombre()
        getActividades()
    }

    private fun getActividades() {
        viewModelScope.launch {
            val todasLasActividades = consultarActividad.obtenerTodasLasActividades()

            val fechaActual = obtenerFechaActualFormateada()

            val actividadDeHoyAux = todasLasActividades.filter { actividad ->
                actividad.fecha == fechaActual
            }

            val actividadesAnterioresAux = todasLasActividades.filter { actividad ->
                actividad.fecha != fechaActual
            }
            _actividadesAnteriores.value = actividadesAnterioresAux

            if (actividadDeHoyAux.isEmpty()) {
                // Si no hay actividades para hoy, crear una instancia de Actividad con valores predeterminados
                val actividadPorDefecto = Actividad(0, 0.0, 0.0, fechaActual)
                guardarActividad(actividadPorDefecto)
                _actividadHoy.value = actividadPorDefecto
                Log.d("TAG", "vacio")
            } else {
                _actividadHoy.value = actividadDeHoyAux[0]
                Log.d("TAG", actividadDeHoyAux[0].toString())
            }
        }
    }

    private fun obtenerFechaActualFormateada(): String {
        val hoy = Date()
        return formatoFecha.format(hoy)
    }

    private fun hayConexionInternet(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    fun actualizarPasos(pasos: Int) {
        val actividad = _actividadHoy.value ?: return
        val nuevaActividad = actividad.copy(pasos = pasos)
        _actividadHoy.value = nuevaActividad
        val aux=hayConexionInternet()
        Log.d("TAG", aux.toString())
        if (hayConexionInternet()) {
            sincronizarConFirebase()
        }
    }

    fun actualizarKilometros(distancia: Double) {
        val actividad = _actividadHoy.value ?: return
        val nuevaActividad = actividad.copy(kilometros = distancia)
        _actividadHoy.value = nuevaActividad
        if (hayConexionInternet()) {
            sincronizarConFirebase()
        }
    }

    private fun guardarActividad(actividad: Actividad) {
        viewModelScope.launch {
            registrarActividad.registrar(actividad)
        }
    }
    fun actualizarActividad(){
        viewModelScope.launch {
            val actividad = _actividadHoy.value ?: return@launch
            registrarActividad.actualizar(actividad)
        }
    }

    fun sincronizarConFirebase(){
        viewModelScope.launch {
            val actividad = _actividadHoy.value ?: return@launch
            registrarActividad.sincronizarConFirebase(preferencias.getNumero(),actividad)
        }
    }
}