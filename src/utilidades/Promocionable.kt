package utilidades

/**
 * Anotacion personalizada para marcar clases de productos o combos que califican
 * para promociones especiales en el sistema de la pizzeria (Paso 2).
 *
 * Se retiene en tiempo de ejecucion (AnnotationRetention.RUNTIME) para permitir su
 * deteccion dinamica mediante Reflection durante el procesamiento de pedidos.
 *
 * @property descripcion Descripcion informativa de la promocion aplicable.
 * @property descuentoAdicional Porcentaje de descuento aplicado sobre el precio base del producto (ej. 0.15 para 15%).
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Promocionable(
    val descripcion: String = "Promocion especial de la pizzeria",
    val descuentoAdicional: Double = 0.15
)
