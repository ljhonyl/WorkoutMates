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

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    private const val BASE_DATOS="workoutmates_db"
    @Singleton
    @Provides
    fun proveerRoom(@ApplicationContext context: Context)=Room.databaseBuilder(context,BaseDatos::class.java, BASE_DATOS).build()

    @Singleton
    @Provides
    fun proveerActividadDAO(db:BaseDatos)=db.getActividadDAO()
}