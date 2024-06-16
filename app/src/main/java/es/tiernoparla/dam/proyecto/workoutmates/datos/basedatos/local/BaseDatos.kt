package es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local

import androidx.room.Database
import androidx.room.RoomDatabase
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.dao.ActividadDAO
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.dao.ObjetivoDAO
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.ActividadEntidad
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.ObjetivosEntidad

/**
 * Clase abstracta BaseDatos que extiende RoomDatabase.
 * Esta clase representa la base de datos de la aplicación.
 *
 * @Database define las entidades y la versión de la base de datos.
 * Entidades: [ActividadEntidad, ObjetivosEntidad]
 * Versión: 1
 */
@Database(entities = [ActividadEntidad::class, ObjetivosEntidad::class], version = 1)
abstract class BaseDatos : RoomDatabase() {

    /**
     * Método abstracto para obtener el DAO de Actividad.
     *
     * @return Instancia de ActividadDAO.
     */
    abstract fun getActividadDAO(): ActividadDAO

    /**
     * Método abstracto para obtener el DAO de Objetivo.
     *
     * @return Instancia de ObjetivoDAO.
     */
    abstract fun getObjetivoDAO(): ObjetivoDAO
}
