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
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.aActividadEntidad
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Actividad
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.aDominio
import javax.inject.Inject

class ActividadRepositorio @Inject constructor(private val actividadDAO: ActividadDAO) {
    private val database = Firebase.database("https://workoutmates-2dbdd-default-rtdb.europe-west1.firebasedatabase.app")
    private val usuariosRef = database.getReference("usuarios")


    suspend fun insertarActividad(actividadEntidad: ActividadEntidad) {
        actividadDAO.insertar(actividadEntidad)
    }

    suspend fun actualizarActividad(actividad: Actividad) {
        val actividadEntidad = actividad.aActividadEntidad()
        actividadDAO.actualizarActividadPorFecha(
            actividadEntidad.pasos,
            actividadEntidad.kilometros,
            actividadEntidad.calorias,
            actividadEntidad.fecha
        )
    }

    suspend fun obtenerActividades(): List<Actividad> {
        return actividadDAO.obtenerActividades().map { it.aDominio() }
    }

    suspend fun obtenerActividadPorFecha(fecha: String): Actividad? {
        return actividadDAO.obtenerActividadPorFecha(fecha)?.aDominio()
    }

    fun sincronizarConFirebase(idNumero: String, actividad: ActividadFirebase, fecha: String) {
        val numero = idNumero
        usuariosRef.child(numero).child(fecha).setValue(actividad)
    }

    fun getActividadDeContacto(numero: String, fecha: String, callback: (Actividad?) -> Unit) {
        usuariosRef.child(numero).child(fecha).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val actividadFirebase = dataSnapshot.getValue(ActividadFirebase::class.java)
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