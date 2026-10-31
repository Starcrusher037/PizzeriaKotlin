import model.Cliente
import model.Pedido
import model.Pizza
import model.Producto
import service.InventarioService
import service.PedidoService

fun main() {
    println("===========================================")
    println("      SISTEMA DE GESTIÓN - PIZZERÍA        ")
    println("===========================================\n")

    // Menú de productos disponibles
    val catalogoPizzas: List<Pizza> = listOf(
        Pizza("Pizza Pepperoni", 8990.0, listOf("Masa", "Salsa de Tomate", "Queso Mozzarella", "Pepperoni")),
        Pizza("Pizza Napolitana", 9490.0, listOf("Masa", "Salsa de Tomate", "Queso Mozzarella", "Jamón")),
        Pizza("Pizza Vegetariana", 9990.0, listOf("Masa", "Salsa de Tomate", "Queso Mozzarella", "Champiñones"))
    )

    val listaPedidosDelDia = mutableListOf<Pedido>()
    var siguienteIdPedido = 1001
    var continuar = true

    while (continuar) {
        println("-------------------------------------------")
        println("              NUEVO PEDIDO                 ")
        println("-------------------------------------------")

        // Paso 1.3: Registro y validación de strings del cliente
        println("--- Registro de Cliente ---")
        print("Ingrese nombre del cliente: ")
        val nombreInput = readLine()?.trim().takeIf { !it.isNullOrBlank() } ?: "Cliente Anónimo"

        print("Ingrese teléfono del cliente (ej. 912345678): ")
        val telefonoRaw = readLine()?.trim()
        val telefono = if (!telefonoRaw.isNullOrBlank() && telefonoRaw.all { it.isDigit() } && telefonoRaw.length in 8..11) {
            telefonoRaw
        } else {
            println("Teléfono inválido. Asignando valor por defecto '(000) 000-0000'.")
            "(000) 000-0000"
        }

        print("Ingrese correo electrónico: ")
        val emailRaw = readLine()?.trim()
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        val email = if (!emailRaw.isNullOrBlank() && emailRegex.matches(emailRaw)) {
            emailRaw
        } else {
            println("Correo electrónico inválido. Asignando 'correo@invalido.com'.")
            "correo@invalido.com"
        }

        print("Ingrese dirección de entrega: ")
        val direccion = readLine()?.trim().takeIf { !it.isNullOrBlank() } ?: "Retiro en local"

        // Paso 4.2: Manejo de nulos para notas del pedido
        print("¿Desea agregar notas especiales al pedido? (Enter para omitir): ")
        val notasInput: String? = readLine()?.trim().takeIf { !it.isNullOrBlank() }

        val cliente = Cliente(
            nombre = nombreInput,
            telefono = telefono,
            email = email,
            direccion = direccion
        )
        println("Cliente registrado: ${cliente.nombre} | Contacto: ${cliente.telefono} | ${cliente.email}\n")

        // Selección de productos con Paso 4.1: Manejo de errores try-catch
        println("--- Menú de Pizzas Disponibles ---")
        for ((index, pizza) in catalogoPizzas.withIndex()) {
            println("[${index + 1}] ${pizza.detallarProducto()}")
        }

        var opcionSeleccionada = 1
        var cantidadPizzas = 1

        try {
            print("\nSeleccione el número de la pizza deseada (1-${catalogoPizzas.size}): ")
            val inputOpcion = readLine()?.toIntOrNull() ?: throw IllegalArgumentException("Debe ingresar un número de opción válido.")
            if (inputOpcion !in 1..catalogoPizzas.size) {
                throw IndexOutOfBoundsException("Opción fuera de rango del menú.")
            }
            opcionSeleccionada = inputOpcion

            print("¿Cuántas pizzas de esta variedad desea pedir?: ")
            val inputCantidad = readLine()?.toIntOrNull() ?: throw IllegalArgumentException("La cantidad debe ser un número entero.")
            if (inputCantidad <= 0) {
                throw IllegalArgumentException("La cantidad de pizzas debe ser mayor a 0.")
            }
            cantidadPizzas = inputCantidad
        } catch (e: Exception) {
            println("Error de entrada: ${e.message}. Se aplicarán valores por defecto (Opción 1, 1 unidad).")
            opcionSeleccionada = 1
            cantidadPizzas = 1
        }

        val pizzaSeleccionada = catalogoPizzas[opcionSeleccionada - 1]
        val productosPedido = List(cantidadPizzas) { pizzaSeleccionada }

        // Paso 1.1: Evaluación de disponibilidad con 'when' en InventarioService
        println("\n--- Verificando Inventario ---")
        val stockDisponible = InventarioService.verificarDisponibilidad(productosPedido)

        val estadoInicial = if (stockDisponible) "En preparación" else "Cancelado"

        // Paso 3.1: Instancia de Pedido con métodos de cálculo y estado
        val pedido = Pedido(
            idPedido = siguienteIdPedido++,
            cliente = cliente,
            productos = productosPedido,
            estadoInicial = estadoInicial,
            notas = notasInput
        )

        if (stockDisponible) {
            // Paso 2.1: Descontar automáticamente del inventario
            InventarioService.descontarIngredientesPedido(productosPedido)

            // Paso 4.2: Notificar cliente utilizando '.let'
            PedidoService.notificarCliente(pedido)

            // Paso 2.2: Asignar repartidor más cercano con 'find' y entregar pedido
            println("\n--- Coordinación de Reparto ---")
            val repartidor = PedidoService.asignarRepartidor(pedido)
            if (repartidor != null) {
                repartidor.entregarPedido(pedido)
            } else {
                pedido.cambiarEstadoPedido("Listo")
            }
        }

        listaPedidosDelDia.add(pedido)

        // Paso 5 & 6: Resumen ordenado del pedido
        println("\n===========================================")
        println("             RESUMEN DEL PEDIDO            ")
        println("===========================================")
        println("   DATOS DEL CLIENTE")
        println("   Nombre         : ${cliente.nombre}")
        println("   Contacto       : Tel: ${cliente.telefono} | Email: ${cliente.email}")
        println("   Dirección      : ${cliente.direccion}")
        println("-------------------------------------------")
        println("   PRODUCTOS DEL PEDIDO")
        println("   Variedad       : ${pizzaSeleccionada.nombre}")
        println("   Precio Unitario: $${"%.2f".format(pizzaSeleccionada.precio)}")
        println("   Cantidad       : ${pedido.productos.size} pizza(s)")
        println("-------------------------------------------")
        println("   TOTALES DEL PEDIDO")
        println("   Subtotal       : $${"%.2f".format(pedido.calcularSubtotal())}")
        val descuento = pedido.calcularDescuento()
        if (descuento > 0.0) {
            println("   Descuento (10%): -$${"%.2f".format(descuento)}")
        } else {
            println("   Descuento      : $0.00")
        }
        println("   Impuestos (19%): $${"%.2f".format(pedido.calcularImpuestos())}")
        println("   Total Final    : $${"%.2f".format(pedido.calcularTotal())}")
        println("-------------------------------------------")
        // Paso 4.2: Operador Elvis para notas
        println("   Notas          : ${pedido.notas ?: "Sin notas"}")
        println("   Estado Pedido  : ${pedido.estadoPedido}")
        println("===========================================\n")

        print("¿Desea ingresar otro pedido? (s/n): ")
        val respuesta = readLine()?.trim()?.lowercase()
        if (respuesta != "s" && respuesta != "si" && respuesta != "sí") {
            continuar = false
        }
        println()
    }

    // Paso 1.2: Almacena todos los pedidos del día en un ARREGLO
    val pedidosDelDia: Array<Pedido> = listaPedidosDelDia.toTypedArray()

    // Paso 1.2: Reporte del día utilizando un ciclo (for) y agrupando con filter
    println("==========================================================================")
    println("                      REPORTE DIARIO DE PEDIDOS                           ")
    println("==========================================================================")

    for (p in pedidosDelDia) {
        val nombresPizzas = p.productos.joinToString { it.nombre }
        println("• Pedido #${p.idPedido} | Cliente: ${p.cliente.nombre} | Pizzas: $nombresPizzas | Total: $${"%.2f".format(p.calcularTotal())} | Estado: ${p.estadoPedido}")
    }

    val pedidosCompletados = pedidosDelDia.filter { it.estadoPedido == "Listo" || it.estadoPedido == "En camino" }
    val pedidosPendientes = pedidosDelDia.filter { it.estadoPedido == "En preparación" || it.estadoPedido == "Cancelado" }

    println("\n--- Resumen de Estados (Agrupados con filter) ---")
    println("Pedidos Completados / Entregados: ${pedidosCompletados.size}")
    for (p in pedidosCompletados) {
        println("  -> #${p.idPedido} (${p.cliente.nombre}) - Estado: ${p.estadoPedido}")
    }

    println("Pedidos Pendientes / Cancelados: ${pedidosPendientes.size}")
    for (p in pedidosPendientes) {
        println("  -> #${p.idPedido} (${p.cliente.nombre}) - Estado: ${p.estadoPedido}")
    }

    println("\n==========================================================================")
    println("Programa finalizado exitosamente. ¡Gracias por usar el sistema!")
}
