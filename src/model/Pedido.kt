package model

data class Pedido(
    val idPedido: Int,
    val cantidadPizzas: Int,
    val totalPedido: Double,
    var estadoPedido: String
)
