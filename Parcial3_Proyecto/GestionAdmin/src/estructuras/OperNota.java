package estructuras;
import modelo.Estudiante;

public class OperNota extends Oper {

    private Estudiante estudiante;
    private int semestre;
    private int posicionMateria;
    private Double notaAnterior; // double por la nota
    private double notaNueva;

    public OperNota(Estudiante estudiante, int semestre, int posicionMateria, Double notaAnterior, double notaNueva) {
        super("Cambiar nota de " + estudiante.getNombre());
        this.estudiante = estudiante;
        this.semestre = semestre;
        this.posicionMateria = posicionMateria;
        this.notaAnterior = notaAnterior;
        this.notaNueva = notaNueva;
    }

    @Override
    public void deshacer() {
        if (notaAnterior == null) {
            estudiante.borrarNota(semestre, posicionMateria); // vuelve a quedar sin nota
            estudiante.setCodigo(semestre, posicionMateria, null);
        } else {
            estudiante.registrarNota(semestre, posicionMateria, notaAnterior);
        }
    }

    @Override
    public void rehacer() {
        estudiante.registrarNota(semestre, posicionMateria, notaNueva);
    }
}
