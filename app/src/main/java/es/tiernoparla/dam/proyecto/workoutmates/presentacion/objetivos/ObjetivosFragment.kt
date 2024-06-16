package es.tiernoparla.dam.proyecto.workoutmates.presentacion.objetivos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import es.tiernoparla.dam.proyecto.workoutmates.R
import es.tiernoparla.dam.proyecto.workoutmates.databinding.FragmentObjetivosBinding
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Objetivos
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
        iniciarListeners()
        viewModel.getActividad()
        viewModel.obtenerObjetivos()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        menuInferior = requireActivity().findViewById(R.id.nav_view)
        actualizarMenuInferior()
        viewModel.getActividad()
        viewModel.objetivos.observe(viewLifecycleOwner) { objetivos ->
            actualizarUI(objetivos)
        }
    }

    private fun actualizarMenuInferior() {
        if (isVisible) {
            binding.scrollViewObjetivos.viewTreeObserver.addOnScrollChangedListener {
                if (isAdded) {
                    val valorActualEjeY = binding.scrollViewObjetivos.scrollY

                    (requireActivity() as MainActivity).manejarDesplazamientoMenu(ejeYAnterior, valorActualEjeY)

                    ejeYAnterior = valorActualEjeY
                }
            }
        }
    }

    private fun iniciarListeners() {
        binding.rsPasos.addOnChangeListener { _, value, _ ->
            binding.tvPasosObjetivos.text = value.toInt().toString()
        }
        binding.rsKm.addOnChangeListener { _, value, _ ->
            binding.tvKmObjetivos.text = value.toString()
        }
        binding.rsCalorias.addOnChangeListener { _, value, _ ->
            binding.tvCaloriasObjetivos.text = value.toInt().toString()
        }
        binding.btnConfirmarPasos.setOnClickListener {
            viewModel.actualizarPasos(binding.tvPasosObjetivos.text.toString().toInt())
        }
        binding.btnConfirmarKm.setOnClickListener {
            viewModel.actualizarKilometros(binding.tvKmObjetivos.text.toString().toDouble())
        }
        binding.btnConfirmarCalorias.setOnClickListener {
            viewModel.actualizarCalorias(binding.tvCaloriasObjetivos.text.toString().toDouble())
        }
    }

    private fun actualizarCirculosProgreso() {
        var progresoPasos=0f
        var progresoKm=0f
        var progresoCalorias=0f
        viewModel.objetivos.value?.let { objetivos ->
            Log.d("TAG", "OBJETIVO: "+ objetivos.toString())
            if(objetivos.pasos != 0){
                progresoPasos=(viewModel.actividad.pasos / objetivos.pasos.toFloat()) * 100
            }
            if(objetivos.kilometros != 0.0){
                progresoKm=(viewModel.actividad.kilometros.toFloat() / objetivos.kilometros.toFloat()) * 100
            }

            if(objetivos.calorias != 0.0){
                progresoCalorias=(viewModel.actividad.calorias.toFloat() / objetivos.calorias.toFloat()) * 100
            }
            binding.pgPasos.apply {
                progressMax = 100f
                setProgressWithAnimation(progresoPasos, 1000)
            }
            binding.pgKm.apply {
                progressMax = 100f
                setProgressWithAnimation(progresoKm, 1000)
            }
            binding.pgCalorias.apply {
                progressMax = 100f
                setProgressWithAnimation(progresoCalorias, 1000)
            }
            binding.tvProgresoPasos.text= progresoPasos.toInt().toString()+"%"
            binding.tvProgresoKm.text=progresoKm.toInt().toString()+"%"
            binding.tvProgresoCalorias.text=progresoCalorias.toInt().toString()+"%"
        }
    }

    private fun actualizarUI(objetivos: Objetivos) {
        binding.tvPasosObjetivos.text = objetivos.pasos.toString()
        binding.rsPasos.values= listOf(objetivos.pasos.toFloat())
        binding.tvKmObjetivos.text = objetivos.kilometros.toString()
        binding.rsKm.values= listOf(objetivos.kilometros.toFloat())
        binding.tvCaloriasObjetivos.text = objetivos.calorias.toString()
        binding.rsCalorias.values= listOf(objetivos.calorias.toFloat())
        actualizarCirculosProgreso()
    }
}