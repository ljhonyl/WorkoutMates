package es.tiernoparla.dam.proyecto.workoutmates.datos.repositorio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.firebase.ActividadFirebase
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.dao.ActividadDAO
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.ActividadEntidad
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Actividad
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.aDominio
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.aActividadEntidad
import javax.inject.Inject

/**
 * Clase que representa el repositorio de actividades. Este repositorio se encarga de manejar
 * las operaciones de almacenamiento y recuperación de datos tanto a nivel local (Room) como remoto (Firebase).
 *
 * @param actividadDAO Instancia de DAO para acceder a la base de datos local.
 */
class ActividadRepositorio @Inject constructor(private val actividadDAO: ActividadDAO) {

    // Referencia a la base de datos de Firebase
    private val database = Firebase.database("https://workoutmates-2dbdd-default-rtdb.europe-west1.firebasedatabase.app")
    private val usuariosRef = database.getReference("usuarios")

    /**
     * Inserta una actividad en la base de datos local.
     *
     * @param actividadEntidad La entidad de actividad a insertar.
     */
    suspend fun insertarActividad(actividadEntidad: ActividadEntidad) {
        actividadDAO.insertar(actividadEntidad)
    }

    /**
     * Actualiza una actividad existente en la base de datos local.
     *
     * @param actividad La actividad a actualizar.
     */
    suspend fun actualizarActividad(actividad: Actividad) {
        val actividadEntidad = actividad.aActividadEntidad()
        actividadDAO.actualizarActividadPorFecha(
            actividadEntidad.pasos,
            actividadEntidad.kilometros,
            actividadEntidad.calorias,
            actividadEntidad.fecha
        )
    }

    /**
     * Obtiene todas las actividades almacenadas en la base de datos local.
     *
     * @return Una lista de actividades.
     */
    suspend fun obtenerActividades(): List<Actividad> {
        return actividadDAO.obtenerActividades().map { it.aDominio() }
    }

    /**
     * Obtiene una actividad específica de la base de datos local según la fecha.
     *
     * @param fecha La fecha de la actividad a obtener.
     * @return La actividad correspondiente a la fecha dada, o null si no existe.
     */
    suspend fun obtenerActividadPorFecha(fecha: String): Actividad? {
        return actividadDAO.obtenerActividadPorFecha(fecha)?.aDominio()
    }

    /**
     * Sincroniza una actividad con Firebase.
     *
     * @param idNumero El identificador del usuario.
     * @param actividad La actividad a sincronizar.
     * @param fecha La fecha de la actividad.
     */
    fun sincronizarConFirebase(idNumero: String, actividad: ActividadFirebase, fecha: String) {
        val numero = idNumero
        usuariosRef.child(numero).child(fecha).setValue(actividad)
    }

    /**
     * Obtiene una actividad específica de un contacto desde Firebase y ejecuta un callback con la actividad obtenida.
     *
     * @param numero El número del contacto.
     * @param fecha La fecha de la actividad.
     * @param callback Función a ejecutar con la actividad obtenida.
     */
    fun getActividadDeContacto(numero: String, fecha: String, callback: (Actividad?) -> Unit) {
        usuariosRef.child(numero).child(fecha).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val actividadFirebase = dataSnapshot.getValue<ActividadFirebase>()
                if (actividadFirebase != null) {
                    val actividad = actividadFirebase.aDominio(fecha)
                    callback(actividad)
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar errores
                callback(null)
            }
        })
    }
}
