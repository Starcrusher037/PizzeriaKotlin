package service

import model.Pedido
import model.Repartidor

object PedidoService {

    // Encapsulado: la colección de repartidores no puede ser modificada desde fuera
    private val repartidoresDisponibles: List<Repartidor> = listOf(
        Repartidor(1, "Carlos Gómez", "(999) 111-2222", "carlos@pizzeria.com", distanciaKm = 3.5),
        Repartidor(2, "Andrea Soto", "(999) 333-4444", "andrea@pizzeria.com", distanciaKm = 1.2),
        Repartidor(3, "Felipe Rivas", "(999) 555-6666", "felipe@pizzeria.com", distanciaKm = 4.8)
    )

    // Constante privada encapsulada
    private const val DISTANCIA_UMBRAL_CERCANA = 2.5

    fun obtenerRepartidores(): List<Repartidor> = repartidoresDisponibles

    fun asignarRepartidor(pedido: Pedido): Repartidor? {
        val repartidor = repartidoresDisponibles.find { it.disponible && it.distanciaKm <= DISTANCIA_UMBRAL_CERCANA }
            ?: repartidoresDisponibles.find { it.disponible }

        if (repartidor != null) {
            println("Repartidor asignado al pedido #${pedido.idPedido}: ${repartidor.nombre} (distancia: ${repartidor.distanciaKm} km).")
        } else {
            println("Aviso: No hay repartidores disponibles en este momento para el pedido #${pedido.idPedido}.")
        }
        return repartidor
    }

    fun notificarCliente(pedido: Pedido) {
        pedido.cliente.email.takeIf { it != "correo@invalido.com" }?.let { correoValido ->
            println("Notificación enviada a $correoValido: El pedido #${pedido.idPedido} se encuentra en estado '${pedido.estadoPedido}'.")
        }
    }
}
