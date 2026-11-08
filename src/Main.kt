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
import utilidades.AnalizadorReflection
import utilidades.FormatoUtilidades
import utilidades.GestorRegistro
import utilidades.PromocionFechaHelper
import utilidades.ValidadorUtilidades

/**
 * Programa principal que demuestra la integracion de conceptos avanzados en Kotlin:
 * - Paso 1: Regex (validacion de email y formateo de telefono) y Ranges (rango de fechas y rango de cantidades 1..100).
 * - Paso 2: Anotaciones (@Promocionable) y Reflection (inspeccion dinamica de clases clave).
 * - Paso 3: Operator Overloading (operador + en Pedido y operador == en Producto).
 * - Paso 4: Declaraciones de desestructuracion en Cliente y Pedido.
 * - Paso 5: Evaluacion de igualdad de objetos (equals y hashCode) y prevencion de duplicados.
 * - Paso 6 y 7: Resumen consolidado ordenado para el cliente y la pizzeria.
 */
fun main() {
    println("==========================================================================")
    println("          SISTEMA DE GESTION DE PIZZERIA - KOTLIN AVANZADO                ")
    println("==========================================================================\n")

    // =========================================================================
    // PASO 2: ANOTACIONES Y REFLECTION
    // =========================================================================
    println("==========================================================================")
    println("PASO 2: REFLECTION - INSPECCION DINAMICA DE ENTIDADES CLAVE")
    println("==========================================================================")
    println("Analizando dinamicamente metodos, propiedades y anotaciones con Reflection:\n")

    // Inspeccion por reflection de Cliente
    AnalizadorReflection.inspeccionarClase(Cliente::class)

    // Inspeccion por reflection de Producto
    AnalizadorReflection.inspeccionarClase(Producto::class)

    // Inspeccion por reflection de Pedido
    AnalizadorReflection.inspeccionarClase(Pedido::class)

    // Inspeccion de clase anotada con @Promocionable
    AnalizadorReflection.inspeccionarClase(ComboFamiliar::class)

    // =========================================================================
    // PASO 1: REGEX Y RANGES
    // =========================================================================
    println("==========================================================================")
    println("PASO 1: REGEX Y RANGES - VALIDACION Y FORMATEO DE ENTRADAS")
    println("==========================================================================")

    println("1. Validacion de correos electronicos con Regex (nombre@dominio.com):")
    val correosPrueba = listOf("juan.perez@dominio.com", "correo_invalido.com", "cliente@pizzeria.cl", "sin_arroba")
    for (correo in correosPrueba) {
        val esValido = ValidadorUtilidades.validarEmail(correo)
        val estado = if (esValido) "[VALIDO]" else "[INVALIDO]"
        println("   - Correo '$correo' -> $estado")
    }

    println("\n2. Formateo de numeros de telefono a estilo uniforme (+XX (XXX) XXX-XXXX):")
    val telefonosPrueba = listOf("9991112222", "569991112222", "+56 9 8765 4321", "98765432")
    for (tel in telefonosPrueba) {
        val telFormateado = FormatoUtilidades.formatearTelefono(tel)
        println("   - Entrada: '$tel' -> Formateado: '$telFormateado'")
    }

    println("\n3. Evaluacion de rango para cantidades de productos (Ranges 1..100):")
    val cantidadesPrueba = listOf(0, 3, 50, 100, 105)
    for (cant in cantidadesPrueba) {
        val enRango = ValidadorUtilidades.validarCantidadEnRango(cant)
        val estado = if (enRango) "[EN RANGO (1-100)]" else "[FUERA DE RANGO]"
        println("   - Cantidad $cant -> $estado")
    }

    println("\n4. Evaluacion de rango de fechas para periodo promocional:")
    val hoy = LocalDate.now()
    val fechaFuera = LocalDate.of(2026, 12, 25)
    println("   - Periodo de promociones: ${PromocionFechaHelper.PERIODO_PROMOCIONAL.start} al ${PromocionFechaHelper.PERIODO_PROMOCIONAL.endInclusive}")
    println("   - Fecha actual ($hoy) en promocion? ${if (PromocionFechaHelper.estaEnPeriodoPromocion(hoy)) "SI (Aplica 10% descuento)" else "NO"}")
    println("   - Fecha $fechaFuera en promocion? ${if (PromocionFechaHelper.estaEnPeriodoPromocion(fechaFuera)) "SI" else "NO"}\n")

    // =========================================================================
    // PASO 5: EVALUACIONES DE IGUALDAD DE OBJETOS (EQUALS Y HASHCODE)
    // =========================================================================
    println("==========================================================================")
    println("PASO 5: EVALUACION DE IGUALDAD Y PREVENCION DE DUPLICADOS")
    println("==========================================================================")
    println("Evaluando igualdad en Clientes (basada en nombre y correo):")
    val cliente1 = Cliente("Carlos Perez", FormatoUtilidades.formatearTelefono("9991112222"), "carlos@correo.com", "Av. Providencia 123")
    val cliente1Duplicado = Cliente("Carlos Perez", FormatoUtilidades.formatearTelefono("9993334444"), "carlos@correo.com", "Otra Direccion 456")
    val cliente2 = Cliente("Maria Gonzalez", FormatoUtilidades.formatearTelefono("9882223333"), "maria@correo.com", "Calle Los Leones 789")

    println("   - cliente1 == cliente1Duplicado? ${cliente1 == cliente1Duplicado} (Mismo nombre y correo)")
    println("   - cliente1 == cliente2? ${cliente1 == cliente2}")
    GestorRegistro.registrarCliente(cliente1)
    GestorRegistro.registrarCliente(cliente1Duplicado) // Se detecta como duplicado
    GestorRegistro.registrarCliente(cliente2)

    println("\nEvaluando igualdad en Productos (basada en nombre y categoria):")
    val pizza1 = Pizza("Pizza Pepperoni", 8990.0, listOf("Masa", "Salsa de Tomate", "Queso Mozzarella", "Pepperoni"))
    val pizza1Duplicada = Producto("Pizza Pepperoni", 9500.0, listOf("Masa", "Queso Mozzarella"), categoria = "Pizza")
    val bebida = Producto("Coca Cola 1.5L", 2500.0, emptyList(), categoria = "Bebida")
    val comboFamiliar = ComboFamiliar()
    val pizzaPromo = PizzaPromocional()

    println("   - pizza1 == pizza1Duplicada? ${pizza1 == pizza1Duplicada} (Mismo nombre y categoria)")
    println("   - pizza1 == bebida? ${pizza1 == bebida}")
    GestorRegistro.registrarProducto(pizza1)
    GestorRegistro.registrarProducto(pizza1Duplicada) // Se detecta como duplicado
    GestorRegistro.registrarProducto(bebida)
    GestorRegistro.registrarProducto(comboFamiliar)
    GestorRegistro.registrarProducto(pizzaPromo)
    println()

    // =========================================================================
    // PASO 3: OPERATOR OVERLOADING Y PROCESAMIENTO DE PEDIDOS
    // =========================================================================
    println("==========================================================================")
    println("PASO 3: OPERATOR OVERLOADING - SOBRECARGA DEL OPERADOR + EN PEDIDO")
    println("==========================================================================")

    val pedidoA = Pedido(
        idPedido = 101,
        cliente = cliente1,
        productos = listOf(pizza1, pizzaPromo),
        estadoInicial = "En preparacion",
        notas = "Masa crujiente",
        fecha = hoy
    )

    val pedidoB = Pedido(
        idPedido = 102,
        cliente = cliente1,
        productos = listOf(comboFamiliar, bebida),
        estadoInicial = "En preparacion",
        notas = "Bebida bien helada",
        fecha = hoy
    )

    println("Detalle del Pedido #101:")
    println("   - Productos: ${pedidoA.productos.joinToString { it.nombre }}")
    println("   - Subtotal: $${"%.2f".format(pedidoA.calcularSubtotal())} | Total: $${"%.2f".format(pedidoA.calcularTotal())}")

    println("\nDetalle del Pedido #102:")
    println("   - Productos: ${pedidoB.productos.joinToString { it.nombre }}")
    println("   - Subtotal: $${"%.2f".format(pedidoB.calcularSubtotal())} | Total: $${"%.2f".format(pedidoB.calcularTotal())}")

    // Combinacion mediante sobrecarga del operador '+'
    println("\nEjecutando combinacion de pedidos con operador '+': val pedidoCombinado = pedidoA + pedidoB")
    val pedidoCombinado = pedidoA + pedidoB

    println("Pedido Combinado generado (#${pedidoCombinado.idPedido}):")
    println("   - Cliente principal: ${pedidoCombinado.cliente.nombre}")
    println("   - Total de productos combinados: ${pedidoCombinado.productos.size}")
    pedidoCombinado.productos.forEachIndexed { i, p -> println("     [$i] ${p.detallarProducto()}") }
    println("   - Notas unificadas: ${pedidoCombinado.notas}")
    println("   - Subtotal recalculado: $${"%.2f".format(pedidoCombinado.calcularSubtotal())}")
    println("   - Descuento total recalculado: -$${"%.2f".format(pedidoCombinado.calcularDescuento())}")
    println("   - Impuestos (IVA 19%): $${"%.2f".format(pedidoCombinado.calcularImpuestos())}")
    println("   - Total Final recalculado: $${"%.2f".format(pedidoCombinado.calcularTotal())}\n")

    // =========================================================================
    // PASO 4: DECLARACIONES DE DESESTRUCTURACION
    // =========================================================================
    println("==========================================================================")
    println("PASO 4: DECLARACIONES DE DESESTRUCTURACION")
    println("==========================================================================")

    println("1. Desestructuracion de objeto Cliente (nombre, email, telefono):")
    val (nombreCli, emailCli, telefonoCli) = cliente1
    println("   -> Nombre    : $nombreCli")
    println("   -> Email     : ${emailCli ?: "Sin correo"}")
    println("   -> Telefono  : ${telefonoCli ?: "Sin telefono"}")

    println("\n2. Desestructuracion de objeto Pedido (cliente, productos, total):")
    val (clienteExtraido, productosExtraidos, totalExtraido) = pedidoCombinado
    println("   -> Cliente   : ${clienteExtraido.nombre} (${clienteExtraido.direccion})")
    println("   -> Cantidad de productos: ${productosExtraidos.size}")
    println("   -> Total Pedido: $${"%.2f".format(totalExtraido)}\n")

    // =========================================================================
    // PASO 2: PRODUCTOS CON ANOTACION @Promocionable DETECTADOS DINAMICAMENTE
    // =========================================================================
    println("==========================================================================")
    println("PASO 2: PRODUCTOS IDENTIFICADOS DINAMICAMENTE COMO @Promocionable")
    println("==========================================================================")
    val promocionables = pedidoCombinado.obtenerProductosPromocionables()
    println("Productos del pedido que califican para promociones especiales:")
    for (prod in promocionables) {
        val anotacion = prod::class.java.getAnnotation(utilidades.Promocionable::class.java)
        println("   - Producto: ${prod.nombre} [${prod.categoria}]")
        println("     Descripcion Promocion: ${anotacion.descripcion}")
        println("     Descuento adicional aplicado: ${(anotacion.descuentoAdicional * 100).toInt()}%")
    }
    println()

    // =========================================================================
    // PASO 6 & 7: RESUMEN ORDENADO Y CONFIRMACION PARA CLIENTE Y PIZZERIA
    // =========================================================================
    println("==========================================================================")
    println("         PASO 6 & 7: RESUMEN Y CONFIRMACION FINAL DE PEDIDO               ")
    println("==========================================================================")
    println("DATOS DEL CLIENTE (DESESTRUCTURADO):")
    println("  Nombre Cliente  : $nombreCli")
    println("  Correo Contacto : ${emailCli ?: "Sin correo"}")
    println("  Telefono        : ${telefonoCli ?: "Sin telefono"}")
    println("  Direccion       : ${cliente1.direccion}")
    println("--------------------------------------------------------------------------")
    println("DETALLE DE PRODUCTOS INCLUIDOS:")
    for ((index, prod) in pedidoCombinado.productos.withIndex()) {
        val esPromo = if (prod in promocionables) "[PROMOCIONABLE]" else "[ESTANDAR]"
        println("  ${index + 1}. ${prod.nombre} (${prod.categoria}) - $${"%.2f".format(prod.precio)} $esPromo")
    }
    println("--------------------------------------------------------------------------")
    println("DESGLOSE DE TOTALES Y DESCUENTOS:")
    println("  Subtotal Bruto             : $${"%.2f".format(pedidoCombinado.calcularSubtotal())}")
    if (pedidoCombinado.calcularDescuentoVolumen() > 0.0) {
        println("  Descuento por Volumen (10%): -$${"%.2f".format(pedidoCombinado.calcularDescuentoVolumen())}")
    }
    if (pedidoCombinado.calcularDescuentoPorFecha() > 0.0) {
        println("  Descuento Periodo Promo(10%): -$${"%.2f".format(pedidoCombinado.calcularDescuentoPorFecha())}")
    }
    if (pedidoCombinado.calcularDescuentoPorAnotacion() > 0.0) {
        println("  Descuento @Promocionable   : -$${"%.2f".format(pedidoCombinado.calcularDescuentoPorAnotacion())}")
    }
    println("  Descuento Total Acumulado  : -$${"%.2f".format(pedidoCombinado.calcularDescuento())}")
    println("  Impuestos (IVA 19%)        : $${"%.2f".format(pedidoCombinado.calcularImpuestos())}")
    println("  TOTAL FINAL A PAGAR        : $${"%.2f".format(pedidoCombinado.calcularTotal())}")
    println("--------------------------------------------------------------------------")
    println("NOTAS DEL PEDIDO             : ${pedidoCombinado.notas ?: "Sin notas"}")
    println("ESTADO DEL PEDIDO            : ${pedidoCombinado.estadoPedido}")
    println("==========================================================================\n")

    // Flujo con servicios de inventario y reparto
    println("--- Procesando Inventario y Coordinacion de Entrega ---")
    val stockDisponible = InventarioService.verificarDisponibilidad(pedidoCombinado.productos)
    if (stockDisponible) {
        InventarioService.descontarIngredientesPedido(pedidoCombinado.productos)
        PedidoService.notificarCliente(pedidoCombinado)
        val repartidor = PedidoService.asignarRepartidor(pedidoCombinado)
        if (repartidor != null) {
            repartidor.entregarPedido(pedidoCombinado)
        } else {
            pedidoCombinado.cambiarEstadoPedido("Listo")
        }
    }

    println("\n==========================================================================")
    println("Demostracion finalizada exitosamente.")
    println("El sistema ha comprobado satisfactoriamente:")
    println(" 1. Regex (email valido nombre@dominio.com y telefono +XX (XXX) XXX-XXXX)")
    println(" 2. Ranges (fecha promocional y cantidad 1-100)")
    println(" 3. Anotacion @Promocionable y Reflection dinamico")
    println(" 4. Operator Overloading (+ en Pedido y == en Producto)")
    println(" 5. Desestructuracion de Cliente y Pedido")
    println(" 6. Evaluacion equals() / hashCode() y no duplicidad")
    println("==========================================================================")
}
