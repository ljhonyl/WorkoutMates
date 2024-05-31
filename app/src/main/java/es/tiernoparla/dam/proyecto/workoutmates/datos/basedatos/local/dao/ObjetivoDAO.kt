package es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.entidades.ObjetivosEntidad

@Dao
interface ObjetivoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(objetivosEntidad: ObjetivosEntidad)

    @Query("SELECT * FROM Objetivos WHERE fecha = :fecha")
    suspend fun obtenerObjetivosPorFecha(fecha: String): ObjetivosEntidad?

    @Query("UPDATE Objetivos SET pasos = :pasos WHERE fecha = :fecha")
    suspend fun actualizarPasos(pasos: Int, fecha: String)

    @Query("UPDATE Objetivos SET kilometros = :kilometros WHERE fecha = :fecha")
    suspend fun actualizarKilometros(kilometros: Double, fecha: String)

    @Query("UPDATE Objetivos SET calorias = :calorias WHERE fecha = :fecha")
    suspend fun actualizarCalorias(calorias: Double, fecha: String)
}