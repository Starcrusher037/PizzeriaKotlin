package model

import utilidades.Promocionable

/**
 * Representa un Combo especial anotado con @Promocionable (Paso 2: Anotaciones).
 * Califica para descuentos promocionales aplicados dinamicamente durante el procesamiento del pedido.
 */
@Promocionable(
    descripcion = "Combo Familiar Festivo con 2 pizzas grandes y bebida incluida",
    descuentoAdicional = 0.15
)
class ComboFamiliar(
    nombre: String = "Combo Familiar Super Pizza",
    precio: Double = 18990.0,
    ingredientes: List<String> = listOf("Masa", "Salsa de Tomate", "Queso Mozzarella", "Jamón")
) : Producto(nombre = nombre, precio = precio, ingredientes = ingredientes, categoria = "Combo")
