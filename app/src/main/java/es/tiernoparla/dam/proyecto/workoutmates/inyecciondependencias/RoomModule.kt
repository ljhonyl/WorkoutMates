package es.tiernoparla.dam.proyecto.workoutmates.inyecciondependencias

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.tiernoparla.dam.proyecto.workoutmates.datos.basedatos.local.BaseDatos
import javax.inject.Singleton

/**
 * Clase RoomModule para la configuración de la base de datos con Room y la inyección de dependencias con Hilt.
 */
@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    // Nombre de la base de datos
    private const val BASE_DATOS="workoutmates_db"

    /**
     * Provee una instancia de la base de datos Room.
     *
     * @param context Contexto de la aplicación.
     * @return Una instancia de BaseDatos.
     */
    @Singleton
    @Provides
    fun proveerRoom(@ApplicationContext context: Context)=Room.databaseBuilder(context,BaseDatos::class.java, BASE_DATOS).build()

    /**
     * Provee el DAO de Actividad.
     *
     * @param db Instancia de BaseDatos.
     * @return Una instancia de ActividadDAO.
     */
    @Singleton
    @Provides
    fun proveerActividadDAO(db:BaseDatos)=db.getActividadDAO()

    /**
     * Provee el DAO de Objetivo.
     *
     * @param db Instancia de BaseDatos.
     * @return Una instancia de ObjetivoDAO.
     */
    @Singleton
    @Provides
    fun proveerObjetivoDAO(db:BaseDatos)=db.getObjetivoDAO()
}
