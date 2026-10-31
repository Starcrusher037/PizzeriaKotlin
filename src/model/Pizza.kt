package model

class Pizza(
    nombrePizza: String,
    precioUnitario: Double,
    receta: List<String>
) : Producto(nombrePizza, precioUnitario, receta)
