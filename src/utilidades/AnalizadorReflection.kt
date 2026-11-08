package utilidades

import kotlin.reflect.KClass

/**
 * Objeto utilitario encargado de analizar e inspeccionar dinamicamente clases en tiempo de ejecucion
 * mediante mecanismos de Reflection (Paso 2: Reflection).
 *
 * Permite listar todas las propiedades, metodos y anotaciones de entidades clave
 * como Pedido, Producto o Cliente.
 */
object AnalizadorReflection {

    /**
     * Inspecciona una clase Java/Kotlin utilizando Reflection y presenta un analisis estructurado:
     * 1. Anotaciones aplicadas a la clase (como @Promocionable).
     * 2. Propiedades y campos declarados (nombre y tipo).
     * 3. Metodos declarados (nombre, tipos de parametros y tipo de retorno).
     */
    fun inspeccionarClase(clase: Class<*>) {
        println("------------------------------------------------------------------")
        println("INSPECCION DINAMICA (REFLECTION): Entidad '${clase.simpleName}'")
        println("Nombre completo: ${clase.name}")
        println("------------------------------------------------------------------")

        // 1. Deteccion de Anotaciones en la clase
        println("[Anotaciones a nivel de clase]")
        val anotacionesPersonalizadas = clase.annotations.filter { it.annotationClass.simpleName != "Metadata" }
        if (anotacionesPersonalizadas.isEmpty()) {
            println("  (Sin anotaciones de negocio directas a nivel de clase)")
        } else {
            for (anotacion in anotacionesPersonalizadas) {
                println("  - @${anotacion.annotationClass.simpleName} -> $anotacion")
            }
        }

        // 2. Propiedades y campos declarados
        println("\n[Propiedades y Campos Declarados]")
        val campos = clase.declaredFields
        if (campos.isEmpty()) {
            println("  (No se encontraron campos declarados)")
        } else {
            for (campo in campos) {
                if (!campo.isSynthetic) {
                    println("  - Propiedad: '${campo.name}' | Tipo: ${campo.type.simpleName}")
                }
            }
        }

        // 3. Metodos declarados
        println("\n[Metodos Declarados]")
        val metodos = clase.declaredMethods
        if (metodos.isEmpty()) {
            println("  (No se encontraron metodos declarados)")
        } else {
            for (metodo in metodos) {
                if (!metodo.isSynthetic) {
                    val parametros = metodo.parameterTypes.joinToString(", ") { it.simpleName }
                    val textoParams = if (parametros.isEmpty()) "sin parametros" else parametros
                    println("  - Metodo: ${metodo.name}($textoParams) -> Retorno: ${metodo.returnType.simpleName}")
                }
            }
        }
        println("------------------------------------------------------------------\n")
    }

    /**
     * Sobrecarga que permite invocar la inspeccion pasando una referencia KClass de Kotlin (ej. Pedido::class).
     */
    fun inspeccionarClase(kClass: KClass<*>) {
        inspeccionarClase(kClass.java)
    }
}
