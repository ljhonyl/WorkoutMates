package es.tiernoparla.dam.proyecto.workoutmates.presentacion.ranking

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.tiernoparla.dam.proyecto.workoutmates.databinding.FragmentRankingBinding
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Contacto
import es.tiernoparla.dam.proyecto.workoutmates.presentacion.ranking.adapter.RankingAdapter

/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class RankingFragment : Fragment() {
    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RankingViewModel by viewModels()

    private val PERMISSION_REQUEST_CODE=1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iniciarComponentes()
        verificarPermisos()
        Log.d("TAG", viewModel.toString())
    }

    private fun iniciarComponentes() {
        binding.recyclerRanking.layoutManager = LinearLayoutManager(requireContext())
    }

    /**
     * Solicitud de permisos
     */
    private fun verificarPermisos(){
        //Permisos concedidos, se prodecerá a insertar
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED){
            cargarContactos()
        }
        //Permisos no concedidos aunque tampoco rechazados(no haber preguntado por ellos)
        else{
            pedirPermisos()
        }
    }

    private fun pedirPermisos() {
        val msg = "Para mostrar la lista de contactos de Workoutmates, necesitamos acceder a sus contactos"
        val msg2 = "El permiso de contactos no ha sido aceptado. Para cambiarlo, acceda a los ajustes de su dispositivo."
        //Permisos ya rechazados, igualemnte se insertará el personaje
        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("AVISO")
                .setMessage(msg2)
                .setPositiveButton("Ok") { _, _ ->
                    // no se han dado los permisos, no se puede seguir con la funcionalidad
                }
            val dialog: AlertDialog= builder.create()
            dialog.show()
        }
        //Los permisos no estan rechazados, se pregunta si aceptarlos o no
        else{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("AVISO")
                .setMessage(msg)
                .setPositiveButton("Ok") { _, _ ->
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),PERMISSION_REQUEST_CODE)
                }
            val dialog: AlertDialog= builder.create()
            dialog.show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            cargarContactos()
        } else {
            // Permiso denegado, mostrar mensaje al usuario (opcional)
            Toast.makeText(requireContext(), "Permiso de contactos denegado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarContactos() {
        val contactos = obtenerContactos()
        Log.d("TAG", "Hasta aqui funciona")
        if (contactos.isNotEmpty()) {
            Log.d("TAG", "rama true")
            viewModel.cargarListaContactosConActividad(contactos)

            viewModel.listaContactosConActividad.observe(viewLifecycleOwner) { listacontactosconactividad ->
                binding.recyclerRanking.adapter = RankingAdapter(listacontactosconactividad)
            }
        } else {
            // Manejar el caso en el que no se obtienen contactos
            Log.d("TAG", "rama else")
            Toast.makeText(requireContext(), "No se encontraron contactos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerContactos(): List<Contacto> {
        val contactos = mutableListOf<Contacto>()
        val resolver = requireActivity().contentResolver
        val cursor = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use { cursor ->
            val nombreIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numeroIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (cursor.moveToNext()) {
                val nombre = cursor.getString(nombreIndex)
                val numero = cursor.getString(numeroIndex).replace(" ", "")
                contactos.add(Contacto(nombre, numero))
            }
        }

        cursor?.close()
         for (contacto in contactos){
             Log.d("Contacto",contacto.toString())
         }

        return contactos
    }
}