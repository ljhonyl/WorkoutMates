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

/**
 * ViewModel para la pantalla de inicio de la aplicación.
 *
 * Esta clase maneja la lógica de presentación y la interacción con los casos de uso y servicios relacionados
 * con la pantalla de inicio de la aplicación.
 *
 * @param consultarActividad Caso de uso para consultar las actividades registradas.
 * @param registrarActividad Caso de uso para registrar y sincronizar las actividades.
 * @param context Contexto de la aplicación.
 */
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

    /**
     * Inicialización del ViewModel.
     * Se establece el nombre de usuario y se obtienen las actividades registradas.
     */
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
                Log.d("TAG", "Actividad guardada: "+ actividadDeHoyAux[0].toString())
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

    /**
     * Actualiza el número de pasos y las calorías quemadas.
     *
     * @param pasos Número de pasos registrados.
     */
    fun actualizarPasos(pasos: Int) {
        val caloriasHoy = truncarDecimales(calcularCaloriasPorPasos(pasos), 2)
        val actividad = _actividadHoy.value ?: return
        Log.d("TAG", "actividad guardada" + actividad.toString())
        val nuevaActividad = actividad.copy(pasos = pasos, calorias = caloriasHoy)
        Log.d("TAG", "actividad nueva" + nuevaActividad.toString())
        _actividadHoy.value = nuevaActividad
        val aux = hayConexionInternet()
        Log.d("TAG", aux.toString())
        if (hayConexionInternet()) {
            sincronizarConFirebase()

        }

    }

    /**
     * Calcula las calorías quemadas basadas en el número de pasos.
     *
     * @param pasos Número de pasos registrados.
     * @return Calorías quemadas.
     */
    fun calcularCaloriasPorPasos(pasos: Int): Double {
        val factorMetabolismoBasal = 0.05 // Valor promedio del factor de metabolismo basal
        return factorMetabolismoBasal * preferencias.getPeso() * pasos * 0.0005
    }

    fun actualizarKilometros(distancia: Double) {
        val distanciaInicial = preferencias.getDistanciaInicial()
        val distanciaHoy = distancia - distanciaInicial
        val actividad = _actividadHoy.value ?: return
        val nuevaActividad = actividad.copy(kilometros = distanciaHoy)
        _actividadHoy.value = nuevaActividad
        if (hayConexionInternet()) {
            sincronizarConFirebase()
        }
    }

    /**
     * Guarda una actividad en la base de datos.
     *
     * @param actividad Actividad a guardar.
     */
    private fun guardarActividad(actividad: Actividad) {
        viewModelScope.launch {
            registrarActividad.registrar(actividad)
        }
    }

    fun actualizarActividad() {
        viewModelScope.launch {
            val actividad = _actividadHoy.value ?: return@launch
            registrarActividad.actualizar(actividad)
        }
    }

    fun sincronizarConFirebase() {
        viewModelScope.launch {
            val actividad = _actividadHoy.value ?: return@launch
            registrarActividad.sincronizarConFirebase(preferencias.getNumero(), actividad)
        }
    }

    /**
     * Trunca un número decimal al número de decimales especificado.
     *
     * @param valor Valor decimal a truncar.
     * @param decimales Número de decimales a mantener.
     * @return Valor decimal truncado.
     */
    fun truncarDecimales(valor: Double, decimales: Int): Double {
        val factor = Math.pow(10.0, decimales.toDouble())
        return (valor * factor).toInt() / factor
    }
}