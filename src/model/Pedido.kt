package model

import java.time.LocalDate
import utilidades.Promocionable
import utilidades.PromocionFechaHelper

/**
 * Representa un pedido registrado en el sistema de la pizzeria.
 *
 * Implementa:
 * - Verificacion de rango de fechas para promociones con descuento (Paso 1: Ranges).
 * - Identificacion dinamica de productos con anotacion @Promocionable (Paso 2: Anotaciones y Reflection).
 * - Sobrecarga del operador '+' para combinar pedidos recalculando totales y descuentos (Paso 3: Operator Overloading).
 * - Declaraciones de desestructuracion para extraer cliente, productos y total (Paso 4).
 */
class Pedido(
    val idPedido: Int,
    val cliente: Cliente,
    val productos: List<Producto>,
    estadoInicial: String = "En preparacion",
    val notas: String? = null,
    val fecha: LocalDate = LocalDate.now()
) {
    // Encapsulamiento del estado: solo modificable desde dentro de la clase
    var estadoPedido: String = estadoInicial
        private set

    companion object {
        private const val PORCENTAJE_DESCUENTO_VOLUMEN = 0.10
        private const val UMBRAL_PIZZAS_DESCUENTO = 3
        private const val TASA_IVA = 0.19
        private val ESTADOS_VALIDOS = listOf("En preparacion", "En preparación", "Listo", "En camino", "Cancelado")
    }

    /**
     * Paso 1: Ranges
     * Evalua si la fecha del pedido se encuentra dentro del rango de promociones vigente.
     */
    fun estaEnPeriodoPromocion(): Boolean {
        return PromocionFechaHelper.estaEnPeriodoPromocional(fecha)
    }

    /**
     * Paso 2: Anotaciones y Reflection
     * Identifica dinamicamente cuales productos del pedido califican para promociones
     * mediante la deteccion de la anotacion @Promocionable.
     */
    fun obtenerProductosPromocionables(): List<Producto> {
        return productos.filter { it::class.java.isAnnotationPresent(Promocionable::class.java) }
    }

    fun calcularSubtotal(): Double {
        return productos.sumOf { it.precio }
    }

    /**
     * Desglose del descuento por volumen (mas de 3 productos).
     */
    fun calcularDescuentoVolumen(): Double {
        val subtotal = calcularSubtotal()
        return if (productos.size > UMBRAL_PIZZAS_DESCUENTO) subtotal * PORCENTAJE_DESCUENTO_VOLUMEN else 0.0
    }

    /**
     * Desglose del descuento por rango de fechas promocional (Paso 1).
     */
    fun calcularDescuentoPorFecha(): Double {
        return if (estaEnPeriodoPromocion()) {
            calcularSubtotal() * PromocionFechaHelper.PORCENTAJE_DESCUENTO_FECHA
        } else {
            0.0
        }
    }

    /**
     * Desglose del descuento obtenido dinamicamente por productos anotados con @Promocionable (Paso 2).
     */
    fun calcularDescuentoPorAnotacion(): Double {
        var totalDescuentoAnotacion = 0.0
        for (producto in productos) {
            val anotacion = producto::class.java.getAnnotation(Promocionable::class.java)
            if (anotacion != null) {
                totalDescuentoAnotacion += producto.precio * anotacion.descuentoAdicional
            }
        }
        return totalDescuentoAnotacion
    }

    /**
     * Calcula la suma consolidada de todos los descuentos aplicables:
     * - Descuento por volumen.
     * - Descuento por rango de fechas (promocion temporal).
     * - Descuento dinamico por anotaciones @Promocionable.
     */
    fun calcularDescuento(): Double {
        return calcularDescuentoVolumen() + calcularDescuentoPorFecha() + calcularDescuentoPorAnotacion()
    }

    fun calcularImpuestos(): Double {
        val neto = (calcularSubtotal() - calcularDescuento()).coerceAtLeast(0.0)
        return neto * TASA_IVA
    }

    fun calcularTotal(): Double {
        val neto = (calcularSubtotal() - calcularDescuento()).coerceAtLeast(0.0)
        return neto + calcularImpuestos()
    }

    /**
     * Paso 3: Operator Overloading
     * Sobrecarga del operador '+' para combinar dos pedidos en uno solo.
     * Suma los productos de ambos pedidos, combina sus notas aclaratorias y
     * genera un nuevo pedido cuyo total y descuentos se recalculan de inmediato.
     */
    operator fun plus(otro: Pedido): Pedido {
        val productosCombinados = this.productos + otro.productos
        val nuevoId = this.idPedido * 1000 + (otro.idPedido % 1000)

        // Combina notas de ambos pedidos de forma descriptiva
        val notasCombinadas = buildString {
            if (!this@Pedido.notas.isNullOrBlank()) append(this@Pedido.notas)
            if (!otro.notas.isNullOrBlank()) {
                if (isNotEmpty()) append(" | ")
                append(otro.notas)
            }
            if (this@Pedido.cliente != otro.cliente) {
                if (isNotEmpty()) append(" | ")
                append("Pedido combinado para clientes: ${this@Pedido.cliente.nombre} y ${otro.cliente.nombre}")
            }
        }.ifBlank { null }

        return Pedido(
            idPedido = nuevoId,
            cliente = this.cliente,
            productos = productosCombinados,
            estadoInicial = "En preparacion",
            notas = notasCombinadas,
            fecha = this.fecha
        )
    }

    /**
     * Paso 4: Declaraciones de desestructuracion
     * Componente 1: extrae el cliente del pedido.
     */
    operator fun component1(): Cliente = cliente

    /**
     * Componente 2: extrae la lista de productos incluidos.
     */
    operator fun component2(): List<Producto> = productos

    /**
     * Componente 3: extrae el total final calculado del pedido.
     */
    operator fun component3(): Double = calcularTotal()

    fun cambiarEstadoPedido(nuevoEstado: String) {
        if (nuevoEstado in ESTADOS_VALIDOS) {
            this.estadoPedido = nuevoEstado
            println("Pedido #$idPedido: Estado cambiado a '$estadoPedido'")
        } else {
            println("Aviso: Estado '$nuevoEstado' no reconocido para el pedido #$idPedido")
        }
    }
}
