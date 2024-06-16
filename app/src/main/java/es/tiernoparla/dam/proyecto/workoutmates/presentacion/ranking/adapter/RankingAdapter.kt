package es.tiernoparla.dam.proyecto.workoutmates.presentacion.ranking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.tiernoparla.dam.proyecto.workoutmates.R
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.ContactoRanking

class RankingAdapter(private val listaContactos: List<ContactoRanking>): RecyclerView.Adapter<RankingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        return RankingViewHolder(layoutInflater.inflate(R.layout.item_ranking,parent,false))
    }

    override fun getItemCount(): Int {
        return listaContactos.size
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val item=listaContactos[position]
        holder.render(item)
    }
}