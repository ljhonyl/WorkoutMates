package es.tiernoparla.dam.proyecto.workoutmates.inyecciondependencias

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Clase ContextModule para proporcionar el contexto de la aplicación con Dagger Hilt.
 */
@Module
@InstallIn(SingletonComponent::class)
object ContextModule {

    /**
     * Provee el contexto de la aplicación.
     *
     * @param context Contexto de la aplicación.
     * @return El contexto de la aplicación.
     */
    @Provides
    fun provideContext(@ApplicationContext context: Context) = context
}
