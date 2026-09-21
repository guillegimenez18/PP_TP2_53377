package modelo;

import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Taller;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class EventoUniversitario implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String titulo;
    private double costoBase;
    private boolean gratuito;
    private static int cantidadEventos;

    private Sala sala;
    private List<Actividad> actividades = new ArrayList<>();

    //constructor estático
    static {
        cantidadEventos = 0;
    }

    // constructor principal
    public EventoUniversitario(String id, String titulo, double costoBase, boolean gratuito) {
        this.id = id;
        setTitulo(titulo);
        setCostoBase(costoBase);
        setGratuito(gratuito);

        cantidadEventos++;
    }

    // constructor de copia
    public EventoUniversitario(EventoUniversitario evento1) {
        this.id = evento1.id;
        this.titulo = evento1.titulo;
        this.costoBase = evento1.costoBase;
        this.gratuito = evento1.gratuito;
        cantidadEventos++;
    }

    public void setTitulo(String titulo) {
        if (titulo != null && !titulo.isEmpty())
            this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setCostoBase(double costoBase) {
        if (costoBase >= 0)
            this.costoBase = costoBase;
    }

    public double getCostoBase() {
        return costoBase;
    }

    public void setGratuito(boolean gratuito) {
        this.gratuito = gratuito;
    }

    public boolean isGratuito() {
        return gratuito;
    }

    public static int getCantidadEventos() {
        return cantidadEventos;
    }

    public double calcularCostoEstimado() {
        if (gratuito) {
            return 0;
        }
        double costoActividades = 0;
        for (Actividad actividad : actividades) {
            costoActividades += actividad.calcularCostoMateriales();
        }
        return (costoBase + costoActividades) * 1.21;
    }

    public void asignarSala(Sala sala) {

        this.sala = sala;
    }

    // Para crear una Charla
    public void crearActividad(int id, String titulo, int cupo, String disertante) {
        Actividad actividad = new Charla(id, titulo, cupo, disertante);
        this.actividades.add(actividad);
    }

    // Para crear un Taller
    public void crearActividad(int id, String titulo, int cupo, boolean requiereNotebook) {
        Actividad actividad = new Taller(id, titulo, cupo, requiereNotebook);
        this.actividades.add(actividad);
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void mostrarDatos() {
        System.out.println("Datos del Evento: " + getTitulo());
        System.out.println("ID: " + id);
        System.out.println("Costo Base: $" + costoBase);
        System.out.println("Gratuito: " + (gratuito ? "Sí" : "No"));
        System.out.println("Costo Estimado: $" + calcularCostoEstimado());
        System.out.println("Sala asignada: " + (sala != null ? sala.getNombre() : "(sin asignar)"));
        System.out.println("Actividades: ");
        if (actividades.isEmpty()) {
            System.out.println("sin actividades");
        }
        for (Actividad actividad : actividades) {
            actividad.mostrarIdentificacion();
            actividad.mostrarInscripciones();
        }
    }
    public boolean persistirEvento()throws IOException {
        String nombreArchivo = "evento" + this.id + ".dat";
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(this);
        }
        return true;
    }
    public EventoUniversitario recuperarEvento(String id) throws IOException, ClassNotFoundException {
        String nombreArchivo = "evento" + id + ".dat";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
            return (EventoUniversitario) ois.readObject();
        }
    }

}