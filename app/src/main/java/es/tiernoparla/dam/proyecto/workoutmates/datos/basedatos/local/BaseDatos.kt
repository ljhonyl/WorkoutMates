package es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local

import androidx.room.Database
import androidx.room.RoomDatabase
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.dao.ActividadDAO
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.ActividadEntidad

@Database(entities = [ActividadEntidad::class], version=1)
abstract class BaseDatos : RoomDatabase() {
    abstract fun getActividadDAO(): ActividadDAO
}