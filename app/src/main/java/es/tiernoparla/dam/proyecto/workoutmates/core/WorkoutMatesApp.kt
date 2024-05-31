package es.tiernoparla.dam.proyecto.workoutmates.core

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WorkoutMatesApp : Application() {

    companion object {
        private val _sesionIniciada = MutableLiveData<Boolean>()
        val sesionIniciada: LiveData<Boolean>
            get() = _sesionIniciada

        lateinit var preferencias: Preferencias

        fun actualizarSesionIniciada(iniciada: Boolean) {
            _sesionIniciada.value = iniciada
        }
    }
    override fun onCreate() {
        super.onCreate()
        preferencias=Preferencias(applicationContext)
        _sesionIniciada.value = preferencias.getNumero().isNotEmpty()
    }
}