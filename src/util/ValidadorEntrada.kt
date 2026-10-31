package util

/**
 * Objeto utilitario encargado de gestionar y validar las entradas por consola del usuario.
 * Implementa el manejo de excepciones (try-catch) dentro de bucles interactivos (while)
 * para garantizar que el usuario ingrese datos válidos sin interrumpir la ejecución
 * ni forzar valores por defecto indeseados.
 */
object ValidadorEntrada {

    /**
     * Solicita un número entero dentro de un rango específico [min, max].
     * Repite la solicitud hasta que se ingrese una opción válida.
     */
    fun leerEnteroEnRango(mensaje: String, min: Int, max: Int): Int {
        while (true) {
            try {
                print(mensaje)
                val linea = readLine()?.trim() ?: throw IllegalArgumentException("La entrada no puede estar vacía.")
                val valor = linea.toIntOrNull() ?: throw IllegalArgumentException("Debe ingresar un número entero válido.")

                if (valor !in min..max) {
                    throw IndexOutOfBoundsException("Opción fuera de rango. Debe seleccionar entre $min y $max.")
                }
                return valor
            } catch (e: Exception) {
                println("⚠️ Error de entrada: ${e.message} Por favor, intente nuevamente.")
            }
        }
    }

    /**
     * Solicita una cantidad entera positiva (> 0).
     * Repite la solicitud hasta que se ingrese un valor válido.
     */
    fun leerEnteroPositivo(mensaje: String): Int {
        while (true) {
            try {
                print(mensaje)
                val linea = readLine()?.trim() ?: throw IllegalArgumentException("La entrada no puede estar vacía.")
                val valor = linea.toIntOrNull() ?: throw IllegalArgumentException("La cantidad debe ser un número entero.")

                if (valor <= 0) {
                    throw IllegalArgumentException("La cantidad debe ser un número mayor a 0.")
                }
                return valor
            } catch (e: Exception) {
                println("⚠️ Error de entrada: ${e.message} Por favor, intente nuevamente.")
            }
        }
    }

    /**
     * Solicita un texto obligatorio que no puede estar vacío ni compuesto solo de espacios.
     */
    fun leerTextoNoVacio(mensaje: String, nombreCampo: String = "campo"): String {
        while (true) {
            try {
                print(mensaje)
                val linea = readLine()?.trim()
                if (linea.isNullOrBlank()) {
                    throw IllegalArgumentException("El $nombreCampo no puede estar vacío.")
                }
                return linea
            } catch (e: Exception) {
                println("⚠️ Error de entrada: ${e.message} Por favor, intente nuevamente.")
            }
        }
    }

    /**
     * Solicita un número de teléfono opcional.
     * Si el usuario presiona Enter sin escribir nada, retorna null (ideal para compras presenciales).
     * Si ingresa un valor, valida que solo contenga dígitos numéricos y su longitud esté entre 8 y 11 caracteres.
     */
    fun leerTelefonoOpcional(mensaje: String): String? {
        while (true) {
            try {
                print(mensaje)
                val telefono = readLine()?.trim()
                if (telefono.isNullOrBlank()) {
                    return null
                }
                if (!telefono.all { it.isDigit() }) {
                    throw IllegalArgumentException("El teléfono solo debe contener dígitos numéricos.")
                }
                if (telefono.length !in 8..11) {
                    throw IllegalArgumentException("El teléfono debe contener entre 8 y 11 dígitos.")
                }
                return telefono
            } catch (e: Exception) {
                println("⚠️ Error de entrada: ${e.message} Por favor, intente nuevamente o presione Enter para omitir.")
            }
        }
    }

    /**
     * Solicita un número de teléfono válido (solo dígitos y longitud entre 8 y 11 caracteres).
     */
    fun leerTelefono(mensaje: String): String {
        while (true) {
            try {
                print(mensaje)
                val telefono = readLine()?.trim()
                if (telefono.isNullOrBlank()) {
                    throw IllegalArgumentException("El teléfono no puede estar vacío.")
                }
                if (!telefono.all { it.isDigit() }) {
                    throw IllegalArgumentException("El teléfono solo debe contener dígitos numéricos.")
                }
                if (telefono.length !in 8..11) {
                    throw IllegalArgumentException("El teléfono debe contener entre 8 y 11 dígitos.")
                }
                return telefono
            } catch (e: Exception) {
                println("⚠️ Error de entrada: ${e.message} Por favor, intente nuevamente.")
            }
        }
    }

    /**
     * Solicita un correo electrónico opcional.
     * Permite dejar el campo en blanco presionando Enter (retorna null), ideal para clientes presenciales o adultos mayores que no poseen email.
     * Si el usuario escribe algo, valida su estructura con regex dentro de un bucle try-catch hasta que sea correcto.
     */
    fun leerEmailOpcional(mensaje: String): String? {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        while (true) {
            try {
                print(mensaje)
                val email = readLine()?.trim()
                if (email.isNullOrBlank()) {
                    return null
                }
                if (!emailRegex.matches(email)) {
                    throw IllegalArgumentException("El formato del correo es inválido (ejemplo: usuario@dominio.com).")
                }
                return email
            } catch (e: Exception) {
                println("⚠️ Error de entrada: ${e.message} Por favor, intente nuevamente o presione Enter para omitir.")
            }
        }
    }

    /**
     * Solicita un correo electrónico obligatorio y valida su estructura mediante una expresión regular.
     */
    fun leerEmail(mensaje: String): String {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        while (true) {
            try {
                print(mensaje)
                val email = readLine()?.trim()
                if (email.isNullOrBlank()) {
                    throw IllegalArgumentException("El correo electrónico no puede estar vacío.")
                }
                if (!emailRegex.matches(email)) {
                    throw IllegalArgumentException("El formato del correo es inválido (ejemplo: usuario@dominio.com).")
                }
                return email
            } catch (e: Exception) {
                println("⚠️ Error de entrada: ${e.message} Por favor, intente nuevamente.")
            }
        }
    }

    /**
     * Solicita una entrada opcional (puede dejarse en blanco presionando Enter, retornando null).
     */
    fun leerTextoOpcional(mensaje: String): String? {
        print(mensaje)
        val linea = readLine()?.trim()
        return if (linea.isNullOrBlank()) null else linea
    }

    /**
     * Solicita una confirmación 's' o 'n' al usuario.
     */
    fun leerConfirmacion(mensaje: String): Boolean {
        while (true) {
            print(mensaje)
            val respuesta = readLine()?.trim()?.lowercase()
            when (respuesta) {
                "s", "si", "sí" -> return true
                "n", "no" -> return false
                else -> println("⚠️ Opción no válida. Por favor responda con 's' (sí) o 'n' (no).")
            }
        }
    }
}
