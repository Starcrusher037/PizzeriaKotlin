package utilidades

import java.time.LocalDate

/**
 * Objeto utilitario encargado de gestionar periodos promocionales mediante Rangos de Fechas (Paso 1: Ranges).
 *
 * Utiliza el operador rangeTo (..) para definir un ClosedRange<LocalDate> y evalua
 * la vigencia de las fechas con el operador 'in'.
 */
object PromocionFechaHelper {

    /**
     * Rango de fechas del periodo promocional activo.
     * Ejemplo: Promocion de Noviembre 2026.
     */
    val PERIODO_PROMOCIONAL: ClosedRange<LocalDate> =
        LocalDate.of(2026, 11, 1)..LocalDate.of(2026, 11, 30)

    /**
     * Porcentaje de descuento aplicado a pedidos dentro del periodo promocional (10%).
     */
    const val PORCENTAJE_DESCUENTO_FECHA: Double = 0.10

    /**
     * Verifica si una fecha dada se encuentra dentro del rango de promociones activas.
     */
    fun estaEnPeriodoPromocional(fecha: LocalDate): Boolean {
        return fecha in PERIODO_PROMOCIONAL
    }

    /**
     * Sobrecarga sinonima para verificar vigencia promocional.
     */
    fun estaEnPeriodoPromocion(fecha: LocalDate): Boolean {
        return estaEnPeriodoPromocional(fecha)
    }
}
