package modelo.actividades;

import modelo.certificacion.Certificable;
import modelo.Estudiante;

public class Curso extends Actividad implements Certificable {
    private static final long serialVersionUID = 1L;

    private int nivel;

    public Curso (int id, String titulo, int cupoMaximo, int nivel){
        super(id, titulo, cupoMaximo);
        this.nivel = nivel;
    }

    @Override
    public double calcularCostoMateriales() {
        return 200 + nivel * 20;
    }

    @Override
    public String getTipo(){
        return "Curso";
    }

    @Override
    public String generarCertificado(Estudiante estudiante){
        return "Certificado de asistencia (" + ENTIDAD_EMISORA + "): " + estudiante.getNombre() + " - legajo: " + estudiante.getLegajo() + " - participó en el curso: \"" + getTitulo() + "\".";
    }

    public int getNivel(){
        return nivel;
    }
}

