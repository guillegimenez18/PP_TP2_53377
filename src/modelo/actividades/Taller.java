package modelo.actividades;

import modelo.certificacion.Certificable;
import modelo.Estudiante;

public class Taller extends Actividad implements Certificable {
    private static final long serialVersionUID = 1L;

    private boolean requiereNotebook;

    public Taller(int id, String titulo, int cupoMaximo, boolean requiereNotebook){
        super(id, titulo, cupoMaximo);
        this.requiereNotebook = requiereNotebook;
    }

    @Override
    public double calcularCostoMateriales() {
        return requiereNotebook ? 5000 : 2000;
    }

    @Override
    public String getTipo() {
        return "Taller";
    }
    public boolean isRequiereNotebook(){
        return requiereNotebook;
    }

    @Override
    public String generarCertificado(Estudiante estudiante){
        return "Certificado de asistencia (" + ENTIDAD_EMISORA + "): " + estudiante.getNombre() + " - legajo: " + estudiante.getLegajo() + " - participó en el taller: \"" + getTitulo() + "\".";
    }
}
