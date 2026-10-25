package model

data class Pedido(
    val idPedido: String,
    val cantidadPizzas: Int,
    val totalPedido: Double,
    val estadoPedido: String
)
