package util

import utilidades.FormatoUtilidades
import utilidades.ValidadorUtilidades

/**
 * Objeto utilitario encargado de gestionar y validar las entradas por consola del usuario.
 * Implementa el manejo de excepciones (try-catch) dentro de bucles interactivos (while)
 * para garantizar que el usuario ingrese datos validos sin interrumpir la ejecucion.
 * Integra validaciones avanzadas con Regex y Rangos (Ranges).
 */
object ValidadorEntrada {

    /**
     * Solicita un numero entero dentro de un rango especifico [min, max].
     * Repite la solicitud hasta que se ingrese una opcion valida.
     */
    fun leerEnteroEnRango(mensaje: String, min: Int, max: Int): Int {
        while (true) {
            try {
                print(mensaje)
                val linea = readLine()?.trim() ?: throw IllegalArgumentException("La entrada no puede estar vacia.")
                val valor = linea.toIntOrNull() ?: throw IllegalArgumentException("Debe ingresar un numero entero valido.")

                if (valor !in min..max) {
                    throw IndexOutOfBoundsException("Opcion fuera de rango. Debe seleccionar entre $min y $max.")
                }
                return valor
            } catch (e: Exception) {
                println("[ERROR] Error de entrada: ${e.message} Por favor, intente nuevamente.")
            }
        }
    }

    /**
     * Solicita una cantidad de productos dentro del rango permitido (1..100) segun el Paso 1.
     * Repite la solicitud hasta que se ingrese un valor valido en dicho rango.
     */
    fun leerCantidadProductos(mensaje: String = "Ingrese cantidad de productos (1-100): "): Int {
        return ValidadorUtilidades.leerCantidadProductosEnRango(mensaje)
    }

    /**
     * Solicita una cantidad entera positiva (> 0).
     * Mantiene retrocompatibilidad y valida rango de productos (1..100).
     */
    fun leerEnteroPositivo(mensaje: String): Int {
        return leerCantidadProductos(mensaje)
    }

    /**
     * Solicita un texto obligatorio que no puede estar vacio ni compuesto solo de espacios.
     *
     * - Si nombreCampo es "nombre": valida que contenga al menos una letra y no sea puramente numerico.
     * - Si nombreCampo es "direccion de entrega": valida formato de calle con Regex. Si no incluye
     *   numero, agrega automaticamente "#0" al final.
     */
    fun leerTextoNoVacio(mensaje: String, nombreCampo: String = "campo"): String {
        // Regex nombre: debe contener al menos dos letras y solo letras/espacios/apostrofes/guiones
        val regexNombre = Regex("^[\\p{L}]+([\\p{L} '\\-]*[\\p{L}]+)?\$")
        // Regex direccion: texto alfabetico inicial (calle) seguido opcionalmente de numero (ej: "Av. Las Heras 1234")
        val regexDireccion = Regex("^[\\p{L}][\\p{L}0-9 .,'°#\\-]*(\\d+)?\$")

        while (true) {
            try {
                print(mensaje)
                val linea = readLine()?.trim()
                if (linea.isNullOrBlank()) {
                    throw IllegalArgumentException("El $nombreCampo no puede estar vacio.")
                }

                return when (nombreCampo) {
                    "nombre" -> {
                        if (!regexNombre.matches(linea)) {
                            throw IllegalArgumentException(
                                "El nombre solo puede contener letras y espacios (sin numeros ni simbolos especiales)."
                            )
                        }
                        linea
                    }
                    "direccion de entrega" -> {
                        if (!regexDireccion.matches(linea)) {
                            throw IllegalArgumentException(
                                "La direccion debe comenzar con el nombre de la calle (ej: 'Av. Siempre Viva 742')."
                            )
                        }
                        // Si no termina con un numero, agrega #0 como numero de puerta
                        if (!linea.last().isDigit()) "$linea #0" else linea
                    }
                    else -> linea
                }
            } catch (e: Exception) {
                println("[ERROR] Error de entrada: ${e.message} Por favor, intente nuevamente.")
            }
        }
    }

    /**
     * Solicita un numero de telefono opcional.
     * Si el usuario presiona Enter sin escribir nada, retorna null.
     * Si ingresa un valor, lo procesa y formatea al estilo uniforme: +XX (XXX) XXX-XXXX (Paso 1).
     */
    fun leerTelefonoOpcional(mensaje: String): String? {
        while (true) {
            try {
                print(mensaje)
                val telefono = readLine()?.trim()
                if (telefono.isNullOrBlank()) {
                    return null
                }
                // Limpieza de caracteres no numericos para validar que contenga digitos validos
                val soloDigitos = telefono.replace(Regex("\\D"), "")
                if (soloDigitos.length !in 8..12) {
                    throw IllegalArgumentException("El telefono debe contener entre 8 y 12 digitos numericos.")
                }
                // Formateo uniforme: +XX (XXX) XXX-XXXX
                return FormatoUtilidades.formatearTelefono(telefono)
            } catch (e: Exception) {
                println("[ERROR] Error de entrada: ${e.message} Por favor, intente nuevamente o presione Enter para omitir.")
            }
        }
    }

    /**
     * Solicita un numero de telefono obligatorio y lo formatea al estilo uniforme (+XX (XXX) XXX-XXXX).
     */
    fun leerTelefono(mensaje: String): String {
        while (true) {
            try {
                print(mensaje)
                val telefono = readLine()?.trim()
                if (telefono.isNullOrBlank()) {
                    throw IllegalArgumentException("El telefono no puede estar vacio.")
                }
                val soloDigitos = telefono.replace(Regex("\\D"), "")
                if (soloDigitos.length !in 8..12) {
                    throw IllegalArgumentException("El telefono debe contener entre 8 y 12 digitos numericos.")
                }
                return FormatoUtilidades.formatearTelefono(telefono)
            } catch (e: Exception) {
                println("[ERROR] Error de entrada: ${e.message} Por favor, intente nuevamente.")
            }
        }
    }

    /**
     * Solicita un correo electronico opcional validando el formato estandar nombre@dominio.com con Regex.
     */
    fun leerEmailOpcional(mensaje: String): String? {
        return ValidadorUtilidades.leerEmailOpcional(mensaje)
    }

    /**
     * Solicita un correo electronico obligatorio y valida su estructura mediante Regex (Paso 1).
     */
    fun leerEmail(mensaje: String): String {
        return ValidadorUtilidades.leerEmailObligatorio(mensaje)
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
     * Solicita una confirmacion 's' o 'n' al usuario.
     */
    fun leerConfirmacion(mensaje: String): Boolean {
        while (true) {
            print(mensaje)
            val respuesta = readLine()?.trim()?.lowercase()
            when (respuesta) {
                "s", "si" -> return true
                "n", "no" -> return false
                else -> println("[AVISO] Opcion no valida. Por favor responda con 's' (si) o 'n' (no).")
            }
        }
    }
}
