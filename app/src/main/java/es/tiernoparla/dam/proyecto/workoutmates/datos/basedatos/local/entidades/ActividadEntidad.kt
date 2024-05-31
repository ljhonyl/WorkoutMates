package es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Actividad
import java.util.Date

@Entity(tableName = "Actividades")
data class ActividadEntidad (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name= "id")
    val id: Int = 0,
    @ColumnInfo(name = "pasos")
    val pasos: Int,
    @ColumnInfo(name = "kilometros")
    val kilometros: Double,
    @ColumnInfo(name = "calorias")
    val calorias: Double,
    @ColumnInfo(name= "fecha")
    val fecha: String
)

fun Actividad.aActividadEntidad()=ActividadEntidad(pasos=pasos,kilometros=kilometros,calorias=calorias,fecha=fecha)