package model

open class Producto(
    val nombre: String,
    val precio: Double,
    val ingredientes: List<String>
) {
    open fun detallarProducto(): String {
        return "$nombre - Precio: $${"%.2f".format(precio)} | Ingredientes: ${ingredientes.joinToString(", ")}"
    }
}
