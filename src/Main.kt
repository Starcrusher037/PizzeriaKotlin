import model.Cliente
import model.Pizza
import model.Pedido
import service.InventarioService
import service.PedidoService
import kotlin.system.exitProcess

fun main() {
    println("===========================================")
    println("      SISTEMA DE PEDIDOS - PIZZERÍA      ")
    println("===========================================\n")

    val pizza = Pizza(nombrePizza = "Pepperoni", precioUnitario = 8990.0, ingredientesDisponibles = 5)
    var siguienteIdPedido = 1001
    var continuar = true

    while (continuar) {
        println("-------------------------------------------")
        println("              NUEVO PEDIDO                 ")
        println("-------------------------------------------")

        if (pizza.ingredientesDisponibles <= 0) {
            println("AVISO: No quedan ingredientes disponibles en el inventario.")
            println("No es posible procesar más pedidos en este momento.\n")
            break
        }

        println("--- Registro de Cliente ---")
        print("Ingrese el nombre del cliente: ")
        val nombre: String = readLine()?.takeIf { it.isNotBlank() } ?: "Cliente Anónimo"

        print("Ingrese la dirección de entrega: ")
        val direccion: String = readLine()?.takeIf { it.isNotBlank() } ?: "Dirección no especificada"

        print("Ingrese el número de teléfono: ")
        val telefonoInput: String? = readLine()
        val telefono: String = if (!telefonoInput.isNullOrBlank() && telefonoInput.all { it.isDigit() }) {
            telefonoInput
        } else {
            println("Teléfono inválido o vacío. Se asignará '000000000' por defecto.")
            "000000000"
        }

        val cliente = Cliente(nombre = nombre, direccion = direccion, telefono = telefono)
        println("Cliente registrado: ${cliente.nombre}\n")

        println("--- Selección de Pizza ---")
        println("Pizza: ${pizza.nombrePizza} | Precio: $${"%.2f".format(pizza.precioUnitario)} | Ingredientes disponibles: ${pizza.ingredientesDisponibles}")
        print("¿Cuántas pizzas desea pedir? ")
        val cantidadInput: String? = readLine()
        val cantidadPizzas: Int = cantidadInput?.toIntOrNull()?.takeIf { it > 0 } ?: run {
            println("Cantidad inválida. Se asignará 1 pizza por defecto.")
            1
        }

        println("\n--- Calculando Total del Pedido ---")
        val totalPedido: Double = PedidoService.calcularTotal(pizza, cantidadPizzas)
        println("Total del pedido: ${"%.2f".format(totalPedido)}")

        println("\n--- Verificando Disponibilidad de Ingredientes ---")
        val stockDisponible: Boolean = InventarioService.verificarYReducirStock(pizza, cantidadPizzas)

        val estadoInicial: String = if (stockDisponible) "Preparación" else "Cancelado"

        val pedido = Pedido(
            idPedido = siguienteIdPedido++,
            cantidadPizzas = cantidadPizzas,
            totalPedido = totalPedido,
            estadoPedido = estadoInicial
        )

        if (stockDisponible) {
            println("\n--- Cocina y Preparación ---")
            println("Estado inicial del pedido: ${pedido.estadoPedido}")
            PedidoService.completarPedido(pedido)
        }

        println("\n===========================================")
        println("             RESUMEN DEL PEDIDO          ")
        println("===========================================")
        println("   DATOS DEL CLIENTE")
        println("   Nombre         : ${cliente.nombre}")
        println("   Dirección      : ${cliente.direccion}")
        println("   Teléfono       : ${cliente.telefono}")
        println("-------------------------------------------")
        println("   INFORMACIÓN DE LA PIZZA")
        println("   Variedad       : ${pizza.nombrePizza}")
        println("   Precio Unitario: $${"%.2f".format(pizza.precioUnitario)}")
        println("-------------------------------------------")
        println("   DETALLES DEL PEDIDO")
        println("   ID Pedido      : #${pedido.idPedido}")
        println("   Cantidad       : ${pedido.cantidadPizzas} pizza(s)")
        println("   Total a Pagar  : $${"%.2f".format(pedido.totalPedido)}")
        println("   Estado         : ${pedido.estadoPedido}")
        println("===========================================\n")

        print("¿Desea realizar otro pedido? (s/n): ")
        val respuesta = readLine()?.trim()?.lowercase()
        if (respuesta != "s" && respuesta != "si" && respuesta != "sí") {
            continuar = false
            println("\n¡Gracias por utilizar el Sistema de Pedidos! Cerrando sistema...")
        }
        println()
    }

    println("Programa finalizado exitosamente.")
    exitProcess(0)
}
