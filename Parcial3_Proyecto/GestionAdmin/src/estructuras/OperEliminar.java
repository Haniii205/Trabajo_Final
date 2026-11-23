package estructuras;
import modelo.Estudiante;
import modelo.Universidad;

public class OperEliminar extends Oper {
    private Universidad uni;
    private Estudiante estudiante;

    public OperEliminar(Universidad uni, Estudiante estudiante) {
        super("Eliminar al estudiante: " + estudiante.getNombre());
        this.uni = uni;
        this.estudiante = estudiante;
    }

    @Override
    public void deshacer() {
        // Lo volvemos a registrar en el HashMap
        uni.registrarEstudiante(estudiante);
    }

    @Override
    public void rehacer() {
        try {
            uni.eliminarEstudiante(estudiante.getId());
        } catch (Exception e) {
            System.out.println("Error al rehacer eliminacion.");
        }
    }
}