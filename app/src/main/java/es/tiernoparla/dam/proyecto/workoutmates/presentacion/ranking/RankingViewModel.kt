package es.tiernoparla.dam.proyecto.workoutmates.presentacion.ranking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tiernoparla.dam.proyecto.workoutmates.dominio.casosdeuso.ConsultarActividad
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.Contacto
import es.tiernoparla.dam.proyecto.workoutmates.dominio.modelos.ContactoRanking
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val consultarActividad: ConsultarActividad
) : ViewModel() {
    private val _listaContactosConActividad = MutableLiveData<List<ContactoRanking>>()
    val listaContactosConActividad: LiveData<List<ContactoRanking>> get() = _listaContactosConActividad


    // Función para obtener la fecha actual formateada
    private fun obtenerFechaActualFormateada(): String {
        val formatoFecha = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatoFecha.format(Date())
    }

    fun cargarListaContactosConActividad(contactos: List<Contacto>) {
        viewModelScope.launch {
            val fechaActual = obtenerFechaActualFormateada()
            val listaContactosConActividad = mutableListOf<ContactoRanking>()


            for (contacto in contactos) {
                try {
                    //val numero=validarNumeroTelefono(contacto.numero)
                    val numero="+34632161009"
                    Log.d("Consulta",numero)
                    if(numero.length>9){
                        consultarActividad.getActividadContato(numero, fechaActual) { actividad ->
                            if (actividad != null) {
                                val contactoConActividad = ContactoRanking(contacto, actividad)
                                Log.d("TAG",contactoConActividad.toString())
                                listaContactosConActividad.add(contactoConActividad)
                                _listaContactosConActividad.postValue(listaContactosConActividad)
                            }
                        }
                    }

                } catch (e: Exception) {
                    Log.e("TAG", "Error al obtener actividad de contacto: ${e.message}")
                }
            }
        }
    }

    fun validarNumeroTelefono(numero: String): String {
        val numeroLimpio = numero.replace("[^0-9]".toRegex(), "") // Eliminar caracteres no numéricos
        return if (numeroLimpio.length < 11) {
            if (numeroLimpio.length == 9) {
                "+34$numeroLimpio" // Agregar prefijo internacional +34 si falta
            } else {
                "+$numeroLimpio"
            }
        } else {
            numeroLimpio // Número válido
        }
    }
}