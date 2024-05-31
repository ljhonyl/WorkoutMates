package es.tiernoparla.dam.proyecto.workoutmates.core

import android.content.Context

class Preferencias (val context: Context) {
    private val PREF_NOMBRE = "WorkoutMatesPrefs"
    private val NOMBRE_KEY="nombreUsuario"
    private val NUMERO_KEY = "numeroUsuario"

    private val preferencias=context.getSharedPreferences(PREF_NOMBRE,0)

    fun guardarNombre(nombre: String){
        preferencias.edit().putString(NOMBRE_KEY,nombre).apply()
    }
    fun guardarNumero(numero: String){
        preferencias.edit().putString(NUMERO_KEY,numero).apply()
    }

    fun getNombre(): String{
        return preferencias.getString(NOMBRE_KEY,"")!!
    }
    fun getNumero(): String{
        return preferencias.getString(NUMERO_KEY,"")!!
    }
}