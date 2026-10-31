package service

import model.Pedido
import model.Pizza

object PedidoService {

    private const val descuentoXUmbral = 0.10
    private const val umbralParaDescuento = 3

    fun calcularTotal(pizza: Pizza, cantidadPizzas: Int): Double {
        val totalPizzas = pizza.precioUnitario * cantidadPizzas
        // Se aplica descuento solo si la cantidad supera estrictamente 3 pizzas
        return if (cantidadPizzas > umbralParaDescuento) {
            val descuentoAplicado = totalPizzas * descuentoXUmbral
            println("Se aplicó un descuento de $${"%.2f".format(descuentoAplicado)}")
            totalPizzas - descuentoAplicado
        } else {
            totalPizzas
        }
    }

    /**
     * Transiciona el estado del pedido de "Preparación" a "Terminado".
     */
    fun completarPedido(pedido: Pedido) {
        if (pedido.estadoPedido == "Preparación") {
            pedido.estadoPedido = "Terminado"
            println("El pedido #${pedido.idPedido} ha sido completado. Estado: ${pedido.estadoPedido}")
        }
    }
}
