package modelo;

public abstract class Persona {

    private String nombre;
    private String id;
    private String email;

    public Persona(String nombre, String id, String email) {
        this.nombre = nombre;
        this.id = id;
        this.email = email;
    }

    public String getNombre() { 
        return nombre; }
    public String getId() { 
        return id; }
    public String getEmail() { 
        return email; }

    public void setNombre(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.nombre = nombre;
        } else {
            System.out.println("Error: El nombre no puede estar vacío.");
        }
    }

    public void setEmail(String email) {
        if (email != null && email.contains("@")) {
            this.email = email;
        } else {
            System.out.println("Error: El email no es válido.");
        }
    }

    public abstract String mostrarInformacion();

    @Override
    public String toString() {
        return "ID: " + id + " | Nombre: " + nombre + " | Email: " + email;
    }
}
