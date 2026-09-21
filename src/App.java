import excepciones.CupoExcedidoException;
import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.Sala;
import modelo.actividades.Actividad;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.NotSerializableException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // ===== EJERCICIO 1 =====
        System.out.println("----------- EJERCICIO 1 -----------");
        EventoUniversitario evento1 = new EventoUniversitario("E1", "Jornada de Programación", 15000.0, false);
        EventoUniversitario copia1 = new EventoUniversitario(evento1);

        evento1.mostrarDatos();
        System.out.println("\n----Copia del evento 1----:");
        copia1.mostrarDatos();
        System.out.println("Total de eventos creados hasta ahora: " + EventoUniversitario.getCantidadEventos());

        // ===== EJERCICIO 2 =====
        System.out.println("\n=========== EJERCICIO 2 y 3 ===========");

        // lista de estudiantes
        List<Estudiante> estudiantes = new ArrayList<>();
        estudiantes.add(new Estudiante("53377", "Guillermina Giménez"));
        estudiantes.add(new Estudiante("53388", "Florencia Sosa"));
        estudiantes.add(new Estudiante("53399", "Guadalupe Vargas"));

        // se construye otro evento
        EventoUniversitario evento2 = new EventoUniversitario("E2", "Olimpiadas Matemáticas", 20000.0, false);

        // se asigna una sala al evento
        Sala salaMagna = new Sala(1, "Sala Magna");
        evento2.asignarSala(salaMagna);

        // se crean actividades propias del evento
        evento2.crearActividad(1, "Introducción a Java", 2, "Guillermina Giménez");   // Charla
        evento2.crearActividad(2, "Uso de Git", 5, true);

        // se inscriben estudiantes en cada actividad
        List<Actividad> actividadesEvento2 = evento2.getActividades();
        Actividad taller = actividadesEvento2.get(1);
        Actividad charla = actividadesEvento2.get(0);

        // resumen de datos del evento (incluye sus actividades e inscripciones)
        evento2.mostrarDatos();

        // total de eventos creados
        System.out.println("Total de eventos creados: " + EventoUniversitario.getCantidadEventos());

        //Caso 1: caso exitoso
        System.out.println("\n === Caso 1: flujo exitoso ===");
        flujoInscribirPersistirLeer(evento2, charla, estudiantes.get(0), "E2");

        // Caso 2: se llena el cupo de la charla (2 de 2)
        System.out.println("\n=== CASO 2: se completa el cupo ===");
        flujoInscribirPersistirLeer(evento2, charla, estudiantes.get(1), "E2");

        // Caso 3: fallo controlado por cupo excedido
        System.out.println("\n=== CASO 3: cupo excedido ===");
        flujoInscribirPersistirLeer(evento2, charla, estudiantes.get(2), "E2");

        // Caso 4: fallo controlado de persistencia (archivo inexistente)
        System.out.println("\n=== CASO 4: archivo inexistente ===");
        flujoInscribirPersistirLeer(evento2, taller, estudiantes.get(0), "NOEXISTE");

        System.out.println("\nTotal de eventos creados: " + EventoUniversitario.getCantidadEventos());
    }

    private static void flujoInscribirPersistirLeer(EventoUniversitario evento, Actividad actividad,
                                                    Estudiante estudiante, String idARecuperar) {
        System.out.println("Intentando inscribir a " + estudiante.getNombre() + " en " + actividad.getTitulo());
        try {
            actividad.inscribir(estudiante);
            System.out.println("Inscripción realizada.");

            evento.persistirEvento();
            System.out.println("Evento guardado en archivo.");

            EventoUniversitario recuperado = evento.recuperarEvento(idARecuperar);
            System.out.println("Evento recuperado del archivo:");
            recuperado.mostrarDatos();

        } catch (CupoExcedidoException e) {
            System.out.println("[ERROR DE CUPO] " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("[ERROR DE ARCHIVO] No se pudo abrir el archivo: " + e.getMessage());
        } catch (NotSerializableException e) {
            System.out.println("[ERROR DE SERIALIZACIÓN] Clase no serializable: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("[ERROR DE ENTRADA/SALIDA] " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("[ERROR DE CLASE] No se encontró la clase del objeto guardado: " + e.getMessage());
        } finally {
            System.out.println("Fin del intento para " + estudiante.getNombre() + ".");
        }
    }
}