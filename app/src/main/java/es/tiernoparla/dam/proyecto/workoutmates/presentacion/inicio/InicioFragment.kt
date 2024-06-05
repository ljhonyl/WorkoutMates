package es.tiernoparla.dam.proyecto.workoutmates.presentacion.inicio

import android.Manifest
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import es.tiernoparla.dam.proyecto.workoutmates.R
import es.tiernoparla.dam.proyecto.workoutmates.databinding.FragmentInicioBinding
import es.tiernoparla.dam.proyecto.workoutmates.presentacion.MainActivity
import es.tiernoparla.dam.proyecto.workoutmates.presentacion.inicio.adapter.ActividadAdapter
import es.tiernoparla.dam.proyecto.workoutmates.servicios.sensores.ContadorActividadService

@AndroidEntryPoint
class InicioFragment : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!
    private lateinit var menuInferior: BottomNavigationView
    private var ejeYAnterior = 0
    private var ejeY = 0
    private val PERMISSION_REQUEST_CODE = 100

    private val viewModel: InicioViewModel by viewModels()
    private var serviceBinder: ContadorActividadService.ContadorActividadBinder? = null
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            serviceBinder = service as ContadorActividadService.ContadorActividadBinder
            serviceBinder?.inicioViewModel = viewModel
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuInferior = requireActivity().findViewById(R.id.nav_view)
        binding.recyclerActividadAnterior.isFocusable = false
        actulizarMenuInferior()
        iniciarComponentes()
        verificarPermisos()
        observarViewModel()
    }

    private fun actulizarMenuInferior() {
        if(isVisible){
            binding.scrollViewInicio.viewTreeObserver.addOnScrollChangedListener {
                if(isAdded) {
                    val valorActualEjeY = binding.scrollViewInicio.scrollY

                    (requireActivity() as MainActivity).manejarDesplazamientoMenu(ejeYAnterior, valorActualEjeY)

                    ejeYAnterior = valorActualEjeY
                }
            }
        }
    }

    private fun iniciarComponentes() {
        binding.recyclerActividadAnterior.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observarViewModel() {
        viewModel.nombreUsuario.observe(viewLifecycleOwner){
            binding.tvTituloHoy.text="$it hoy has realizado... "
        }
        viewModel.actividadesAnteriores.observe(viewLifecycleOwner) { actividades ->
            binding.recyclerActividadAnterior.adapter = ActividadAdapter(actividades)
        }
        viewModel.contadorPasos.observe(viewLifecycleOwner) { pasos ->
            binding.tvContadorPasosHoy.text = pasos.toString()
            viewModel.actualizarPasos(pasos)
        }
        viewModel.distanciaRecorrida.observe(viewLifecycleOwner) { distancia ->
            binding.tvContadorKilometrosHoy.text = distancia.toString()
            viewModel.actualizarKilometros(distancia)
        }
    }

    private fun verificarPermisos() {
        val sdkVersion = Build.VERSION.SDK_INT
        val permisosNecesarios = if (sdkVersion >= Build.VERSION_CODES.Q) {
            arrayOf(Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.BODY_SENSORS)
        } else if (sdkVersion >= Build.VERSION_CODES.M) {
            arrayOf(Manifest.permission.BODY_SENSORS)
        } else {
            // Android 5.1 (API nivel 22) o inferior no necesita permisos específicos para estos sensores
            return iniciarServicio()
        }

        val permisosNoConcedidos = permisosNecesarios.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }

        if (permisosNoConcedidos.isEmpty()) {
            iniciarServicio()
        } else {
            pedirPermisos(permisosNoConcedidos.toTypedArray())
        }
    }

    private fun pedirPermisos(permisos: Array<String>) {
        val msg = "Si no acepta los permisos, la aplicación podría no funcionar correctamente."
        val msg2 = "El permiso no ha sido aceptado, para cambiarlo accede a los ajustes de tu dispositivo."

        val permissionsToRequest = permisos.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isEmpty()) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Aviso")
                .setMessage(msg2)
                .setPositiveButton("OK") { _, _ -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        } else {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Aviso")
                .setMessage(msg)
                .setPositiveButton("OK") { _, _ ->
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        permissionsToRequest.toTypedArray(),
                        PERMISSION_REQUEST_CODE
                    )
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.any { it == PackageManager.PERMISSION_GRANTED }) {
                iniciarServicio()
            } else {
                Toast.makeText(requireContext(), "Permisos necesarios no concedidos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun iniciarServicio() {
        val intent = Intent(requireContext(), ContadorActividadService::class.java)
        requireContext().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        requireContext().startService(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.actualizarActividad()
        if (isBound) {
            requireContext().unbindService(connection)
            isBound = false
        }
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.actualizarActividad()
    }
}