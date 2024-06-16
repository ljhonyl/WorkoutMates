package es.tiernoparla.dam.proyecto.workoutmates.presentacion.registro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import es.tiernoparla.dam.proyecto.workoutmates.R
import es.tiernoparla.dam.proyecto.workoutmates.databinding.FragmentInfoRegistroBinding
import es.tiernoparla.dam.proyecto.workoutmates.databinding.FragmentObjetivosBinding

/**
 * A simple [Fragment] subclass.
 */
class InfoRegistroFragment : Fragment() {
    private var _binding: FragmentInfoRegistroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoRegistroBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnVolver.setOnClickListener{
            findNavController().navigate(InfoRegistroFragmentDirections.actionInfoRegistroFragmentToRegistroFragment())
        }
    }
}