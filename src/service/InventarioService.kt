package service

import model.Producto

object InventarioService {

    // Encapsulado: el mapa mutable no puede ser alterado directamente desde el exterior
    private val stockIngredientes: MutableMap<String, Int> = mutableMapOf(
        "Masa" to 15,
        "Salsa de Tomate" to 15,
        "Queso Mozzarella" to 15,
        "Pepperoni" to 6,
        "Jamón" to 6,
        "Champiñones" to 6
    )

    // Consulta de solo lectura del stock (retorna copia inmutable)
    fun consultarStock(): Map<String, Int> = stockIngredientes.toMap()

    fun verificarDisponibilidad(productos: List<Producto>): Boolean {
        val requeridos = calcularIngredientesRequeridos(productos)
        val faltantes = mutableListOf<String>()

        for ((ingrediente, cantidadRequerida) in requeridos) {
            val disponible = stockIngredientes[ingrediente] ?: 0
            if (disponible < cantidadRequerida) {
                faltantes.add("$ingrediente (Requerido: $cantidadRequerida, Disponible: $disponible)")
            }
        }

        return when {
            faltantes.isEmpty() -> {
                println("Stock suficiente: Todos los ingredientes necesarios están disponibles en inventario.")
                true
            }
            else -> {
                println("No hay stock suficiente para aceptar el pedido.")
                println("Ingredientes insuficientes:")
                faltantes.forEach { println(" - $it") }
                println("Pasos para reabastecer:")
                println(" 1. Notificar al encargado de compras de insumos.")
                println(" 2. Generar orden de compra al proveedor de ingredientes faltantes.")
                println(" 3. Registrar el ingreso de stock en el sistema antes de reintentar el pedido.")
                false
            }
        }
    }

    // Método auxiliar privado: lógica interna encapsulada
    private fun calcularIngredientesRequeridos(productos: List<Producto>): Map<String, Int> {
        val requeridos = mutableMapOf<String, Int>()
        for (producto in productos) {
            for (ingrediente in producto.ingredientes) {
                requeridos[ingrediente] = (requeridos[ingrediente] ?: 0) + 1
            }
        }
        return requeridos
    }

    // Método auxiliar privado: solo accesible y ejecutable por el servicio
    private fun descontarLoteIngredientes(ingredientes: List<String>) {
        for (ingrediente in ingredientes) {
            val stockActual = stockIngredientes[ingrediente] ?: 0
            if (stockActual > 0) {
                stockIngredientes[ingrediente] = stockActual - 1
            }
        }
    }

    fun descontarIngredientesPedido(productos: List<Producto>) {
        for (producto in productos) {
            descontarLoteIngredientes(producto.ingredientes)
        }
        println("Inventario actualizado automáticamente según los ingredientes utilizados.")
    }
}
