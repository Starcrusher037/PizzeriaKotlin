package model

/**
 * Representa a un cliente en el sistema de la pizzeria.
 *
 * Implementa:
 * - Declaraciones de desestructuracion (Paso 4): Funciones component1, component2 y component3
 *   para extraer directamente: val (nombre, email, telefono) = cliente
 * - Evaluacion de igualdad (Paso 5): Sobreescritura de equals() y hashCode()
 *   basada en los atributos clave: 'nombre' y 'email' (correo electronico).
 */
class Cliente(
    nombre: String,
    telefono: String?,
    email: String?,
    val direccion: String
) : Usuario(nombre, telefono, email) {

    /**
     * Componente 1 de desestructuracion: retorna el nombre del cliente.
     */
    operator fun component1(): String = nombre

    /**
     * Componente 2 de desestructuracion: retorna el correo electronico del cliente.
     */
    operator fun component2(): String? = email

    /**
     * Componente 3 de desestructuracion: retorna el numero de telefono del cliente.
     */
    operator fun component3(): String? = telefono

    /**
     * Evaluacion de igualdad de objetos (Paso 5).
     * Dos clientes se consideran iguales si coinciden su nombre y su correo electronico,
     * ignorando diferencias entre mayusculas y minusculas.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Cliente) return false
        return this.nombre.equals(other.nombre, ignoreCase = true) &&
               this.email.equals(other.email, ignoreCase = true)
    }

    /**
     * Sobreescritura de hashCode():
     * Preserva la coherencia con equals(), generando el hash a partir de nombre y correo.
     */
    override fun hashCode(): Int {
        var result = nombre.lowercase().hashCode()
        result = 31 * result + (email?.lowercase()?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Cliente(nombre='$nombre', email='${email ?: "Sin correo"}', telefono='${telefono ?: "Sin telefono"}', direccion='$direccion')"
    }
}
