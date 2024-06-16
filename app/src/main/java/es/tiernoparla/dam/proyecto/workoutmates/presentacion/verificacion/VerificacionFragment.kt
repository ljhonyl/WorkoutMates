package es.tiernoparla.dam.proyecto.workoutmates.presentacion.verificacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import es.tiernoparla.dam.proyecto.workoutmates.databinding.FragmentVerificacionBinding

class VerificacionFragment : Fragment() {

    private val viewModel: VerificacionViewModel by viewModels(){
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }
    private val args: VerificacionFragmentArgs by navArgs()
    private var _binding: FragmentVerificacionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerificacionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configurarListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configurarListeners() {
        binding.btnVerificar.setOnClickListener {
            if (binding.etCodigoVerificacion.text.isNotBlank()) {
                val numero=args.numero
                val nombre=args.nombre
                val peso=args.peso
                val verificationId = args.verificationId
                val verificationCode = binding.etCodigoVerificacion.text.toString()
                viewModel.onVerificarseClicked(numero, nombre,peso,requireActivity(), findNavController(), verificationId, verificationCode)
            } else {
                Toast.makeText(requireContext(), "Ingrese el código de verificación", Toast.LENGTH_SHORT).show()
            }
        }
    }
}