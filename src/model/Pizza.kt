package model

/**
 * Representa una pizza dentro del sistema de la pizzeria.
 * Especializacion de Producto cuya categoria predeterminada es 'Pizza'.
 */
open class Pizza(
    nombrePizza: String,
    precioUnitario: Double,
    receta: List<String>
) : Producto(nombre = nombrePizza, precio = precioUnitario, ingredientes = receta, categoria = "Pizza")
