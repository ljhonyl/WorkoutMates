package es.tiernoparla.dam.proyecto.workoutmates.presentacion.ranking.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import es.tiernoparla.dam.proyecto.workoutmates.databinding.ItemRankingBinding
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Actividad
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.ContactoRanking

class RankingViewHolder(view:View): ViewHolder(view){
    private val binding= ItemRankingBinding.bind(view)
    fun render(contactoRanking: ContactoRanking){
        binding.tvTituloRanking.text="${contactoRanking.contacto.nombre} ha realizado"
        binding.tvContadorPasosRanking.text=contactoRanking.actividad.pasos.toString()
        binding.tvContadorKilometrosRanking.text=contactoRanking.actividad.kilometros.toString()
        binding.tvContadorCaloriasRanking.text=contactoRanking.actividad.calorias.toString()


    }
}