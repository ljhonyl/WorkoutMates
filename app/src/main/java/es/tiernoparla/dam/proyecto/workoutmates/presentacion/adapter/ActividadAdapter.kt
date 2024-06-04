package es.tiernoparla.dam.proyecto.workoutmates.presentacion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.tiernoparla.dam.proyecto.workoutmates.R
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Actividad

/**
 * Clase que se encarga de crear y actulizar el recyclerView y sus elementos
 * @author jhony
 * @param listaPersonajes lista que se mostrará en el recyclerView
 */
class ActividadAdapter(private val listaActividad: List<Actividad>): RecyclerView.Adapter<ActividadViewHolder>() {

    /**
     * Metodo que infla la vista de la lista con su diseño del elemto
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        return ActividadViewHolder(layoutInflater.inflate(R.layout.item_actividad,parent,false))
    }

    override fun getItemCount(): Int {
        return listaActividad.size
    }

    /**
     * Pinta cada en una posición del recyclerView.
     * holder es objeto de PersonajeViewHolder, clase que maneja la información
     * de la vista de cada elemento de la lista
     */
    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        val item=listaActividad[position]
        holder.render(item)
    }
}