package service

import model.Pizza

object InventarioService {

    fun verificarYReducirStock(pizza: Pizza, cantidadPizza: Int): Boolean {
        // 1. Verificación previa de stock disponible
        return if (pizza.ingredientesDisponibles >= cantidadPizza) {
            // 2. Mensaje de confirmación antes de reducir
            println("Stock suficiente de ingredientes para la preparación de la pizza.")
            // 3. Reducción del inventario tras la confirmación
            pizza.ingredientesDisponibles -= cantidadPizza
            println("Ingredientes restantes: ${pizza.ingredientesDisponibles}")
            true
        } else {
            // Mensaje si no hay suficiente stock
            println("No hay stock suficiente para el pedido.")
            println("Sólo hay disponibles ${pizza.ingredientesDisponibles} ingredientes.")
            false
        }
    }
}
