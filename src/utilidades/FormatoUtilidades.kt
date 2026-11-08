package utilidades

/**
 * Objeto utilitario encargado de procesar y estandarizar formatos de datos (Paso 1).
 * Implementa el procesamiento de numeros telefonicos mediante expresiones regulares (Regex).
 */
object FormatoUtilidades {

    /**
     * Procesa un numero de telefono ingresado en cualquier formato comun
     * (con espacios, guiones o solo digitos) y lo formatea al estilo uniforme:
     * +XX (XXX) XXX-XXXX
     *
     * Logica y documentacion:
     * 1. Elimina cualquier caracter no numerico utilizando la expresion regular '\D'.
     * 2. Si el numero contiene 10 digitos (formato de codigo de area y linea), antepone el codigo de pais por defecto (56).
     * 3. Si contiene 9 digitos (numero celular nacional comun), antepone el codigo de pais y adapta la longitud a 12 digitos.
     * 4. Aplica una expresion regular estructurada en 4 grupos de captura:
     *    - Grupo 1 (\\d{2}): Codigo de pais -> +XX
     *    - Grupo 2 (\\d{3}): Codigo de area o prefijo -> (XXX)
     *    - Grupo 3 (\\d{3}): Bloque central -> XXX
     *    - Grupo 4 (\\d{4}): Bloque terminal -> XXXX
     * 5. Utiliza declaraciones de desestructuracion en la expresion regular (val (pais, area, prefijo, linea) = match.destructured).
     */
    fun formatearTelefono(telefono: String?, codigoPaisPorDefecto: String = "56"): String {
        if (telefono.isNullOrBlank()) return "Sin telefono"

        // Limpieza: conservar unicamente los digitos numericos usando Regex
        val soloDigitos = telefono.replace(Regex("\\D"), "")

        // Adaptacion de longitud para alcanzar los 12 digitos requeridos (+XX (XXX) XXX-XXXX)
        val digitosNormalizados = when {
            soloDigitos.length == 12 -> soloDigitos
            soloDigitos.length == 10 -> codigoPaisPorDefecto + soloDigitos
            soloDigitos.length == 9 -> codigoPaisPorDefecto + "9" + soloDigitos.takeLast(8)
            soloDigitos.length == 11 && soloDigitos.startsWith("56") -> {
                // Si viene como 569XXXXXXXX (11 digitos), insertamos el digito faltante en el bloque central
                val pais = soloDigitos.substring(0, 2)
                val resto = soloDigitos.substring(2)
                pais + resto.padStart(10, '0')
            }
            else -> soloDigitos.padStart(12, '0')
        }

        // Expresion regular con grupos de captura para formatear a +XX (XXX) XXX-XXXX
        val regexFormato = Regex("^(\\d{2})(\\d{3})(\\d{3})(\\d{4})$")
        val match = regexFormato.matchEntire(digitosNormalizados)

        return if (match != null) {
            val (pais, area, prefijo, linea) = match.destructured
            "+$pais ($area) $prefijo-$linea"
        } else {
            // Retorno de contingencia seguro si los digitos no cumplen la mascara
            "+$codigoPaisPorDefecto $soloDigitos"
        }
    }
}
