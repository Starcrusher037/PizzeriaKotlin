package utilidades

import model.Cliente
import model.Producto

/**
 * Gestor encargado de almacenar y administrar el registro de clientes y productos.
 * Utiliza las evaluaciones de igualdad de objetos (equals() y hashCode()) para evitar
 * el ingreso de duplicados en la aplicacion (Paso 5).
 */
object GestorRegistro {

    // Colecciones de tipo Set para garantizar unicidad automatica mediante equals y hashCode
    private val clientesRegistrados = mutableSetOf<Cliente>()
    private val productosRegistrados = mutableSetOf<Producto>()

    /**
     * Registra un nuevo cliente evaluando previamente si ya existe.
     * La comprobacion 'contains' utiliza la sobreescritura de equals() (nombre y correo).
     */
    fun registrarCliente(cliente: Cliente): Boolean {
        if (clientesRegistrados.contains(cliente)) {
            println("[AVISO DUPLICADO] El cliente '${cliente.nombre}' con correo '${cliente.email}' ya existe en el sistema. Registro omitido.")
            return false
        }
        clientesRegistrados.add(cliente)
        println("[REGISTRO EXITOSO] Cliente registrado: ${cliente.nombre} (${cliente.email ?: "Sin correo"}).")
        return true
    }

    /**
     * Registra un nuevo producto en el catalogo evaluando previamente si ya existe.
     * La comprobacion 'contains' utiliza la sobreescritura de equals() (nombre y categoria).
     */
    fun registrarProducto(producto: Producto): Boolean {
        if (productosRegistrados.contains(producto)) {
            println("[AVISO DUPLICADO] El producto '${producto.nombre}' en la categoria '${producto.categoria}' ya existe en el catalogo. Registro omitido.")
            return false
        }
        productosRegistrados.add(producto)
        println("[REGISTRO EXITOSO] Producto registrado: ${producto.nombre} [${producto.categoria}].")
        return true
    }

    fun obtenerClientes(): Set<Cliente> = clientesRegistrados.toSet()

    fun obtenerProductos(): Set<Producto> = productosRegistrados.toSet()

    fun limpiar() {
        clientesRegistrados.clear()
        productosRegistrados.clear()
    }
}
