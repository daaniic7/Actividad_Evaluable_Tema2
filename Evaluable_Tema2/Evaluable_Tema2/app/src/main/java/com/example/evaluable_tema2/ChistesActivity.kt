package com.example.evaluable_tema2

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ChistesActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    /**
     * TEXTOSPEECH PARA QUE UNA VOZ NOS NARRE LOS CHISTES QUE HAY EN LA SIGUIENTE LISTA
     */
    private lateinit var textToSpeech: TextToSpeech
    private val chistes = listOf(
        "¿Por qué los futbolistas nunca usan internet? Porque tienen miedo a las redes.",
        "¿Sabes cuál es el café más peligroso del mundo? ¡El ex-preso!",
        "¿Qué hace un perro con un taladro? ¡Taladrando!",
        "¿Por qué el reloj se deprimió? Porque siempre daba las mismas vueltas.",
        "¿Qué le dijo un semáforo a otro? ¡No me mires, me estoy cambiando!",
        "¿Por qué las neveras son tan buenas amigas? Porque siempre están llenas de buenas cosas.",
        "¿Qué pasa si tiras un pato al agua? ¡Nada!",
        "¿Cómo se llama un boomerang que no vuelve? ¡Palo!",
        "¿Qué hace una silla en medio del campo? ¡Silla espera!",
        "¿Por qué los celulares nunca tienen dinero? Porque siempre están en modo ahorro.",
        "¿Cómo se despide un espagueti? ¡Hasta la pasta!",
        "¿Qué hace un pez con una calculadora? ¡Nada cuentas!",
        "¿Por qué los astronautas no se pierden? Porque tienen estrella.",
        "¿Sabes cómo se llama un mago en la playa? ¡Mago de arena!",
        "¿Por qué las tortugas nunca cuentan secretos? Porque saben cerrar la boca.",
        "¿Qué le dijo el zapato al pie? ¡Estoy contigo en cada paso!",
        "¿Qué le dice un cable a otro? ¡Conéctate a la vida!",
        "¿Por qué las abejas siempre ganan? Porque tienen buena miel-todología.",
        "¿Por qué los vampiros nunca se broncean? ¡Porque se quiebran en la luz!",
        "¿Qué hace un electricista en el cine? ¡Enciende la trama!"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chistes)

        // Inicializa TextToSpeech
        textToSpeech = TextToSpeech(this, this)

        // Referencias a los elementos del diseño
        val btnEscuchar = findViewById<Button>(R.id.id_btn_escuchar)
        val btnVolver = findViewById<Button>(R.id.id_btn_volver)
        val progressBar = findViewById<ProgressBar>(R.id.id_progress_bar)

        // Configura el botón "Escuchar un chiste"
        btnEscuchar.setOnClickListener {
            // Oculta los botones y muestra el ProgressBar
            btnEscuchar.visibility = View.GONE
            btnVolver.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            // Selecciona un chiste aleatorio
            val chisteAleatorio = chistes.random()

            // Habla el chiste usando TextToSpeech
            textToSpeech.speak(chisteAleatorio, TextToSpeech.QUEUE_FLUSH, null, null)

            // Retraso para restaurar los botones después de narrar el chiste
            progressBar.postDelayed({
                progressBar.visibility = View.GONE
                btnEscuchar.visibility = View.VISIBLE
                btnVolver.visibility = View.VISIBLE
            }, 5000) // Ajusta según la duración promedio de los chistes
        }

        // Configura el botón "Volver"
        btnVolver.setOnClickListener {
            textToSpeech.stop() // Detén cualquier narración activa
            finish() // Finaliza la actividad y vuelve a la anterior
        }
    }

    // Inicialización de TextToSpeech
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Establece el idioma para TextToSpeech
            val result = textToSpeech.setLanguage(Locale("es", "ES")) // Español
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                println("El idioma seleccionado no es compatible.")
            }
        } else {
            println("Error al inicializar TextToSpeech.")
        }
    }

    // Libera recursos de TextToSpeech al cerrar la actividad
    override fun onDestroy() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
        super.onDestroy()
    }
}
