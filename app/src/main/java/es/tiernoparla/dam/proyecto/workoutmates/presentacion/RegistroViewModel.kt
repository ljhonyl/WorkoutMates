package es.tiernoparla.dam.proyecto.workoutmates.presentacion

import android.app.Activity
import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class RegistroViewModel (aplication: Application): AndroidViewModel(aplication) {

    private lateinit var navController: NavController
    private lateinit var numero: String
    private lateinit var nombre: String
    private var mAuth: FirebaseAuth=FirebaseAuth.getInstance()
    private val _cargando = MutableLiveData<Boolean>()
    val cargando: LiveData<Boolean> = _cargando

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("TAG", "Código de verificación recibido: ${credential.smsCode}")
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        Log.d("TAG", "autoverificacion")
                    } else {
                        // Manejar el fallo de inicio de sesión
                    }
                }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            _cargando.value=false
            Toast.makeText(getApplication(), e.message, Toast.LENGTH_SHORT).show()
            Log.d("TAG", "Verificación automatica fallida")
        }

        override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken) {
            _cargando.value=false
            navigateToVerificacionFragment(verificationId)
        }
    }

    fun iniciarVerificacion(activity: Activity, nombre: String, numero: String, navController: NavController) {
        this.numero = numero
        this.nombre=nombre
        this.navController = navController
        _cargando.value=true

        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(this.numero)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun navigateToVerificacionFragment(verificationId: String) {
        navController.navigate(RegistroFragmentDirections.actionRegistroFragmentToVerificacionFragment(numero = numero, verificationId = verificationId, nombre = nombre))
    }
}