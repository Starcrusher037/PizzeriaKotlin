package model

class Pedido(
    val idPedido: Int,
    val cliente: Cliente,
    val productos: List<Producto>,
    estadoInicial: String = "En preparación",
    val notas: String? = null
) {
    // Encapsulamiento del estado: solo modificable desde dentro de la clase
    var estadoPedido: String = estadoInicial
        private set

    // Constantes de negocio encapsuladas (privadas)
    companion object {
        private const val PORCENTAJE_DESCUENTO = 0.10
        private const val UMBRAL_PIZZAS_DESCUENTO = 3
        private const val TASA_IVA = 0.19
        private val ESTADOS_VALIDOS = listOf("En preparación", "Listo", "En camino", "Cancelado")
    }

    fun calcularSubtotal(): Double {
        return productos.sumOf { it.precio }
    }

    fun calcularDescuento(): Double {
        val subtotal = calcularSubtotal()
        return if (productos.size > UMBRAL_PIZZAS_DESCUENTO) subtotal * PORCENTAJE_DESCUENTO else 0.0
    }

    fun calcularImpuestos(): Double {
        val neto = calcularSubtotal() - calcularDescuento()
        return neto * TASA_IVA
    }

    fun calcularTotal(): Double {
        return (calcularSubtotal() - calcularDescuento()) + calcularImpuestos()
    }

    fun cambiarEstadoPedido(nuevoEstado: String) {
        if (nuevoEstado in ESTADOS_VALIDOS) {
            this.estadoPedido = nuevoEstado
            println("Pedido #$idPedido: Estado cambiado a '$estadoPedido'")
        } else {
            println("Aviso: Estado '$nuevoEstado' no reconocido para el pedido #$idPedido")
        }
    }
}
