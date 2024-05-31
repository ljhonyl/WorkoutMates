package es.tiernoparla.dam.proyecto.workoutmates.servicios.sensores

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import es.tiernoparla.dam.proyecto.workoutmates.presentacion.inicio.InicioViewModel


class ContadorActividadService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var sensorContadorPasos: Sensor? = null
    private var sensorAcelerometro: Sensor? = null
    private var tiempoAnterior: Long = 0
    private var velocidadUmbralKmh = 10 // Velocidad umbral para considerar movimiento en km/h
    private var tiempoUmbralMs = 5000 // Umbral de tiempo para considerar una actividad en milisegundos
    private var tiempoInicioActividad: Long = 0 // Tiempo de inicio de la actividad
    private var distanciaRecorridaTotalMetros = 0.0 // Distancia recorrida total en metros
    private val binder = ContadorActividadBinder()
    companion object {
        val _contadorPasos = MutableLiveData<Int>()
        val _distanciaRecorrida = MutableLiveData<Double>()
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

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        sensorManager.unregisterListener(this)
        return super.onUnbind(intent)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_STEP_COUNTER -> {
                    _contadorPasos.postValue(it.values[0].toInt())
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

                    _distanciaRecorrida.postValue(distanciaRecorridaTotalMetros)
                }
                else ->{

                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    inner class ContadorActividadBinder : Binder() {
        var inicioViewModel: InicioViewModel? = null
    }
}
