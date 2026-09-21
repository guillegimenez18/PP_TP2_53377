package modelo.actividades;

import excepciones.CupoExcedidoException;
import modelo.Inscripcion;
import modelo.Estudiante;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public abstract class Actividad implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String titulo;
    private int cupoMaximo;
    private List<Inscripcion> inscripciones = new ArrayList<>();

    //variable de clase
    public static final int CUPO_MINIMO;

    //Inicializador estático
    static {
        CUPO_MINIMO = 1;
        System.out.println("Inicializador estático: se cargó la clase Actividad.");
    }

    public Actividad(int id, String titulo, int cupoMaximo){
        this.id = id;
        this.titulo = titulo;
        this.cupoMaximo = cupoMaximo;
    }

    public Inscripcion inscribir(Estudiante estudiante) throws CupoExcedidoException {
        if(inscripciones.size() >= cupoMaximo){
           throw new CupoExcedidoException("No se pudo inscribir a " + estudiante.getNombre() + ": cupo completo en " + titulo + ".");
        }
        Inscripcion inscripcion = new Inscripcion(LocalDate.now(), "Confirmada", estudiante);
        inscripciones.add(inscripcion);
        return inscripcion;
    }

    public void mostrarInscripciones(){
        System.out.println("  Inscripciones en " + titulo + ":");
        if (inscripciones.isEmpty()) {
            System.out.println("sin inscriptos");
        }
        for (Inscripcion i : inscripciones) {
            System.out.println("    - " + i.getEstudiante().getNombre() + " (legajo " + i.getEstudiante().getLegajo()
                    + ") | Fecha: " + i.getFecha() + " | Estado: " + i.getEstado());
        }
    }

    public final void mostrarIdentificacion() {
        System.out.println("[" + getTipo() + "] " + titulo + " (id " + id + ") - Costo materiales: $" + calcularCostoMateriales());
    }

    public abstract double calcularCostoMateriales();

    // Cada subclase concreta dice qué tipo de actividad es
    public abstract String getTipo();

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

}