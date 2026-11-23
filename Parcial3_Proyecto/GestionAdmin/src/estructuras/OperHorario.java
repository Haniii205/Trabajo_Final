package estructuras;
import modelo.Aula;

public class OperHorario extends Oper {
    private Aula aula;
    private int dia;
    private int hora;
    private int duracion;

    public OperHorario(Aula aula, int dia, int hora, int duracion) {
        super("Reservar aula " + aula.getNombre() + " el dia " + dia + " a las " + hora);
        this.aula = aula;
        this.dia = dia;
        this.hora = hora;
        this.duracion = duracion;
    }

    @Override
    public void deshacer() {
        aula.liberar(dia, hora, duracion);
    }

    @Override
    public void rehacer() {
        try {
            aula.reservar(dia, hora, duracion);
        } catch (Exception e) {
            System.out.println("Error al rehacer horario: " + e.getMessage());
        }
    }
}