package es.tiernoparla.dam.proyecto.workoutmates.presentacion.inicio.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import es.tiernoparla.dam.proyecto.workoutmates.databinding.ItemActividadBinding
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Actividad

/**
 * Clase ActividadViewHolder que extiende ViewHolder para gestionar la vista de un ítem de actividad
 * en un RecyclerView.
 *
 * @param view Vista del ítem de actividad.
 */
class ActividadViewHolder(view: View) : ViewHolder(view) {

    // Binding para acceder a las vistas del layout de ítem de actividad
    private val binding = ItemActividadBinding.bind(view)

    /**
     * Método para renderizar los datos de una actividad en las vistas correspondientes.
     *
     * @param actividad Instancia de la clase Actividad que contiene los datos a mostrar.
     */
    fun render(actividad: Actividad) {
        binding.tvTitulo.text = "Actividad del ${actividad.fecha}"
        binding.tvContadorPasos.text = actividad.pasos.toString()
        binding.tvContadorKilometros.text = actividad.kilometros.toString()
        binding.tvContadorCalorias.text = actividad.calorias.toString()
    }
}
