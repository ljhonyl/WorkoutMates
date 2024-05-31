package es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.ActividadEntidad
import java.util.Date

@Dao
interface ActividadDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(actividad: ActividadEntidad)

    @Query("UPDATE actividades SET pasos = :pasos, kilometros = :kilometros, calorias = :calorias WHERE fecha = :fecha")
    suspend fun actualizarActividadPorFecha(pasos: Int, kilometros: Double, calorias: Double, fecha: String): Int

    @Query("SELECT * FROM Actividades")
    suspend fun obtenerActividades(): List<ActividadEntidad>

    @Query("SELECT * FROM Actividades WHERE fecha = :fecha")
    suspend fun obtenerActividadPorFecha(fecha: String): ActividadEntidad?

}