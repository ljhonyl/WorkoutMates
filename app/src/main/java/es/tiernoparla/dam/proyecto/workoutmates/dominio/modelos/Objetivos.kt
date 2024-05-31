package es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos

import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.ObjetivosEntidad

data class Objetivos(val pasos: Int, val kilometros: Double, val calorias: Double)

fun ObjetivosEntidad.aDominio()=Objetivos(pasos=pasos, kilometros=kilometros,calorias=calorias)