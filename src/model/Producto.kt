package model

/**
 * Clase base que representa un producto disponible en la pizzeria (pizzas, bebidas, ingredientes o combos).
 *
 * Implementa:
 * - Atributo 'categoria' para clasificar el producto ("Pizza", "Bebida", "Ingrediente", "Combo").
 * - Sobrecarga del operador '==' a traves de la sobreescritura de equals() y hashCode() (Paso 3 y Paso 5).
 * - Comparacion de igualdad basada exclusivamente en los atributos clave: 'nombre' y 'categoria'.
 */
open class Producto(
    val nombre: String,
    val precio: Double,
    val ingredientes: List<String> = emptyList(),
    val categoria: String = "Pizza"
) {
    /**
     * Retorna una descripcion formateada del producto incluyendo categoria, precio e ingredientes.
     */
    open fun detallarProducto(): String {
        val detalleIngredientes = if (ingredientes.isNotEmpty()) " | Ingredientes: ${ingredientes.joinToString(", ")}" else ""
        return "[$categoria] $nombre - Precio: $${"%.2f".format(precio)}$detalleIngredientes"
    }

    /**
     * Sobrecarga del operador == (Paso 3) y Evaluacion de Igualdad (Paso 5).
     * En Kotlin, el operador '==' invoca internamente el metodo equals().
     * Dos instancias de Producto se consideran identicas si coinciden en su nombre
     * y en su categoria, omitiendo distinciones de mayusculas y minusculas.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Producto) return false
        return this.nombre.equals(other.nombre, ignoreCase = true) &&
               this.categoria.equals(other.categoria, ignoreCase = true)
    }

    /**
     * Sobreescritura de hashCode() para preservar el contrato general con equals():
     * Objetos considerados iguales segun equals() deben producir identico codigo hash.
     */
    override fun hashCode(): Int {
        var result = nombre.lowercase().hashCode()
        result = 31 * result + categoria.lowercase().hashCode()
        return result
    }

    override fun toString(): String {
        return "$nombre [$categoria] - $${"%.2f".format(precio)}"
    }
}
