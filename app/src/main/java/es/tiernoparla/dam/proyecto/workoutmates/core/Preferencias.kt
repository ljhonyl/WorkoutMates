package es.tiernoparla.dam.proyecto.workoutmates.core

import android.content.Context

/**
 * Clase Preferencias para gestionar las preferencias del usuario utilizando SharedPreferences.
 *
 * @param context Contexto de la aplicación.
 */
class Preferencias(val context: Context) {

    // Nombre del archivo de preferencias
    private val PREF_NOMBRE = "WorkoutMatesPrefs"

    // Claves para acceder a los valores almacenados en las preferencias
    private val NOMBRE_KEY = "nombreUsuario"
    private val NUMERO_KEY = "numeroUsuario"
    private val PESO_KEY = "pesoUsuario"
    private val PASOS_KEY = "pasosUsuario"
    private val DISTANCIA_KEY = "distanciaUsuario"
    private val FECHA_KEY = "Fecha"

    // Instancia de SharedPreferences
    private val preferencias = context.getSharedPreferences(PREF_NOMBRE, 0)

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre Nombre del usuario.
     */
    fun setNombre(nombre: String) {
        preferencias.edit().putString(NOMBRE_KEY, nombre).apply()
    }

    /**
     * Establece el número del usuario.
     *
     * @param numero Número del usuario.
     */
    fun setNumero(numero: String) {
        preferencias.edit().putString(NUMERO_KEY, numero).apply()
    }

    /**
     * Establece el peso del usuario.
     *
     * @param peso Peso del usuario.
     */
    fun setPeso(peso: String) {
        preferencias.edit().putString(PESO_KEY, peso).apply()
    }

    /**
     * Establece los pasos iniciales del usuario.
     *
     * @param pasos Número de pasos iniciales.
     */
    fun setPasosIniciales(pasos: Int) {
        preferencias.edit().putInt(PASOS_KEY, pasos).apply()
    }

    /**
     * Establece la distancia inicial del usuario.
     *
     * @param distancia Distancia inicial.
     */
    fun setDistanciaInicial(distancia: String) {
        preferencias.edit().putString(DISTANCIA_KEY, distancia).apply()
    }

    /**
     * Establece la fecha.
     *
     * @param fecha Fecha.
     */
    fun setFecha(fecha: String) {
        preferencias.edit().putString(FECHA_KEY, fecha).apply()
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return El nombre del usuario.
     */
    fun getNombre(): String {
        return preferencias.getString(NOMBRE_KEY, "")!!
    }

    /**
     * Obtiene el número del usuario.
     *
     * @return El número del usuario.
     */
    fun getNumero(): String {
        return preferencias.getString(NUMERO_KEY, "")!!
    }

    /**
     * Obtiene el peso del usuario.
     *
     * @return El peso del usuario en formato Double.
     */
    fun getPeso(): Double {
        return preferencias.getString(PESO_KEY, "50.0")!!.toDouble()
    }

    /**
     * Obtiene los pasos iniciales del usuario.
     *
     * @return El número de pasos iniciales.
     */
    fun getPasosIniciales(): Int {
        return preferencias.getInt(PASOS_KEY, 0)
    }

    /**
     * Obtiene la distancia inicial del usuario.
     *
     * @return La distancia inicial en formato Double.
     */
    fun getDistanciaInicial(): Double {
        return preferencias.getString(DISTANCIA_KEY, "0.0")!!.toDouble()
    }

    /**
     * Obtiene la fecha.
     *
     * @return La fecha.
     */
    fun getFecha(): String {
        return preferencias.getString(FECHA_KEY, " ")!!
    }
}
