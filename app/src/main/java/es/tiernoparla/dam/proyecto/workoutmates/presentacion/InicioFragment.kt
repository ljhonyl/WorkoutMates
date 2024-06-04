package es.tiernoparla.dam.proyecto.workoutmates.presentacion

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.tiernoparla.dam.proyecto.workoutmates.ContadorActividadService
import es.tiernoparla.dam.proyecto.workoutmates.R
import es.tiernoparla.dam.proyecto.workoutmates.databinding.FragmentInicioBinding
import es.tiernoparla.dam.proyecto.workoutmates.presentacion.adapter.ActividadAdapter
import kotlin.math.sqrt

class InicioFragment : Fragment(){

    private var _binding:FragmentInicioBinding?=null
    private val binding get() = _binding!!
    private lateinit var menuInferior: BottomNavigationView
    private var ejeY = 0
    private val PERMISSION_REQUEST_CODE = 100

    companion object {
        fun newInstance() = InicioFragment()
    }

    private val viewModel: InicioViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        val root = binding.root

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Acceder al menu inferior desde MainActivity, el fragmento esta contenido en la Activity
        menuInferior = requireActivity().findViewById(R.id.nav_view)
        //El recycler view estaba atrayendo el foco y no es el comportamiento deseado
        binding.recyclerActividadAnterior.isFocusable = false
        actulizarEstadoMenuInferior()
        iniciarComponentes()
        observarViewModel()
        comprobarPermisos()
    }

    fun actulizarEstadoMenuInferior(){
        binding.scrollViewInicio.viewTreeObserver.addOnScrollChangedListener {
            val valorActualEjeY = binding.scrollViewInicio.scrollY // Obtener el desplazamiento vertical actual

            if (valorActualEjeY > ejeY && menuInferior.isShown) {
                // Ocultar el menu inferior cuando se desplaza hacia abajo
                ocultarMenuInferior()
            } else if (valorActualEjeY < ejeY && menuInferior.visibility == View.GONE) {
                // Mostrar el menu inferior cuando se desplaza hacia arriba
                mostrarMenuInferior()
            }

            ejeY = valorActualEjeY // Actualizar el valor del eje Y
        }
    }

    private fun ocultarMenuInferior() {
        menuInferior.animate()
            .translationY(menuInferior.height.toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    menuInferior.visibility = View.GONE
                }
            })
    }

    private fun mostrarMenuInferior() {
        menuInferior.visibility = View.VISIBLE
        menuInferior.animate()
            .translationY(0f)
            .setListener(null)
    }

    private fun iniciarComponentes(){
        binding.recyclerActividadAnterior.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observarViewModel(){
        viewModel.actividadesAnteriores.observe(viewLifecycleOwner) { actividades ->
            binding.recyclerActividadAnterior.adapter = ActividadAdapter(actividades)
        }
        viewModel.contadorPasos.observe(viewLifecycleOwner){pasos ->
            binding.tvContadorPasosHoy.text=pasos.toString()

        }
        viewModel.distanciaRecorrida.observe(viewLifecycleOwner){distancia ->
            binding.tvContadorKilometrosHoy.text=distancia.toString()
        }
    }

    private fun comprobarPermisos() {
        val requiredPermissions = arrayOf(
            android.Manifest.permission.ACTIVITY_RECOGNITION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
        } else {
            permisosConcedidos()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                permisosConcedidos()
            } else {
                Toast.makeText(requireContext(), "Permisos necesarios no concedidos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun permisosConcedidos() {
        val intent = Intent(requireContext(), ContadorActividadService::class.java)
        requireContext().startService(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}