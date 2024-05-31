package es.tiernoparla.dam.proyecto.workoutmates.presentacion.objetivos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import es.tiernoparla.dam.proyecto.workoutmates.R
import es.tiernoparla.dam.proyecto.workoutmates.databinding.FragmentObjetivosBinding
import es.tiernoparla.dam.proyecto.workoutmates.presentacion.MainActivity

@AndroidEntryPoint
class ObjetivosFragment : Fragment() {
    private var _binding: FragmentObjetivosBinding? = null
    private val binding get() = _binding!!
    private lateinit var menuInferior: BottomNavigationView
    private var ejeYAnterior = 0
    private val viewModel: ObjetivosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentObjetivosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuInferior = requireActivity().findViewById(R.id.nav_view)
        actulizarMenuInferior()
        iniciarListeners()
    }

    private fun actulizarMenuInferior() {
        if(isVisible) {
            binding.scrollViewObjetivos.viewTreeObserver.addOnScrollChangedListener {
                if(isAdded) {
                    val valorActualEjeY = binding.scrollViewObjetivos.scrollY

                    (requireActivity() as MainActivity).manejarDesplazamientoMenu(ejeYAnterior, valorActualEjeY)

                    ejeYAnterior = valorActualEjeY
                }
            }
        }
    }

    fun iniciarListeners(){
        binding.rsPasos.addOnChangeListener{_,value,_ ->
            binding.tvPasosObjetivos.text=value.toInt().toString()
        }
        binding.rsKm.addOnChangeListener{_,value,_ ->
            binding.tvKmObjetivos.text=value.toString()
        }
        binding.rsCalorias.addOnChangeListener{_,value,_ ->
            binding.tvCaloriasObjetivos.text=value.toInt().toString()
        }
        binding.btnConfirmarPasos.setOnClickListener{
            viewModel.actualizarPasos(binding.tvPasosObjetivos.text.toString().toInt())
        }
        binding.btnConfirmarKm.setOnClickListener{
            viewModel.actualizarKilometros(binding.tvPasosObjetivos.text.toString().toDouble())
        }
        binding.btnConfirmarCalorias.setOnClickListener{
            viewModel.actualizarCalorias(binding.tvPasosObjetivos.text.toString().toDouble())
        }
    }
}