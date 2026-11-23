package modelo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Materia {

    private String codigo;
    private String nombre;
    private int cuposMaximos;
    private int creditos;
    private Profesor profesor;  //nombre del profe
    private String grupo;       // "A", "B", "C"
    private Aula aula;          // Dónde da clases

    private LinkedList<Materia> prerrequisitos = new LinkedList<>();
    private Queue<Estudiante> colaEspera = new LinkedList<>();
    private ArrayList<Estudiante> inscritos = new ArrayList<>();

    public Materia(String codigo, String nombre, int cuposMaximos, int creditos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cuposMaximos = cuposMaximos;
        this.creditos = creditos;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCuposMaximos() {
        return cuposMaximos;
    }

    public int getCreditos() {
        return creditos;
    }

    public LinkedList<Materia> getPrerrequisitos() {
        return prerrequisitos;
    }

    public Queue<Estudiante> getColaEspera() {
        return colaEspera;
    }

    public int getCuposDisponibles() {
        return cuposMaximos - inscritos.size();
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    // true si se agregó, false si no se pudo (ella misma o repetido)
    public boolean agregarPrerrequisito(Materia requisito) {
        if (requisito == this) {
            return false;
        }
        if (prerrequisitos.contains(requisito)) {
            return false;
        }
        prerrequisitos.add(requisito);
        return true;
    }

    // Devuelve el primer prerrequisito que nou aprobó, o null si cumple todos
    public Materia prerrequisitoFaltante(Estudiante e) {
        for (Materia req : prerrequisitos) {
            if (!e.aprobo(req.getCodigo())) {
                return req;
            }
        }
        return null;
    }

    public boolean tieneCupo() {
        return getCuposDisponibles() > 0;
    }

    public boolean estaInscrito(Estudiante e) {
        return inscritos.contains(e);
    }

    public boolean estaEnCola(Estudiante e) {
        return colaEspera.contains(e);
    }

    public void agregarInscrito(Estudiante e) {
        inscritos.add(e);
    }

    public void encolar(Estudiante e) {
        colaEspera.add(e); // entra al final
    }

    // Cancela y, si hay cola, el primero toma el cupo. Devuelve el estudiante, o vacio nu si no hay nadie. 
    public Estudiante cancelarInscripcion(Estudiante e) {
        if (colaEspera.contains(e)) {   // estaba en espera, sale
            colaEspera.remove(e);
            return null;
        }
        if (!inscritos.contains(e)) {
            return null; // no esta inscrito, no pasa nada
        }
        inscritos.remove(e);
        if (colaEspera.isEmpty()) {
            return null;
        }
        Estudiante siguiente = colaEspera.poll(); // saca el primero
        inscritos.add(siguiente);
        return siguiente;
    }

    public void mostrarColaEspera() {
        if (colaEspera.isEmpty()) {
            System.out.println("Cola de espera vacia.");
            return;
        }
        int pos = 1;
        for (Estudiante e : colaEspera) {
            System.out.println("Posicion " + pos + ": " + e.getNombre());
            pos++;
        }
        System.out.println("Total en espera: " + colaEspera.size());
    }

    @Override
    public String toString() {
        String texto = codigo + " - " + nombre + " (" + creditos + "cr)";
        if (grupo != null) {
            texto = texto + " | Grupo: " + grupo;
        }
        if (profesor != null) {
            texto = texto + " | Prof: " + profesor.getNombre();
        }
        return texto;
    }
}
