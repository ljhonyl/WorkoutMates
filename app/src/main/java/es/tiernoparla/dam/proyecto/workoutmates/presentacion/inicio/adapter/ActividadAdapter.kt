package es.tiernoparla.dam.proyecto.workoutmates.presentacion.inicio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.tiernoparla.dam.proyecto.workoutmates.R
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Actividad

/**
 * Clase ActividadAdapter que extiende RecyclerView.Adapter para gestionar la lista de actividades
 * en un RecyclerView.
 *
 * @param listaActividad Lista de objetos Actividad que se mostrarán en el RecyclerView.
 */
class ActividadAdapter(private val listaActividad: List<Actividad>) : RecyclerView.Adapter<ActividadViewHolder>() {

    /**
     * Crea un nuevo ViewHolder cuando no hay suficientes ViewHolders existentes para reutilizar.
     *
     * @param parent ViewGroup al que se añadirá el nuevo ViewHolder.
     * @param viewType Tipo de vista del nuevo ViewHolder.
     * @return Una nueva instancia de ActividadViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ActividadViewHolder(layoutInflater.inflate(R.layout.item_actividad, parent, false))
    }

    /**
     * Obtiene el número de elementos en la lista de actividades.
     *
     * @return El tamaño de la lista de actividades.
     */
    override fun getItemCount(): Int {
        return listaActividad.size
    }

    /**
     * Vincula un ViewHolder existente con los datos de una actividad en una posición específica.
     *
     * @param holder ActividadViewHolder que debe ser actualizado con los nuevos datos.
     * @param position Posición de la actividad en la lista de actividades.
     */
    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        val item = listaActividad[position]
        holder.render(item)
    }
}
