package es.tiernoparla.dam.proyecto.workoutmates

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.HiltAndroidApp
import es.tiernoparla.dam.proyecto.workoutmates.core.Preferencias

/**
 * Clase WorkoutMatesApp que extiende Application para inicializar las preferencias del usuario
 * y gestionar el estado de la sesión utilizando LiveData. Anotada con @HiltAndroidApp para
 * habilitar la inyección de dependencias con Hilt.
 */
@HiltAndroidApp
class WorkoutMatesApp : Application() {

    companion object {
        // LiveData para gestionar el estado de la sesión iniciada
        private val _sesionIniciada = MutableLiveData<Boolean>()

        /**
         * LiveData para acceder al estado de la sesión iniciada.
         */
        val sesionIniciada: LiveData<Boolean>
            get() = _sesionIniciada

        // Instancia de Preferencias
        lateinit var preferencias: Preferencias

        /**
         * Actualiza el estado de la sesión iniciada.
         *
         * @param iniciada Estado de la sesión (true si está iniciada, false en caso contrario).
         */
        fun actualizarSesionIniciada(iniciada: Boolean) {
            _sesionIniciada.value = iniciada
        }
    }

    /**
     * Método onCreate que se llama cuando la aplicación se crea.
     * Inicializa las preferencias y establece el estado de la sesión iniciada.
     */
    override fun onCreate() {
        super.onCreate()
        preferencias = Preferencias(applicationContext)

        // Verifica si hay un número de usuario guardado en las preferencias para determinar
        // si la sesión está iniciada.
        _sesionIniciada.value = preferencias.getNumero().isNotEmpty()
    }
}
