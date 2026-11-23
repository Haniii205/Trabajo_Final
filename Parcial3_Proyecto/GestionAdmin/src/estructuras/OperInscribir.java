package estructuras;
import modelo.Estudiante;   
import modelo.Materia;

public class OperInscribir extends Oper {

    private Materia materia;
    private Estudiante estudiante;

    public OperInscribir(Materia materia, Estudiante estudiante) {
        super("Inscribir a " + estudiante.getNombre() + " en " + materia.getCodigo());
        this.materia = materia;
        this.estudiante = estudiante;
    }

    @Override
    public void deshacer() {
        materia.cancelarInscripcion(estudiante);
    }

    @Override
    public void rehacer() {
        if (!materia.estaInscrito(estudiante) && !materia.estaEnCola(estudiante)) {
            if (materia.tieneCupo()) {
                materia.agregarInscrito(estudiante);
            } else {
                materia.encolar(estudiante); // si ya no hay cupo, queda en cola
            }
        }
    }
}