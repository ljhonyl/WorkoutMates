package es.tiernoparla.dam.proyecto.workoutmates.servicios.sensores

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import es.tiernoparla.dam.proyecto.workoutmates.WorkoutMatesApp.Companion.preferencias
import es.tiernoparla.dam.proyecto.workoutmates.presentacion.inicio.InicioViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Servicio ContadorActividadService que extiende Service e implementa SensorEventListener para
 * gestionar el conteo de pasos y la distancia recorrida utilizando sensores.
 */
class ContadorActividadService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var sensorContadorPasos: Sensor? = null
    private var sensorAcelerometro: Sensor? = null
    private var tiempoAnterior: Long = 0
    private var velocidadUmbralKmh = 10
    private var tiempoUmbralMs = 5000 // Umbral de tiempo para considerar una actividad en milisegundos
    private var tiempoInicioActividad: Long = 0
    private var distanciaRecorridaTotalMetros = 0.0
    private val binder = ContadorActividadBinder()
    private var fecha = preferencias.getFecha()
    private val formatoFecha = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private val fechaActual = obtenerFechaActualFormateada()

    // Se exponen los MutableLiveData para que puedan ser observados
    companion object {
        val _contadorPasos = MutableLiveData<Int>()
        val _distanciaRecorrida = MutableLiveData<Double>()
    }

    /**
     * Método llamado cuando el servicio se crea. Inicializa los sensores y los registra.
     */
    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorContadorPasos = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorAcelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorContadorPasos?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }

        sensorAcelerometro?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    /**
     * Método llamado cuando el servicio se enlaza a un componente.
     *
     * @param intent Intent para enlazar el servicio.
     * @return IBinder para el enlace con el servicio.
     */
    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    /**
     * Método llamado cuando el servicio se desenlaza de un componente.
     *
     * @param intent Intent para desenlazar el servicio.
     * @return Boolean indicando si el servicio debe ser reenlazado.
     */
    override fun onUnbind(intent: Intent?): Boolean {
        sensorManager.unregisterListener(this)
        return super.onUnbind(intent)
    }

    /**
     * Método llamado cuando cambia algún valor de un sensor.
     *
     * @param event SensorEvent que contiene los nuevos datos del sensor.
     */
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_STEP_COUNTER -> {
                    val pasosTotales = it.values[0].toInt()
                    Log.d("TAG", "PASOS: $pasosTotales")
                    val fechaActual = obtenerFechaActualFormateada()
                    val fechaGuardada = preferencias.getFecha()
                    Log.d("FECHA", fechaGuardada.toString())

                    if (fechaGuardada != fechaActual) {
                        // Si ha cambiado de día
                        Log.d("SERVICIO", "Cambio de dia")

                        preferencias.setFecha(fechaActual)
                        preferencias.setPasosIniciales(pasosTotales)
                        preferencias.setDistanciaInicial(distanciaRecorridaTotalMetros.toString())
                    }

                    val pasosIniciales = preferencias.getPasosIniciales()
                    Log.d("TAG", "PASOS Guardados: $pasosIniciales")
                    val pasos = pasosTotales - pasosIniciales
                    Log.d("TAG", "Pasos: $pasos")
                    _contadorPasos.postValue(pasos)
                }
                Sensor.TYPE_ACCELEROMETER -> {
                    val tiempoActual = System.currentTimeMillis()
                    if (tiempoAnterior == 0L) {
                        tiempoAnterior = tiempoActual
                    }
                    val intervaloTiempoSegundos = (tiempoActual - tiempoAnterior) / 1000.0
                    tiempoAnterior = tiempoActual

                    val aceleracionX = it.values[0]
                    val aceleracionY = it.values[1]
                    val aceleracionZ = it.values[2]

                    val magnitudAceleracion = Math.sqrt(
                        (aceleracionX * aceleracionX + aceleracionY * aceleracionY + aceleracionZ * aceleracionZ).toDouble()
                    ).toFloat()

                    // Convertir la aceleración a m/s^2 (1 g = 9.81 m/s^2)
                    val aceleracionMetrosPorSegundoCuadrado = magnitudAceleracion * 9.81

                    // Calcular la velocidad en m/s
                    val velocidadMetrosPorSegundo = aceleracionMetrosPorSegundoCuadrado * intervaloTiempoSegundos

                    // Convertir la velocidad a km/h
                    val velocidadKmPorHora = velocidadMetrosPorSegundo * 3.6

                    // Si la velocidad supera el umbral y ha pasado suficiente tiempo, considerar como actividad
                    if (velocidadKmPorHora >= velocidadUmbralKmh && tiempoActual - tiempoInicioActividad >= tiempoUmbralMs) {
                        // Calcular la distancia recorrida en este intervalo y sumar a la distancia total
                        val distanciaIntervaloMetros = velocidadMetrosPorSegundo * intervaloTiempoSegundos
                        distanciaRecorridaTotalMetros += distanciaIntervaloMetros
                        // Actualizar el tiempo de inicio de la actividad
                        tiempoInicioActividad = tiempoActual
                    }
                    // Revisar cálculos de la distancia, por ahora devolver 0
                    _distanciaRecorrida.postValue(0.0)
                }
                else -> {
                    // Posible actualización para futuros sensores
                }
            }
        }
    }

    /**
     * Método llamado cuando cambia la precisión de un sensor.
     *
     * @param sensor Sensor cuyo valor de precisión ha cambiado.
     * @param accuracy Nueva precisión del sensor.
     */
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    /**
     * Obtiene la fecha actual formateada según el formato definido.
     *
     * @return Fecha actual en formato "dd-MM-yyyy".
     */
    private fun obtenerFechaActualFormateada(): String {
        val hoy = Date()
        return formatoFecha.format(hoy)
    }

    /**
     * Clase interna ContadorActividadBinder que extiende Binder para gestionar la vinculación
     * del servicio con un componente.
     */
    inner class ContadorActividadBinder : Binder() {
        var inicioViewModel: InicioViewModel? = null
    }
}
