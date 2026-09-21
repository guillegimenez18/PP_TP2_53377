package modelo.actividades;

public class Charla extends Actividad {
    private static final long serialVersionUID = 1L;

    private String disertante;

    public Charla(int id, String titulo, int cupoMaximo, String disertante){
        super(id, titulo, cupoMaximo); //arma la parte heredada de Actividad
        this.disertante = disertante;
    }

    @Override
    public double calcularCostoMateriales() {
        return 0;
    }

    @Override
    public String getTipo(){
      return "Charla";
    }
    public String getDisertante(){
        return disertante;
    }
}
