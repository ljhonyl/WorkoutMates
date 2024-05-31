package es.tiernoparla.dam.proyecto.workoutmates.presentacion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import es.tiernoparla.dam.proyecto.workoutmates.databinding.FragmentRegistroBinding
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Usuario

class RegistroFragment : Fragment() {

    // ViewModel asociado al fragmento
    private val viewModel: RegistroViewModel by viewModels(){
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    // Referencia al binding del fragmento
    private var _binding: FragmentRegistroBinding? = null
    private val binding get() = _binding!!

    // Objeto de usuario para almacenar datos
    private var usuario: Usuario? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistroBinding.inflate(inflater, container, false)
        val root = binding.root

        configurarListeners()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observa el estado de carga del ViewModel
        viewModel.cargando.observe(viewLifecycleOwner, Observer { estaCargando ->
            if (estaCargando) {
                // Muestra la barra de progreso
                binding.progressBar.visibility = View.VISIBLE
                binding.btnRegistrarse.visibility = View.INVISIBLE
                binding.tvPregunta.visibility = View.INVISIBLE
            } else {
                // Oculta la barra de progreso
                binding.progressBar.visibility = View.INVISIBLE
                binding.btnRegistrarse.visibility = View.VISIBLE
                binding.tvPregunta.visibility = View.VISIBLE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun configurarListeners(){
        binding.btnRegistrarse.setOnClickListener {
            val nombre = binding.etNombre.text.toString()
            val numero = binding.etNumero.text.toString()

            // Verifica que se hayan ingresado datos válidos
            if (nombre.isNotBlank() && numero.isNotBlank()) {
                registrarDatos(nombre, numero)
            }
            else {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrarDatos(nombre: String, numero: String) {
            val extension = "+34"
            val numeroCompleto = extension + numero
            Log.d("TAG","Numero $numeroCompleto")
            usuario = Usuario(nombre, numeroCompleto)
            iniciarAutenticacion()
    }

    /**
     * Inicia el proceso de autenticación, pasando los datos del usuario al ViewModel
     */
    private fun iniciarAutenticacion() {
        usuario?.let {//Si usuario no es nulo ejecuta esta intrucción
            viewModel.iniciarVerificacion(requireActivity(), it.nombre, it.numero, findNavController())
        }
    }
}