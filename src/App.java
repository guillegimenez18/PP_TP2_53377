import excepciones.CupoExcedidoException;
import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.Sala;
import modelo.actividades.Actividad;
import modelo.Inscripcion;
import modelo.actividades.Taller;
import modelo.certificacion.Certificable;
import modelo.actividades.Charla;
import modelo.actividades.Curso;

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
        System.out.println("\n=========== EJERCICIO 1: excepciones y persistencia ===========");

        // lista de estudiantes
        List<Estudiante> estudiantes = new ArrayList<>();
        estudiantes.add(new Estudiante("53377", "Guillermina Giménez"));
        estudiantes.add(new Estudiante("53388", "Florencia Sosa"));
        estudiantes.add(new Estudiante("53399", "Guadalupe Vargas"));

        // se construye otro evento
        EventoUniversitario evento2 = new EventoUniversitario("E2", "Olimpiadas Matemáticas", 20000.0, false);

        // se asigna una sala al evento
        Sala salaSum = new Sala(1, "SUM");
        evento2.asignarSala(salaSum);

        // se crean actividades propias del evento
        evento2.crearActividad(1, "Introducción a Java", 2, "Guillermina Giménez");   // Charla
        evento2.crearActividad(2, "Uso de Git", 5, true);

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

        ejercicio2();
        ejercicio3();
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

    private static void ejercicio2() {
        System.out.println("\n=========== EJERCICIO 2: CERTIFICADOS ===========");

        //se crean estudiantes
        List<Estudiante> estudiantes = new ArrayList<>();
        estudiantes.add(new Estudiante("53377", "Guillermina Giménez"));
        estudiantes.add(new Estudiante("53388", "Florencia Sosa"));
        estudiantes.add(new Estudiante("53399", "Guadalupe Vargas"));

        //se crean eventos
        EventoUniversitario evento3 = new EventoUniversitario("E3", "Semana de la Ingeniería", 10000.0, false);
        EventoUniversitario evento4 = new EventoUniversitario("E4", "Jornada de Innovación", 12000.0, false);

        //se asigna una sala a cada evento
        evento3.asignarSala(new Sala(2, "Aula 101"));
        evento4.asignarSala(new Sala(3, "Laboratorio 2"));

        //se crean actividades (la última es un Curso)
        evento3.crearActividad(1, "Charla de Bienvenida", 30, "Dr. López");
        evento3.crearActividad(2, "Taller de Git", 20, true);
        evento3.crearActividad(3, "Curso de Java Básico", 15, 1);

        evento4.crearActividad(1, "Charla de Inteligencia Artificial", 30, "Dra. Gómez");
        evento4.crearActividad(2, "Taller de html", 20, false);
        evento4.crearActividad(3, "Curso de Estadística", 15, 2);

        //se inscriben alumnos en todas las actividades
        for (Actividad actividad : evento3.getActividades()) {
            inscribirSeguro(actividad, estudiantes.get(0));
            inscribirSeguro(actividad, estudiantes.get(1));
        }
        for (Actividad actividad : evento4.getActividades()) {
            inscribirSeguro(actividad, estudiantes.get(1));
            inscribirSeguro(actividad, estudiantes.get(2));
        }

        //se emiten los certificados (las charlas no son certificables)
        List<String> certificados = new ArrayList<>();
        certificados.addAll(emitirCertificados(evento3));
        certificados.addAll(emitirCertificados(evento4));

        //se muestran los certificados emitidos
        System.out.println("\n--- Certificados emitidos ---");
        for (String certificado : certificados) {
            System.out.println(certificado);
        }
        System.out.println("Total de certificados emitidos: " + certificados.size());

        // se muestran los datos de los eventos creados
        System.out.println("\n--- Datos de los eventos ---");
        evento3.mostrarDatos();
        System.out.println();
        evento4.mostrarDatos();
    }

    private static void inscribirSeguro(Actividad actividad, Estudiante estudiante) {
        try {
            actividad.inscribir(estudiante);
            System.out.println("Inscripto: " + estudiante.getNombre() + " en " + actividad.getTitulo());
        } catch (CupoExcedidoException e) {
            System.out.println("[ERROR DE CUPO] " + e.getMessage());
        }
    }

    private static List<String> emitirCertificados(EventoUniversitario evento) {
        List<String> certificados = new ArrayList<>();
        for (Actividad actividad : evento.getActividades()) {
            if (actividad instanceof Certificable certificable) {
                for (Inscripcion inscripcion : actividad.getInscripciones()) {
                    certificados.add(certificable.generarCertificado(inscripcion.getEstudiante()));
                }
            }
        }
        return certificados;
    }
    //ejercicio 3
    private static void ejercicio3() {
        System.out.println("\n=========== EJERCICIO 3: FILTRADO Y COSTOS ===========");

        EventoUniversitario evento3 = new EventoUniversitario("E3-bis", "Semana de la Ingeniería", 10000.0, false);
        EventoUniversitario evento4 = new EventoUniversitario("E4-bis", "Jornada de Innovación", 12000.0, false);

        evento3.crearActividad(1, "Charla de Bienvenida", 30, "Dr. López");
        evento3.crearActividad(2, "Taller de Git", 20, true);
        evento3.crearActividad(3, "Curso de Java Básico", 15, 1);

        evento4.crearActividad(1, "Charla de Inteligencia Artificial", 30, "Dra. Gómez");
        evento4.crearActividad(2, "Taller de html", 20, false);
        evento4.crearActividad(3, "Curso de Estadística", 15, 2);

        mostrarFiltradoYCostos(evento3);
        mostrarFiltradoYCostos(evento4);
    }

    private static void mostrarFiltradoYCostos(EventoUniversitario evento) {
        System.out.println("\n--- " + evento.getTitulo() + " ---");

        List<Charla> charlas = evento.filtrarActividadesPorTipo(Charla.class);
        List<Taller> talleres = evento.filtrarActividadesPorTipo(Taller.class);
        List<Curso> cursos = evento.filtrarActividadesPorTipo(Curso.class);

        System.out.println("Cantidad de charlas: " + charlas.size());
        System.out.println("Cantidad de talleres: " + talleres.size());
        System.out.println("Cantidad de cursos: " + cursos.size());

        System.out.printf("Costo materiales charlas: $%.2f%n", evento.calcularCostoMateriales(charlas));
        System.out.printf("Costo materiales talleres: $%.2f%n", evento.calcularCostoMateriales(talleres));
        System.out.printf("Costo materiales cursos: $%.2f%n", evento.calcularCostoMateriales(cursos));

        if (!charlas.isEmpty()) {
            System.out.println("Tipo real del primer elemento de 'charlas': " + charlas.get(0).getClass().getSimpleName());
        }
        if (!talleres.isEmpty()) {
            System.out.println("Tipo real del primer elemento de 'talleres': " + talleres.get(0).getClass().getSimpleName());
        }
        if (!cursos.isEmpty()) {
            System.out.println("Tipo real del primer elemento de 'cursos': " + cursos.get(0).getClass().getSimpleName());
        }
    }

}