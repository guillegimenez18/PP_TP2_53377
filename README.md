# TP2 - Programación Orientada a Objetos en Java
Paradigmas de Programación - UTN FRM - Unidad 2

## Integrante
- Guillermina Giménez - 53377

## Estado de la entrega
Completo hasta el Ejercicio 3, y se sumó en el repositorio el mapa de memoria del TP N 1.

## Cómo ejecutar
1. Clonar el repositorio.
2. Abrir la carpeta con IntelliJ IDEA (JDK 21).
3. Ejecutar la clase `App`, que contiene el método `main`.

## Estructura de paquetes
- `excepciones`: contiene `CupoExcedidoException`, la excepción chequeada
  que se lanza cuando una actividad ya alcanzó su cupo máximo.
- `modelo`: contiene `EventoUniversitario`, `Sala`, `Estudiante` e
  `Inscripcion`, las clases centrales del dominio.
- `modelo.actividades`: contiene `Actividad` (clase abstracta) y sus tres
  subclases concretas: `Charla`, `Taller` y `Curso`.
- `modelo.certificacion`: contiene la interfaz `Certificable`, que
  implementan `Taller` y `Curso`, pero no `Charla`.

## Ejercicio 1: excepciones y persistencia
El método `inscribir(Estudiante)` de `Actividad` controla el cupo antes de
crear la inscripción. Si la cantidad de inscriptos ya alcanzó el
`cupoMaximo`, lanza `CupoExcedidoException` (excepción chequeada, declarada
con `throws`) en lugar de crear la inscripción.

`EventoUniversitario` implementa `Serializable`, al igual que todas las
clases que forman parte de su grafo de objetos (`Sala`, `Actividad` y sus
subclases, `Inscripcion`, `Estudiante`), porque la serialización de Java
guarda el objeto y todo lo que referencia. El método `persistirEvento()`
guarda el evento en un archivo `evento<id>.dat` mediante
`ObjectOutputStream`, y `recuperarEvento(id)` lo reconstruye con
`ObjectInputStream`.

En `App`, el método `flujoInscribirPersistirLeer` encierra los tres pasos
(inscribir, persistir, leer) en un único `try-catch-finally`, con un
`catch` específico para cada tipo de excepción (`CupoExcedidoException`,
`FileNotFoundException`, `NotSerializableException`, `IOException`,
`ClassNotFoundException`), ordenados de la excepción más específica a la
más general. Se demuestran cuatro casos: dos inscripciones exitosas, una
que excede el cupo (fallo controlado por `CupoExcedidoException`) y una
lectura de un archivo inexistente (fallo controlado por
`FileNotFoundException`).

## Ejercicio 2: certificados
`Certificable` es una interfaz con la constante `ENTIDAD_EMISORA` y el
método `generarCertificado(Estudiante)`. La implementan `Taller` y
`Curso`, porque son las actividades certificables; `Charla` no la
implementa, así que queda excluida sin necesidad de ninguna condición
adicional en el código que emite los certificados.

En `App`, el método `emitirCertificados` recorre las actividades de un
evento y usa `actividad instanceof Certificable certificable` para
identificar cuáles pueden emitir certificado. Para cada una, recorre sus
inscripciones (expuestas por `Actividad.getInscripciones()`, que devuelve
una vista de solo lectura con `Collections.unmodifiableList`) y genera un
certificado por cada estudiante inscripto.

## Ejercicio 3: métodos genéricos y wildcards
`EventoUniversitario` incorpora dos métodos:

```java
//public <T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T> tipo);
//public double calcularCostoMateriales(List<? extends Actividad> actividades);
```

`filtrarActividadesPorTipo` usa un método genérico con parámetro de tipo
acotado (`<T extends Actividad>`) porque necesita devolver un tipo
concreto: según el `Class<T>` que se le pase (`Charla.class`,
`Taller.class` o `Curso.class`), devuelve una `List<Charla>`,
`List<Taller>` o `List<Curso>` ya tipada, sin necesidad de *cast* del lado
de quien lo llama. Usa `Class<T>.isInstance()` y `Class<T>.cast()` para
filtrar y convertir cada elemento.

`calcularCostoMateriales` usa un wildcard (`List<? extends Actividad>`) en
lugar de un genérico, porque no necesita devolver ningún tipo concreto:
solo necesita leer el costo de materiales de cualquier lista de
actividades, sea `List<Charla>`, `List<Taller>`, `List<Curso>` o
`List<Actividad>`. Un parámetro `List<Actividad>` no serviría, porque en
Java `List<Charla>` no es subtipo de `List<Actividad>` (los genéricos no
son covariantes); el wildcard resuelve esto porque permite leer elementos
como `Actividad` sin importar la subclase concreta de la lista recibida.

En `App`, el método `mostrarFiltradoYCostos` demuestra ambos métodos:
filtra las actividades de cada evento por tipo concreto, muestra la
cantidad de cada tipo, calcula el costo de materiales por tipo, e
imprime la clase real (`getClass().getSimpleName()`) del primer elemento
de cada lista filtrada, para evidenciar que el filtrado devuelve listas
correctamente tipadas.

## Decisiones propias (no especificadas en el enunciado)
- **Fórmula de costo de materiales de `Curso`**: 200 + ´nivel´ * 20.
- **`crearActividad` con tres versiones sobrecargadas** (por `String`,
  `boolean` e `int` como último parámetro) en lugar de una única versión
  con `String tipo`, para poder pasar el parámetro propio de cada
  subclase (`disertante`, `requiereNotebook`, `nivel`) sin necesitar
  conversiones de tipo dentro del método.
- **Constructor de copia de `EventoUniversitario`**: copia `id`, `titulo`,
  `costoBase` y `gratuito`, pero no `sala` ni `actividades`, porqué preferí enfocarme en las consignas del trabajo 2. 
- **Caso 4 del Ejercicio 1**: provoca el fallo de persistencia a
  propósito, guardando el evento con un id y luego intentando leer un id
  distinto que no tiene archivo asociado, para demostrar el `catch` de
  `FileNotFoundException`.

## Capturas
Ver carpeta `capturas/`.