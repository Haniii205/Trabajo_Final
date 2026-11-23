package estructuras;
import modelo.Aula;

public class OperLiberarHorario extends Oper {
    private Aula aula;
    private int dia;
    private int hora;
    private int duracion;

    public OperLiberarHorario(Aula aula, int dia, int hora, int duracion) {
        super("Liberar aula " + aula.getNombre() + " el dia " + dia + " a las " + hora);
        this.aula = aula;
        this.dia = dia;
        this.hora = hora;
        this.duracion = duracion;
    }

    @Override
    public void deshacer() {
        // Para deshacer la liberación, volvemos a reservar el aula
        try {
            aula.reservar(dia, hora, duracion);
        } catch (Exception e) {
            System.out.println("Error al deshacer liberacion: " + e.getMessage());
        }
    }

    @Override
    public void rehacer() {
        // Para rehacer, volvemos a liberar el aula
        aula.liberar(dia, hora, duracion);
    }
}