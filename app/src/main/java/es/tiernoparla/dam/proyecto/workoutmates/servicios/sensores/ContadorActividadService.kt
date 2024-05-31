package es.tiernoparla.dam.proyecto.workoutmates.servicios.sensores

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import es.tiernoparla.dam.proyecto.workoutmates.presentacion.InicioViewModel
import javax.inject.Inject
import kotlin.math.sqrt

class ContadorActividadService : Service(), SensorEventListener {
    @Inject
    lateinit var inicioViewModel: InicioViewModel

    private lateinit var sensorManager: SensorManager
    private var sensorContadorPasos: Sensor? = null
    private var sensorAcelerometro: Sensor? = null
    private var tiempoAnterior: Long = 0

    companion object {
        val contadorPasos = MutableLiveData<Int>()
        val distanciaRecorrida = MutableLiveData<Double>()
    }

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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_STEP_COUNTER -> {
                    inicioViewModel.actualizarContadorPasos(it.values[0].toInt())
                }
                Sensor.TYPE_ACCELEROMETER -> {
                    val tiempoActual = System.currentTimeMillis()
                    if (tiempoAnterior == 0L) {
                        tiempoAnterior = tiempoActual
                    }
                    val intervaloTiempo = (tiempoActual - tiempoAnterior) / 1000.0 // Convertir milisegundos a segundos
                    tiempoAnterior = tiempoActual

                    val aceleracionX = it.values[0]
                    val aceleracionY = it.values[1]
                    val aceleracionZ = it.values[2]

                    val magnitudAceleracion = sqrt(
                        (aceleracionX * aceleracionX + aceleracionY * aceleracionY + aceleracionZ * aceleracionZ).toDouble()
                    ).toFloat()

                    val velocidad = magnitudAceleracion * intervaloTiempo
                    val distanciaIntervalo = velocidad * intervaloTiempo

                    val nuevaDistancia = (distanciaRecorrida.value ?: 0.0) + (distanciaIntervalo / 1000)
                    distanciaRecorrida.postValue(nuevaDistancia)

                    inicioViewModel.actualizarDistanciaRecorrida(nuevaDistancia)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}