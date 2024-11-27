package com.example.evaluable_tema2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.evaluable_tema2.databinding.ActivityDadosBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class DadosActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityDadosBinding
    private var sum: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityDadosBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

        // Configuración inicial
        bindingMain.btnIniciarJuego.setOnClickListener {
            val numeroAdivinado = bindingMain.etNumeroAdivinar.text.toString().toIntOrNull()

            if (numeroAdivinado == null || numeroAdivinado !in 2..12) {
                Toast.makeText(this, "Introduce un número válido entre 2 y 12", Toast.LENGTH_SHORT).show()
            } else {
                // Ocultar la configuración inicial y mostrar el juego
                bindingMain.configContainer.visibility = View.GONE
                bindingMain.gameContainer.visibility = View.VISIBLE

                initEvent(numeroAdivinado)
            }
        }

        // Botón de ajustes
        bindingMain.btnAjustes.setOnClickListener {
            // Mostrar nuevamente el contenedor de configuración
            bindingMain.configContainer.visibility = View.VISIBLE
            bindingMain.gameContainer.visibility = View.GONE

            // Restaurar elementos ocultos
            resetGame()
        }

        // Botón para volver al MainActivity
        bindingMain.btnMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initEvent(numeroAdivinado: Int) {
        // Ocultar el resultado inicialmente
        bindingMain.txtResultado.visibility = View.INVISIBLE

        bindingMain.imageButton.setOnClickListener {
            // Animación del vaso
            val shakeAndRotateAnimation = AnimationUtils.loadAnimation(this, R.anim.shake)
            bindingMain.imageButton.startAnimation(shakeAndRotateAnimation)

            // Listener para la animación del vaso
            shakeAndRotateAnimation.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
                override fun onAnimationStart(animation: android.view.animation.Animation?) {}

                override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                    // Lanzar los dados después de que termine la animación del vaso

                    // Animación para el primer dado
                    val diceDropAnimation = AnimationUtils.loadAnimation(this@DadosActivity, R.anim.dado)
                    bindingMain.imagviewDado1.startAnimation(diceDropAnimation)

                    // Listener para animación del primer dado
                    diceDropAnimation.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
                        override fun onAnimationStart(animation: android.view.animation.Animation?) {}

                        override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                            // Animación para el segundo dado
                            val diceDropAnimation2 = AnimationUtils.loadAnimation(this@DadosActivity, R.anim.dado)
                            bindingMain.imagviewDado2.startAnimation(diceDropAnimation2)

                            // Listener para animación del segundo dado
                            diceDropAnimation2.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
                                override fun onAnimationStart(animation: android.view.animation.Animation?) {}

                                override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                                    // Mostrar el resultado después de animar los dados
                                    bindingMain.txtResultado.visibility = View.VISIBLE
                                    game(numeroAdivinado)
                                }

                                override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
                            })
                        }

                        override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
                    })
                }

                override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
            })
        }
    }

    /**
     * FUNCION QUE INICIA EL JUEGO
     */
    private fun game(numeroAdivinado: Int) {
        val schedulerExecutor = Executors.newSingleThreadScheduledExecutor()
        val msc = 1000

        for (i in 1..2) { // Solo dos dados
            schedulerExecutor.schedule(
                { throwDadoInTime() },
                (msc * i).toLong(),
                TimeUnit.MILLISECONDS
            )
        }
        schedulerExecutor.schedule(
            { viewResult(numeroAdivinado) },
            (msc * 3).toLong(),
            TimeUnit.MILLISECONDS
        )
        schedulerExecutor.shutdown()
    }

    /**
     * FUNCION PARA LANZAR LOS DADOS
     */
    private fun throwDadoInTime() {
        val numDados = Array(2) { Random.nextInt(1, 7) }
        val imagViews: Array<ImageView> = arrayOf(
            bindingMain.imagviewDado1,
            bindingMain.imagviewDado2
        )

        sum = numDados.sum()

        for (i in imagViews.indices) {
            imagViews[i].setImageResource(getDadoImage(numDados[i]))
        }

        runOnUiThread {
            bindingMain.txtResultado.text = "Suma: $sum"
        }
    }

    private fun getDadoImage(numero: Int): Int {
        return when (numero) {
            1 -> R.drawable.dado1
            2 -> R.drawable.dado2
            3 -> R.drawable.dado3
            4 -> R.drawable.dado4
            5 -> R.drawable.dado5
            6 -> R.drawable.dado6
            else -> R.drawable.dado1
        }
    }

    /**
     * MENSAJES EN FORMA DE TOAST QUE MUESTRAN SI HEMOS GANADO O PERDIDO
     */
    private fun viewResult(numeroAdivinado: Int) {
        runOnUiThread {
            if (sum == numeroAdivinado) {
                Toast.makeText(this, "¡Felicidades, has ganado!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Fallaste vuelve a intentarlo.", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * FUNCION PARA RESETEAR EL JUEGO
     */
    private fun resetGame() {
        // Restaurar elementos del juego
        bindingMain.imageButton.visibility = View.VISIBLE
        bindingMain.imagviewDado1.visibility = View.VISIBLE
        bindingMain.imagviewDado2.visibility = View.VISIBLE
        bindingMain.txtResultado.visibility = View.INVISIBLE
    }
}
