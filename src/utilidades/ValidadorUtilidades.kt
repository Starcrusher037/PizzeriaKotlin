package utilidades

/**
 * Objeto utilitario encargado de validaciones basadas en Expresiones Regulares (Regex)
 * y Verificaciones de Rangos (Ranges) segun el Paso 1.
 */
object ValidadorUtilidades {

    /**
     * Expresion regular estandar para validar direcciones de correo electronico (nombre@dominio.com).
     * Asegura la presencia de usuario, '@', subdominio y extension de al menos 2 letras.
     */
    val REGEX_EMAIL: Regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    /**
     * Rango permitido para la cantidad de productos en un pedido (Paso 1: Ranges 1..100).
     */
    val RANGO_CANTIDAD_PRODUCTOS: IntRange = 1..100

    /**
     * Valida si un texto cumple con el formato estandar de email mediante Regex.
     */
    fun validarEmail(email: String): Boolean {
        return REGEX_EMAIL.matches(email.trim())
    }

    /**
     * Valida si una cantidad entera se encuentra dentro del rango permitido (1..100).
     */
    fun validarCantidadEnRango(cantidad: Int): Boolean {
        return cantidad in RANGO_CANTIDAD_PRODUCTOS
    }

    /**
     * Solicita por consola un correo electronico obligatorio, validando que cumpla el formato estandar.
     * Si el formato es incorrecto, muestra un mensaje de error y solicita al usuario que lo reingrese.
     */
    fun leerEmailObligatorio(mensaje: String = "Ingrese correo electronico del cliente (nombre@dominio.com): "): String {
        while (true) {
            print(mensaje)
            val entrada = readLine()?.trim()
            if (entrada.isNullOrBlank()) {
                println("[ERROR] El correo electronico no puede estar vacio. Por favor, reingrese.")
                continue
            }
            if (validarEmail(entrada)) {
                return entrada
            } else {
                println("[ERROR] Formato de correo electronico invalido (debe cumplir: nombre@dominio.com). Por favor, reingrese.")
            }
        }
    }

    /**
     * Solicita por consola un correo electronico opcional.
     * Si el usuario presiona Enter sin contenido, retorna null.
     * Si escribe un valor, valida su estructura con Regex; si es invalido, exige reingreso.
     */
    fun leerEmailOpcional(mensaje: String = "Ingrese correo electronico (Enter para omitir): "): String? {
        while (true) {
            print(mensaje)
            val entrada = readLine()?.trim()
            if (entrada.isNullOrBlank()) {
                return null
            }
            if (validarEmail(entrada)) {
                return entrada
            } else {
                println("[ERROR] Formato de correo electronico invalido (debe cumplir: nombre@dominio.com). Por favor, intente nuevamente o presione Enter para omitir.")
            }
        }
    }

    /**
     * Solicita por consola la cantidad de productos a ordenar evaluando si cae dentro
     * del rango permitido (1..100). Si no, muestra un mensaje de error y solicita un valor valido.
     */
    fun leerCantidadProductosEnRango(mensaje: String = "Ingrese cantidad deseada (1-100): "): Int {
        while (true) {
            print(mensaje)
            val entrada = readLine()?.trim()
            val numero = entrada?.toIntOrNull()

            if (numero == null) {
                println("[ERROR] Debe ingresar un numero entero valido. Por favor, intente nuevamente.")
                continue
            }

            // Evaluacion de rango utilizando el operador 'in'
            if (numero !in RANGO_CANTIDAD_PRODUCTOS) {
                println("[ERROR] Cantidad fuera de rango permitido. Debe ingresar un valor entre ${RANGO_CANTIDAD_PRODUCTOS.first} y ${RANGO_CANTIDAD_PRODUCTOS.last}. Por favor, intente nuevamente.")
                continue
            }

            return numero
        }
    }
}
