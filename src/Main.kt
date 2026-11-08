import java.time.LocalDate
import model.Cliente
import model.ComboFamiliar
import model.Pedido
import model.Pizza
import model.PizzaPromocional
import model.Producto
import service.InventarioService
import service.PedidoService
import util.ValidadorEntrada
import utilidades.GestorRegistro
import utilidades.PromocionFechaHelper
import utilidades.Promocionable

/**
 * Programa Principal - Sistema de Gestion de Pizzeria (Kotlin Avanzado).
 */
fun main() {
    println("==========================================================================")
    println("                     SISTEMA DE GESTION DE PIZZERIA                       ")
    println("==========================================================================\n")

    // =========================================================================
    // INICIALIZACION DEL CATALOGO Y EVALUACION DE DUPLICADOS (PASO 5)
    // =========================================================================
    val catalogoProductos: List<Producto> = listOf(
        Pizza("Pizza Pepperoni", 8990.0, listOf("Masa", "Salsa de Tomate", "Queso Mozzarella", "Pepperoni")),
        Pizza("Pizza Napolitana", 9490.0, listOf("Masa", "Salsa de Tomate", "Queso Mozzarella", "Jamón")),
        Pizza("Pizza Vegetariana", 9990.0, listOf("Masa", "Salsa de Tomate", "Queso Mozzarella", "Champiñones")),
        PizzaPromocional(), // Anotado con @Promocionable (Paso 2)
        ComboFamiliar(),    // Anotado con @Promocionable (Paso 2)
        Producto("Coca Cola 1.5L", 2500.0, emptyList(), categoria = "Bebida")
    )

    println("--- Inicializando Catalogo de Productos en GestorRegistro ---")
    for (prod in catalogoProductos) {
        GestorRegistro.registrarProducto(prod)
    }

    // Demostracion practica de prevencion de duplicados (Paso 5) usando sobrecarga == (Paso 3)
    val productoDuplicadoTest = Producto("Pizza Pepperoni", 9200.0, listOf("Masa"), categoria = "Pizza")
    println("Comprobando deteccion de duplicado en catalogo (Pizza Pepperoni == Pizza Pepperoni):")
    GestorRegistro.registrarProducto(productoDuplicadoTest)
    println()


    val hoy = LocalDate.now()
    val enPeriodoPromo = PromocionFechaHelper.estaEnPeriodoPromocion(hoy)
    println("==========================================================================")
    println("PASO 1: EVALUACION DE RANGO DE FECHAS PROMOCIONALES (RANGES)")
    println("==========================================================================")
    println("Periodo promocional activo : ${PromocionFechaHelper.PERIODO_PROMOCIONAL.start} al ${PromocionFechaHelper.PERIODO_PROMOCIONAL.endInclusive}")
    println("Fecha actual de operacion  : $hoy")
    println("¿Aplica descuento de fecha?: ${if (enPeriodoPromo) "SI (10% descuento extra en todos los pedidos)" else "NO"}\n")





    // =========================================================================
    // FLUJO INTERACTIVO DE PEDIDOS
    // =========================================================================
    val listaPedidosDelDia = mutableListOf<Pedido>()
    var siguienteIdPedido = 101
    var continuar = true

    while (continuar) {
        println("==========================================================================")
        println("                           NUEVO PEDIDO                                   ")
        println("==========================================================================")

        println("\n--- 1. Datos del Cliente ---")
        val nombre = ValidadorEntrada.leerTextoNoVacio("Ingrese nombre del cliente: ", "nombre")

        // Regex: Telefono opcional formateado automaticamente a +XX (XXX) XXX-XXXX
        val telefono = ValidadorEntrada.leerTelefonoOpcional("Ingrese telefono (ej: 98765432 o Enter para omitir): ")
        if (telefono != null) {
            println(" -> Telefono formateado uniformemente con Regex: $telefono")
        }

        // Regex: Email opcional validado con patron nombre@dominio.com
        val email = ValidadorEntrada.leerEmailOpcional("Ingrese correo electronico (nombre@dominio.com o Enter para omitir): ")
        if (email != null) {
            println(" -> Correo validado exitosamente con Regex: $email")
        }

        val direccion = ValidadorEntrada.leerTextoNoVacio("Ingrese direccion de entrega: ", "direccion de entrega")

        val cliente = Cliente(nombre = nombre, telefono = telefono, email = email, direccion = direccion)

        println("\n--- Registro de Cliente en GestorRegistro (equals / hashCode) ---")
        val esClienteNuevo = GestorRegistro.registrarCliente(cliente)
        if (!esClienteNuevo) {
            println(" -> [INFO] Cliente recurrente reconocido en el sistema.")
        }


        println("\n--- 2. Seleccion de Productos del Catalogo ---")
        val productosSeleccionados = mutableListOf<Producto>()
        var agregarMasProductos = true

        while (agregarMasProductos) {
            println("\nMenu de Productos Disponibles:")
            for ((index, prod) in catalogoProductos.withIndex()) {
                val esPromo = if (prod::class.java.isAnnotationPresent(Promocionable::class.java)) " [PROMOCION ESPECIAL]" else ""
                println(" [${index + 1}] ${prod.detallarProducto()}$esPromo")
            }

            val opcion = ValidadorEntrada.leerEnteroEnRango(
                mensaje = "\nSeleccione el numero del producto deseado (1-${catalogoProductos.size}): ",
                min = 1,
                max = catalogoProductos.size
            )

            // Paso 1: Ranges 1..100 para cantidades
            val cantidad = ValidadorEntrada.leerCantidadProductos("Ingrese cantidad deseada (1-100): ")

            val productoElegido = catalogoProductos[opcion - 1]
            repeat(cantidad) {
                productosSeleccionados.add(productoElegido)
            }
            println(" -> Agregado(s) $cantidad x '${productoElegido.nombre}' al pedido.")

            agregarMasProductos = ValidadorEntrada.leerConfirmacion("\n¿Desea agregar otro producto a este mismo pedido? (s/n): ")
        }

        val notasPedido = ValidadorEntrada.leerTextoOpcional("\n¿Desea agregar notas especiales al pedido? (Enter para omitir): ")

        // Creacion de la instancia de Pedido
        var pedidoActual = Pedido(
            idPedido = siguienteIdPedido++,
            cliente = cliente,
            productos = productosSeleccionados,
            estadoInicial = "En preparacion",
            notas = notasPedido,
            fecha = hoy
        )


        println("\n--------------------------------------------------------------------------")
        println("PASO 3: SOBRECARGA DEL OPERADOR '+' (OPERATOR OVERLOADING)")
        println("--------------------------------------------------------------------------")
        println("¿Desea agregar una adicion o segundo pedido y combinarlo con el actual")
        val combinarConOtro = ValidadorEntrada.leerConfirmacion("utilizando la sobrecarga del operador '+'? (s/n): ")

        if (combinarConOtro) {
            println("\n--- Armando Pedido Adicional para Combinacion con '+' ---")
            for ((index, prod) in catalogoProductos.withIndex()) {
                println(" [${index + 1}] ${prod.detallarProducto()}")
            }
            val opcionExtra = ValidadorEntrada.leerEnteroEnRango(
                mensaje = "Seleccione producto para la adicion (1-${catalogoProductos.size}): ",
                min = 1,
                max = catalogoProductos.size
            )
            val cantExtra = ValidadorEntrada.leerCantidadProductos("Ingrese cantidad (1-100): ")
            val notaExtra = ValidadorEntrada.leerTextoOpcional("Notas de la adicion (Enter para omitir): ")

            val productosExtra = List(cantExtra) { catalogoProductos[opcionExtra - 1] }
            val pedidoB = Pedido(
                idPedido = siguienteIdPedido++,
                cliente = cliente,
                productos = productosExtra,
                estadoInicial = "En preparacion",
                notas = notaExtra,
                fecha = hoy
            )

            // Ejecucion real de la sobrecarga del operador '+'
            println("\n[EJECUTANDO OPERATOR OVERLOADING] val pedidoCombinado = pedidoActual + pedidoB")
            val pedidoCombinado = pedidoActual + pedidoB
            println("¡Pedidos combinados exitosamente en Pedido #${pedidoCombinado.idPedido}!")
            println(" - Cantidad total de productos combinados: ${pedidoCombinado.productos.size}")
            println(" - Notas combinadas: ${pedidoCombinado.notas ?: "Sin notas"}")
            pedidoActual = pedidoCombinado
        }


        val (nombreCli, emailCli, telefonoCli) = pedidoActual.cliente

        val (clienteExtraido, productosExtraidos, totalCalculado) = pedidoActual

        val productosPromo = pedidoActual.obtenerProductosPromocionables()


        println("\n==========================================================================")
        println("             RESUMEN Y CONFIRMACION FINAL DEL PEDIDO                      ")
        println("==========================================================================")
        println("DATOS DEL CLIENTE (DESESTRUCTURADOS):")
        println("  Nombre    : $nombreCli")
        println("  Contacto  : Tel: ${telefonoCli ?: "Sin telefono"} | Email: ${emailCli ?: "Sin correo"}")
        println("  Direccion : ${clienteExtraido.direccion}")
        println("--------------------------------------------------------------------------")
        println("DETALLE DE PRODUCTOS INCLUIDOS (${productosExtraidos.size} item(s)):")
        for ((idx, p) in productosExtraidos.withIndex()) {
            val esPromo = if (p in productosPromo) {
                val anotacion = p::class.java.getAnnotation(Promocionable::class.java)
                " [PROMO: ${(anotacion.descuentoAdicional * 100).toInt()}% desc extra]"
            } else {
                ""
            }
            println("  ${idx + 1}. ${p.nombre} (${p.categoria}) - $${"%.2f".format(p.precio)}$esPromo")
        }
        println("--------------------------------------------------------------------------")
        println("DESGLOSE DE TOTALES Y DESCUENTOS:")
        println("  Subtotal Bruto              : $${"%.2f".format(pedidoActual.calcularSubtotal())}")

        val descVolumen = pedidoActual.calcularDescuentoVolumen()
        if (descVolumen > 0.0) {
            println("  Descuento por Volumen (10%) : -$${"%.2f".format(descVolumen)}")
        }

        val descFecha = pedidoActual.calcularDescuentoPorFecha()
        if (descFecha > 0.0) {
            println("  Descuento Periodo Promo (10%): -$${"%.2f".format(descFecha)}")
        }

        val descAnotacion = pedidoActual.calcularDescuentoPorAnotacion()
        if (descAnotacion > 0.0) {
            println("  Descuento @Promocionable    : -$${"%.2f".format(descAnotacion)}")
        }

        println("  Descuento Total Acumulado   : -$${"%.2f".format(pedidoActual.calcularDescuento())}")
        println("  Impuestos (IVA 19%)         : $${"%.2f".format(pedidoActual.calcularImpuestos())}")
        println("  TOTAL FINAL (Desestructurado): $${"%.2f".format(totalCalculado)}")
        println("--------------------------------------------------------------------------")
        println("  Notas del Pedido            : ${pedidoActual.notas ?: "Sin notas"}")
        println("  Estado Inicial              : ${pedidoActual.estadoPedido}")
        println("==========================================================================")

        // ---------------------------------------------------------------------
        // FLUJO DE INVENTARIO Y ASIGNACION DE REPARTO
        // ---------------------------------------------------------------------
        println("\n--- Verificacion de Stock en Inventario ---")
        val stockDisponible = InventarioService.verificarDisponibilidad(pedidoActual.productos)

        if (stockDisponible) {
            InventarioService.descontarIngredientesPedido(pedidoActual.productos)
            PedidoService.notificarCliente(pedidoActual)

            println("\n--- Asignacion de Repartidor ---")
            val repartidor = PedidoService.asignarRepartidor(pedidoActual)
            if (repartidor != null) {
                repartidor.entregarPedido(pedidoActual)
            } else {
                pedidoActual.cambiarEstadoPedido("Listo")
            }
        } else {
            pedidoActual.cambiarEstadoPedido("Cancelado")
            println("El pedido #${pedidoActual.idPedido} no pudo procesarse por falta de insumos.")
        }

        listaPedidosDelDia.add(pedidoActual)

        continuar = ValidadorEntrada.leerConfirmacion("\n¿Desea ingresar otro pedido nuevo? (s/n): ")
        println()
    }

    // =========================================================================
    // REPORTE DIARIO DE PEDIDOS AL CIERRE DE LA JORNADA
    // =========================================================================
    val pedidosDelDia: Array<Pedido> = listaPedidosDelDia.toTypedArray()

    println("\n==========================================================================")
    println("                      REPORTE DIARIO CONSOLIDADO                          ")
    println("==========================================================================")
    println("Total de pedidos registrados: ${pedidosDelDia.size}\n")

    for (p in pedidosDelDia) {
        val resumenProductos = p.productos.groupBy { it.nombre }.map { "${it.value.size}x ${it.key}" }.joinToString(", ")
        println(" • Pedido #${p.idPedido} | Cliente: ${p.cliente.nombre} | Productos: [$resumenProductos] | Total: $${"%.2f".format(p.calcularTotal())} | Estado: ${p.estadoPedido}")
    }

    val completados = pedidosDelDia.filter { it.estadoPedido == "Listo" || it.estadoPedido == "En camino" }
    val pendientesOCancelados = pedidosDelDia.filter { it.estadoPedido == "En preparacion" || it.estadoPedido == "Cancelado" }
    val recaudacionTotal = completados.sumOf { it.calcularTotal() }

    println("\n--- Resumen por Estados (Agrupados con filter) ---")
    println("Pedidos Completados / Entregados: ${completados.size}")
    for (p in completados) {
        println("  -> #${p.idPedido} (${p.cliente.nombre}) - Total: $${"%.2f".format(p.calcularTotal())}")
    }

    println("\nPedidos Pendientes / Cancelados : ${pendientesOCancelados.size}")
    for (p in pendientesOCancelados) {
        println("  -> #${p.idPedido} (${p.cliente.nombre}) - Estado: ${p.estadoPedido}")
    }

    println("\nRecaudacion total del dia: $${"%.2f".format(recaudacionTotal)}")
    println("==========================================================================")
    println("¡Gracias por utilizar el Sistema de Gestion de Pizzeria!")
}
