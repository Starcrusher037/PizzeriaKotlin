package model

import utilidades.Promocionable

/**
 * Representa una pizza especial anotada con @Promocionable (Paso 2: Anotaciones).
 * Identificada dinamicamente en tiempo de ejecucion mediante Reflection.
 */
@Promocionable(
    descripcion = "Pizza Promocional del Chef con seleccion especial",
    descuentoAdicional = 0.10
)
class PizzaPromocional(
    nombre: String = "Pizza Especial del Chef",
    precio: Double = 10490.0,
    ingredientes: List<String> = listOf("Masa", "Salsa de Tomate", "Queso Mozzarella", "Pepperoni", "Champiñones")
) : Pizza(nombrePizza = nombre, precioUnitario = precio, receta = ingredientes)
