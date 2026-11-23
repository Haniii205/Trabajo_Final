package estructuras;

public abstract class Oper {
    private String descripcion;

    public Oper(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Métodos aki
    public abstract void deshacer();
    public abstract void rehacer();
}