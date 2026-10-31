import model.Cliente
import model.Pedido
import model.Pizza
import model.Producto
import service.InventarioService
import service.PedidoService
import util.ValidadorEntrada

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
        val nombreInput = ValidadorEntrada.leerTextoNoVacio("Ingrese nombre del cliente: ", "nombre")
        val telefono = ValidadorEntrada.leerTelefonoOpcional("Ingrese teléfono del cliente (Enter para omitir): ")
        val email = ValidadorEntrada.leerEmailOpcional("Ingrese correo electrónico (Enter para omitir): ")
        val direccion = ValidadorEntrada.leerTextoNoVacio("Ingrese dirección de entrega: ", "dirección de entrega")

        // Paso 4.2: Manejo de nulos para notas del pedido
        val notasInput: String? = ValidadorEntrada.leerTextoOpcional("¿Desea agregar notas especiales al pedido? (Enter para omitir): ")

        val cliente = Cliente(
            nombre = nombreInput,
            telefono = telefono,
            email = email,
            direccion = direccion
        )
        println("Cliente registrado: ${cliente.nombre} | Contacto: Tel: ${cliente.telefono ?: "Sin teléfono"} | Email: ${cliente.email ?: "Sin correo"}\n")

        // Paso 4.1: Manejo de errores try-catch en bucle interactivo de selección
        println("--- Menú de Pizzas Disponibles ---")
        for ((index, pizza) in catalogoPizzas.withIndex()) {
            println("[${index + 1}] ${pizza.detallarProducto()}")
        }

        val opcionSeleccionada = ValidadorEntrada.leerEnteroEnRango(
            mensaje = "\nSeleccione el número de la pizza deseada (1-${catalogoPizzas.size}): ",
            min = 1,
            max = catalogoPizzas.size
        )

        val cantidadPizzas = ValidadorEntrada.leerEnteroPositivo(
            mensaje = "¿Cuántas pizzas de esta variedad desea pedir?: "
        )

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
        println("   Contacto       : Tel: ${cliente.telefono ?: "Sin teléfono"} | Email: ${cliente.email ?: "Sin correo"}")
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

        continuar = ValidadorEntrada.leerConfirmacion("¿Desea ingresar otro pedido? (s/n): ")
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
