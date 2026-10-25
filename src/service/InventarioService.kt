package service

import model.Pizza


object InventarioService {

    fun gestionarStock(pizza: Pizza, cantidadPizza: Int): Boolean {
        val pedidoConfirmado = if(pizza.ingredientesDisponibles >= cantidadPizza){
            pizza.ingredientesDisponibles -= cantidadPizza
            println("Stock suficiente de ingredientes para la preparacion de la pizza.")
            println("Ingredientes restantes: ${pizza.ingredientesDisponibles}")
            true
        } else{
            println("No hay stock suficiente para el pedido")
            println("Sólo hay disponibles ${pizza.ingredientesDisponibles} ingredientes.")
            false
        }
        return pedidoConfirmado
    }
}