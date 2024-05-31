package es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos

import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.ActividadEntidad
import java.util.Date

data class Actividad(val pasos: Int, val kilometros: Double, val calorias: Double, val fecha : String)

//Funciones de extensión
fun ActividadEntidad.aDominio()=Actividad(pasos, kilometros, calorias, fecha)