package es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local

import androidx.room.Database
import androidx.room.RoomDatabase
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.dao.ActividadDAO
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.dao.ObjetivoDAO
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.ActividadEntidad
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.ObjetivosEntidad

@Database(entities = [ActividadEntidad::class, ObjetivosEntidad::class], version=1)
abstract class BaseDatos : RoomDatabase() {
    abstract fun getActividadDAO(): ActividadDAO
    abstract fun getObjetivoDAO():ObjetivoDAO
}