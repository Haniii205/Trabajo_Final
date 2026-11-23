package estructuras;
import modelo.Estudiante;
import modelo.Materia;

public class OperCancelar extends Oper {
    private Materia materia;
    private Estudiante estudiante;

    public OperCancelar(Materia materia, Estudiante estudiante) {
        super("Cancelar inscripcion de " + estudiante.getNombre() + " en " + materia.getCodigo());
        this.materia = materia;
        this.estudiante = estudiante;
    }

    @Override
public void deshacer() {
    // Lo volvemos a inscribir (si hay cupo) o a la cola
    if (!materia.estaInscrito(estudiante) && !materia.estaEnCola(estudiante)) {
            if (materia.tieneCupo()) {
                materia.agregarInscrito(estudiante);
            } else {
                materia.encolar(estudiante);
            }
        }
    }

    @Override
    public void rehacer() {
        // Lo volvemos a cancelar
        materia.cancelarInscripcion(estudiante);
    }
}
