package com.example.calculadora

import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculadora.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityMainBinding
    var num1: Float = 0.0f
    var num2: Float = 0.0f
    var operador: String = ""
    var btnEqualsPulsado: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //inicializar las variables en savedInstanceState para guardar los datos y mostrarlos en la pantalla si el usuario gire el móvil
        //el programa va a comprobar si se ha llamado este metodo, y si las variables tienen valor
        num1 = savedInstanceState?.getFloat("num1") ?: 0.0f
        num2 = savedInstanceState?.getFloat("num2") ?: 0.0f
        operador = savedInstanceState?.getString("operador") ?: ""
        btnEqualsPulsado = savedInstanceState?.getBoolean("btnEqualsPulsado") ?: false
        binding.mostrarResultado.text = savedInstanceState?.getString("mostrarResultado") ?: "0"
        binding.textOperaciones.text = savedInstanceState?.getString("textOperaciones") ?: ""

        binding.btnOne.setOnClickListener(this)
        binding.btnTwo.setOnClickListener(this)
        binding.btnThree.setOnClickListener(this)
        binding.btnFour.setOnClickListener(this)
        binding.btnFive.setOnClickListener(this)
        binding.btnSix.setOnClickListener(this)
        binding.btnSeven.setOnClickListener(this)
        binding.btnEight.setOnClickListener(this)
        binding.btnNine.setOnClickListener(this)
        binding.btnZero.setOnClickListener(this)
        binding.btnDot.setOnClickListener(this)
        binding.btnClear.setOnClickListener(this)
        binding.btnAdd.setOnClickListener(this)
        binding.btnSubtract.setOnClickListener(this)
        binding.btnMultiply.setOnClickListener(this)
        binding.btnDivide.setOnClickListener(this)
        binding.btnEquals.setOnClickListener(this)

        //botones para landscape
        binding.btnExponent?.setOnClickListener(this)
        binding.btnPercent?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            //para los números y el punto - lo que se mostrará en la pantalla
            binding.btnOne.id -> mostrarEnPantalla("1") //se llama el metodo para mostrar los números en la pantalla, y añadir un número después del otro
            binding.btnTwo.id -> mostrarEnPantalla("2")
            binding.btnThree.id -> mostrarEnPantalla("3")
            binding.btnFour.id -> mostrarEnPantalla("4")
            binding.btnFive.id -> mostrarEnPantalla("5")
            binding.btnSix.id -> mostrarEnPantalla("6")
            binding.btnSeven.id -> mostrarEnPantalla("7")
            binding.btnEight.id -> mostrarEnPantalla("8")
            binding.btnNine.id -> mostrarEnPantalla("9")
            binding.btnZero.id -> mostrarEnPantalla("0")

            binding.btnDot.id -> {
                val textoQueSeVeAhora = binding.mostrarResultado.text.toString()

                //si la cadena antes de pulsar "." está vacía, se añadirá un 0 delante del "."; si es igual a cero, aparecerá "0."
                if (textoQueSeVeAhora.isEmpty() || textoQueSeVeAhora == "0") {
                    mostrarEnPantalla("0.")
                }
                //si no hay puntos, se añadirá uno después del número
                else if (!textoQueSeVeAhora.contains(".")) {
                    mostrarEnPantalla(".")
                }
            }

            binding.btnClear.id -> limpiarClear()

            //operaciones con los números
            binding.btnAdd.id -> {
                operadorPulsado("+") //se asigna el operador de la función lambda para realizar las operaciones
            }

            binding.btnSubtract.id -> {
                operadorPulsado("-")
            }

            binding.btnMultiply.id -> {
                operadorPulsado("*")
            }

            binding.btnDivide.id -> {
                operadorPulsado("/")
            }

            binding.btnExponent?.id -> {
                operadorPulsado("E")
            }

            binding.btnPercent?.id -> {
                operadorPulsado("%")
            }

            binding.btnEquals.id -> {
                calcular()
                btnEqualsPulsado = true
                operador = "" //para empezar las operaciones de nuevo
            }
        }//when
    }//onClick

    //función para guardar el num1 después de que se pulse el botón de alguna operación
    private fun operadorPulsado(operator: String) {
        operador = operator
        num1 = binding.mostrarResultado.text.toString().toFloat() //se guarda el numero en la pantalla como num1 cuando el usuario pulsa el botón una alguna operación
        binding.mostrarResultado.text = "" //se limpia el campo para introducir el siguiente número
        //en el TextView a la izquierda se apuntan todos los números y operadores para seguir los cálculos que se han realizado
        binding.textOperaciones.text = binding.textOperaciones.text.toString() + "${operator}"
    }

    //función para mostrar los números en el TextView (principal, a la derecha). Se convertirán en una cadena. Los números van a aparecer uno detrás otro, según se pulsan los botónes
    fun mostrarEnPantalla (num: String){
        val numEnPantalla = binding.mostrarResultado.text.toString()
        /*
          añadir ' && num != "." ' para que funcione correctamente la condición de binding.btnDot.id,
          especialmente si pulsar el punto sin tener pulsado otro número (por ejemplo: pulsar ".2" -> se muestra "0.2"
        */
        if (numEnPantalla == "0" && num != ".") {
            binding.mostrarResultado.text = num
            binding.textOperaciones.text = binding.textOperaciones.text.toString() + num
        } else if (num == "." && numEnPantalla.contains(".")) {
            //no va a aparecer nada en las pantallas si el usuario intente a pulsar el punto dos o más veces seguidas (por ejemplo, si pulsar: 2... -> se ve: 2. y el programa espera siguiente número)
            return
        } else {
            binding.mostrarResultado.text = numEnPantalla + num //permite añadir un nuevo número pulsando botón, sin que el número anterior se borre
            binding.textOperaciones.text = binding.textOperaciones.text.toString() + num //lo mismo aparece en el TextView de la izquierda
        }
    }

    //resetea los valores de las variables
    fun limpiarClear(){
        binding.mostrarResultado.text = "0"
        binding.textOperaciones.text = "" //esto está "escondido" hasta que el usuario empiece a pulsar botones: números y operaciones
        num1 = 0.0f
        num2 = 0.0f
        operador = ""
        btnEqualsPulsado = false
        //establecer el tamaño del texto dependiendo de la orientación del móvil
        binding.mostrarResultado.textSize = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            45f
        }
        else {
            29f //el tamaño de la fuente más pequeña para el landscape
        }
        //volver el cero que aparece después de pulsar Clear a su posición inicial, ya que si el resultado contiene más de 7 simbolos, se baja un poco para no tapar el área con las operaciones
        binding.mostrarResultado.gravity = Gravity.END
    }

    //función lambda para realizar la operaciones con los números
    fun operaciones(num1: Float?, num2: Float?, lambda: (Float, Float) -> Float): Float {
        return lambda(num1!!, num2!!)
    }
    //variables para las operaciones en concreto
    val sumar: (Float, Float) -> Float = {num1, num2 -> num1 + num2}
    val restar: (Float, Float) -> Float = {num1, num2 -> num1 - num2}
    val multiplicar: (Float, Float) -> Float = {num1, num2 -> num1 * num2}
    val dividir: (Float, Float) -> Float = {num1, num2 ->
        if (num2 != 0.0f) {
            num1 / num2
        } else {
            mostrarSnackbarCero() //se mostrará un Snackbar con el aviso sobre la división entre cero
            Float.NaN //devuelve NaN y habrá que limpiar la pantalla para realizar la siguiente operación
        }
    }

    //operaciones para landscape
    val exponer: (Float, Float) -> Float = {num1, num2 -> Math.pow(num1.toDouble(), num2.toDouble()).toFloat()} //convertir en Double para poder exponer el número, y luego de nuevo a Float
    val porcentaje: (Float, Float) -> Float = {num1, num2 -> num1 * num2 / 100} // solo se puede saber el porcentaje (num1) del otro numero (num2)

    //función para hacer las operaciones
    //para realizar las operaciones con más de 2 operandos, hay que pulsar "=" después de cada operación para que el resultado se guarde como num1
    fun calcular () {
            if (num1 != null && num2 != null) {
                //si num1 no está vacío, el número en la pantalla se guarda como num2 y se mostrará en ambos TextView
                num2 = binding.mostrarResultado.text.toString().toFloat()
                binding.textOperaciones.text = binding.textOperaciones.text

                    //se crea una variable con el resultado de la operación con el num1 y num2
                    val resultado = when (operador) {
                        "+" -> operaciones(num1, num2, sumar)
                        "-" -> operaciones(num1, num2, restar)
                        "*" -> operaciones(num1, num2, multiplicar)
                        "/" -> operaciones(num1, num2, dividir)
                        "E" -> operaciones(num1, num2, exponer)
                        "%" -> operaciones(num1, num2, porcentaje)
                        else -> num2
                    }
                binding.mostrarResultado.text = mostrarFormatoCorrecto(resultado) //se llama el metodo que quitará decimales si trabajamos con los numeros enteros
                num1 = resultado //asignar resultado a num1 para poder realizar las operaciones con más operandos
                operador = "" //resetear el operador
            }
    }//calcular

    //función para saber si se trata de números enteros o decimales, y mostrar el resultado en el formato correcto
    fun mostrarFormatoCorrecto(resultado: Float?): String {
        //si hay más de 7 símbolos en el resultado, su tamaño de la fuente será más pequeño y se mostrará más abajo, para no tapar el TextView de la izquierda con las operaciones realizadas
        if (resultado.toString().length > 7){
            binding.mostrarResultado.textSize = 25f
            //ajustar la gravedad por si el número es muy largo, se mostrará más abajo
            binding.mostrarResultado.gravity = Gravity.BOTTOM or Gravity.END
        }
        return if (resultado!! % 1 == 0.0f) {
            resultado.toInt().toString() //si el número no es decimal, se mostrará sin el punto ni ceros
        } else {
            resultado.toString()
        }
    }

    //Snackbar para que se muestre un mensaje de error si el usuario intente dividir entre 0
    fun mostrarSnackbarCero() {
        Snackbar.make(binding.root, "No se puede dividir entre 0", Snackbar.LENGTH_SHORT).show()
    }

    //se guardará el estado de la app: los valores de las variables y de lo que se muestra en las pantallas.
    //Si el usuario gire el dispositivo, no se resetearán los valores y se puede continuar con las operaciones
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat("num1", num1)
        outState.putFloat("num2", num2)
        outState.putString("operador", operador)
        outState.putBoolean("btnEqualsPulsado", btnEqualsPulsado)
        outState.putString("mostrarResultado", binding.mostrarResultado.text.toString())
        outState.putString("textOperaciones", binding.textOperaciones.text.toString())
    }
}//Main