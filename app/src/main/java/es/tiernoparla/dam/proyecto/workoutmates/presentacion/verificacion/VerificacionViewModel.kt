package es.tiernoparla.dam.proyecto.workoutmates.presentacion.verificacion

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tiernoparla.dam.proyecto.workoutmates.WorkoutMatesApp
import es.tiernoparla.dam.proyecto.workoutmates.WorkoutMatesApp.Companion.preferencias
import javax.inject.Inject

@HiltViewModel
class VerificacionViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    private lateinit var navController: NavController
    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var numero: String
    private lateinit var nombre: String

    // LiveData para notificar un evento de falla en la verificación
    private val _verificationFailedEvent = MutableLiveData<Unit>()
    val verificationFailedEvent: LiveData<Unit>
        get() = _verificationFailedEvent

    /**
     * Método para manejar el clic en el botón de verificación.
     * @param activity Actividad actual.
     * @param navController NavController para la navegación.
     * @param verificationId ID de verificación recibido.
     * @param codigo Código de verificación ingresado por el usuario.
     */
    fun onVerificarseClicked(numero: String, nombre: String, activity: Activity, navController: NavController, verificationId: String, codigo: String) {
        this.numero=numero
        this.nombre=nombre
        this.navController = navController
        val credential = PhoneAuthProvider.getCredential(verificationId, codigo)
        signInWithPhoneAuthCredential(credential)
    }

    /**
     * Método para iniciar sesión con las credenciales de verificación del teléfono.
     * @param credential Credencial de verificación del teléfono.
     */
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso, actualizar UI con la información del usuario
                    preferencias.guardarNombre(nombre)
                    preferencias.guardarNumero(numero)
                    WorkoutMatesApp.actualizarSesionIniciada(true)
                } else {
                    // Fallo en el inicio de sesión, mostrar un mensaje y actualizar la UI
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // El código de verificación ingresado es inválido
                    }
                    // Actualizar la UI según sea necesario
                }
            }
    }
}
