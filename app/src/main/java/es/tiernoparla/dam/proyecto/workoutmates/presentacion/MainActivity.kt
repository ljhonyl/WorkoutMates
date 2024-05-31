package es.tiernoparla.dam.proyecto.workoutmates.presentacion

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import es.tiernoparla.dam.proyecto.workoutmates.R
import es.tiernoparla.dam.proyecto.workoutmates.WorkoutMatesApp
import es.tiernoparla.dam.proyecto.workoutmates.WorkoutMatesApp.Companion.preferencias
import es.tiernoparla.dam.proyecto.workoutmates.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var menuInferior: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actulizarInterfaz()
        observarEstadoSesion()
        menuInferior=binding.navView
    }

    fun actulizarInterfaz(){
        establecerGraficoDeNavegacion()
        mostrarEncabezado()
    }

    private fun establecerGraficoDeNavegacion() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        val numeroGuardado = preferencias.getNumero()
        Log.d("TAG", "Número guardado en preferencias: $numeroGuardado")

        val navGraph = navController.navInflater.inflate(
            if (numeroGuardado.isEmpty()) {
                Log.d("TAG", "No hay número guardado, se debe registrar")
                R.navigation.autenticacion_navigation // Gráfico de autenticación por defecto
            } else {
                binding.navView.visibility= View.VISIBLE
                NavigationUI.setupWithNavController(binding.navView, navController)
                Log.d("TAG", "Número encontrado, iniciar app")
                R.navigation.app_navigation // Gráfico principal si el usuario ha iniciado sesión
            }
        )
        navController.graph = navGraph
    }

    private fun observarEstadoSesion() {
        WorkoutMatesApp.sesionIniciada.observe(this, Observer {
            actulizarInterfaz()
        })
    }

    private fun mostrarEncabezado() {
        if (preferencias.getNumero().isNotEmpty()) {
            binding.clEncabezado.visibility = View.VISIBLE

            // Usar ConstraintSet para actualizar las restricciones
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.contenedorApp) // Clonar las restricciones actuales

            // Actualizar las restricciones del FragmentContainerView
            constraintSet.connect(
                R.id.navHostFragment,
                ConstraintSet.TOP,
                R.id.clEncabezado,
                ConstraintSet.BOTTOM
            )
            constraintSet.applyTo(binding.contenedorApp) // Aplicar las nuevas restricciones
            binding.tvSaludo.text= "Hola "+ preferencias.getNombre()
        }
    }

    fun manejarDesplazamientoMenu(valorAnteriorEjeY: Int, valorActualEjeY: Int) {
        if (valorActualEjeY > valorAnteriorEjeY && menuInferior.isShown) {
            ocultarMenuInferior()
        } else if (valorActualEjeY < valorAnteriorEjeY && menuInferior.visibility == View.GONE) {
            mostrarMenuInferior()
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
}
