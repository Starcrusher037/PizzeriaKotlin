package service

import model.Pizza
import model.Pedido


object PedidoService {

    private const val descuentoXUmbral = 0.10
    private const val umbralParaDescuento = 3

    fun calcularTotal(pizza: Pizza, cantidadPizzas: Int): Double {
        val totalPizzas = pizza.precioUnitario * cantidadPizzas
        return if (cantidadPizzas >= umbralParaDescuento) {
            val descuentoAplicado = totalPizzas * descuentoXUmbral
            println("Se aplicó un descuento de $${descuentoAplicado}")
            totalPizzas - descuentoAplicado
        } else {
            totalPizzas
        }
    }
}
