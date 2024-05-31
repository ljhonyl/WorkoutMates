package es.tiernoparla.dam.proyecto.workoutmates.presentacion.inicio.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import es.tiernoparla.dam.proyecto.workoutmates.databinding.ItemActividadBinding
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Actividad

/**
 * Clase que se encarga de asociar el diseño de la vista del item(item_personaje.xml)
 */
class ActividadViewHolder(view:View): ViewHolder(view){
    private val binding= ItemActividadBinding.bind(view)

    /**
     * Muestra los datos del personaje en las propiedades correspondientes
     */
    fun render(actividad: Actividad){
            binding.tvTitulo.text="Funciona"
            binding.tvContadorPasos.text=actividad.pasos.toString()
            binding.tvContadorKilometros.text=actividad.kilometros.toString()
            binding.tvContadorCalorias.text=actividad.calorias.toString()


    }
}