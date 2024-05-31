package es.tiernoparla.dam.proyecto.workoutmates.presentacion.ranking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.tiernoparla.dam.proyecto.workoutmates.R
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.ContactoRanking

/**
 * Clase que se encarga de crear y actulizar el recyclerView y sus elementos
 * @author jhony
 * @param listaPersonajes lista que se mostrará en el recyclerView
 */
class RankingAdapter(private val listaContactos: List<ContactoRanking>): RecyclerView.Adapter<RankingViewHolder>() {

    /**
     * Metodo que infla la vista de la lista con su diseño del elemto
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        return RankingViewHolder(layoutInflater.inflate(R.layout.item_ranking,parent,false))
    }

    override fun getItemCount(): Int {
        return listaContactos.size
    }

    /**
     * Pinta cada en una posición del recyclerView.
     * holder es objeto de PersonajeViewHolder, clase que maneja la información
     * de la vista de cada elemento de la lista
     */
    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val item=listaContactos[position]
        holder.render(item)
    }
}