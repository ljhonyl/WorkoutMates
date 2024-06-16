package es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.ActividadEntidad

/**
 * Interface ActividadDAO que proporciona métodos para interactuar con la tabla de actividades en la base de datos.
 */
@Dao
interface ActividadDAO {

    /**
     * Inserta una nueva actividad en la base de datos.
     * Si hay un conflicto (por ejemplo, si ya existe una actividad con la misma clave primaria),
     * se reemplaza la actividad existente.
     *
     * @param actividad La entidad ActividadEntidad a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(actividad: ActividadEntidad)

    /**
     * Actualiza una actividad existente en la base de datos basada en la fecha.
     *
     * @param pasos El número de pasos a actualizar.
     * @param kilometros Los kilómetros a actualizar.
     * @param calorias Las calorías a actualizar.
     * @param fecha La fecha de la actividad a actualizar.
     */
    @Query("UPDATE Actividades SET pasos = :pasos, kilometros = :kilometros, calorias = :calorias WHERE fecha = :fecha")
    suspend fun actualizarActividadPorFecha(pasos: Int, kilometros: Double, calorias: Double, fecha: String)

    /**
     * Obtiene todas las actividades de la base de datos.
     *
     * @return Una lista de todas las entidades ActividadEntidad en la base de datos.
     */
    @Query("SELECT * FROM Actividades ORDER BY id DESC")
    suspend fun obtenerActividades(): List<ActividadEntidad>

    /**
     * Obtiene una actividad específica de la base de datos basada en la fecha.
     *
     * @param fecha La fecha de la actividad a obtener.
     * @return La entidad ActividadEntidad correspondiente a la fecha dada, o null si no existe.
     */
    @Query("SELECT * FROM Actividades WHERE fecha = :fecha")
    suspend fun obtenerActividadPorFecha(fecha: String): ActividadEntidad?
}
