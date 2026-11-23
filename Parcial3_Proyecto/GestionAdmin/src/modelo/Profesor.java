package modelo;

public class Profesor extends Persona {

    private String departamento;

    public Profesor(String nombre, String id, String email, String departamento) {
        super(nombre, id, email);
        this.departamento = departamento;
    }

    public String getDepartamento() { 
        return departamento; }

    @Override
    public String mostrarInformacion() {
        return "Profesor: " + getNombre() + " | Departamento: " + departamento;
    }
}
