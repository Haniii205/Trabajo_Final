package modelo;

import java.util.LinkedList;

public class Estudiante extends Persona {

    private int semestreActual;
    private Double[][] notas;                      // (semestre)(materia) waos
    private LinkedList<Materia> historialMaterias; // lista enlazada
    private LinkedList<String> aprobadas;          // codigos de materias aprobadas
    private String[][] codigos = new String[10][20]; // codigo de la materia de cada nota
    private Facultad facultad;

    public Estudiante(String nombre, String id, String email, int semestreActual) {
        super(nombre, id, email);
        this.semestreActual = semestreActual;
        this.notas = new Double[10][20];
        this.historialMaterias = new LinkedList<>();
        this.aprobadas = new LinkedList<>();
    }

    public int getSemestreActual() {
        return semestreActual;
    }

    public void setSemestreActual(int semestreActual) {
        this.semestreActual = semestreActual;
    }

    public Double[][] getNotas() {
        return notas;
    }

    public LinkedList<Materia> getHistorialMaterias() {
        return historialMaterias;
    }

    public boolean aprobo(String codigoMateria) {
        return aprobadas.contains(codigoMateria);
    }

    public void marcarAprobada(String codigoMateria) {
        if (!aprobadas.contains(codigoMateria)) {
            aprobadas.add(codigoMateria);
        }
    }
//para hacer la evaluacion de notitas

    public boolean registrarNota(int semestre, int materia, double nota) {
        if (semestre < 0 || semestre >= 10 || materia < 0 || materia >= 20) {
            System.out.println("Error: posición fuera de rango (semestre 0-9, materia 0-19)");
            return false;
        }
        if (nota < 0.0 || nota > 5.0) {
            System.out.println("Error: la nota debe estar entre 0.0 y 5.0 para ser validada.");
            return false;
        }
        notas[semestre][materia] = nota;
        return true;
    }

// Devuelve la nota o vacio si no hay nota (o la posicion no existe)
    public Double getNota(int semestre, int materia) {
        if (semestre < 0 || semestre >= 10 || materia < 0 || materia >= 20) {
            return null;
        }
        return notas[semestre][materia];
    }

// Deja la posicion sin nota (null), no en 0.0
    public void borrarNota(int semestre, int materia) {
        if (semestre >= 0 && semestre < 10 && materia >= 0 && materia < 20) {
            notas[semestre][materia] = null;
        }
    }

    public void setCodigo(int semestre, int materia, String codigo) {
        if (semestre >= 0 && semestre < 10 && materia >= 0 && materia < 20) {
            codigos[semestre][materia] = codigo;
        }
    }

    public String getCodigo(int semestre, int materia) {
        if (semestre < 0 || semestre >= 10 || materia < 0 || materia >= 20) {
            return null;
        }
        return codigos[semestre][materia];
    }

    public Facultad getFacultad() {
        return facultad;
    }

    public void setFacultad(Facultad facultad) {
        this.facultad = facultad;
    }

    // Suma las notas de un smestre y divide entre cuantas hay
    public double promedioSemestre(int semestre) {
        if (semestre < 0 || semestre >= 10) {
            return 0.0; //eto porque para ser mas exactos, q no solo quede en 0, sino en 0.0 la nota
        }
        double suma = 0;
        int contador = 0;
        for (int i = 0; i < 20; i++) {
            if (notas[semestre][i] != null) {
                suma = suma + notas[semestre][i];
                contador++;
            }
        }
        if (contador == 0) {
            return 0.0;
        }
        return suma / contador;
    }

    // Recorre tda la matriz y saca el promedio
    public double promedioAcumulado() {
        double suma = 0;
        int contador = 0;
        for (int s = 0; s < 10; s++) {
            for (int m = 0; m < 20; m++) {
                if (notas[s][m] != null) {
                    suma = suma + notas[s][m];
                    contador++;
                }
            }
        }
        if (contador == 0) {
            return 0.0;
        }
        return suma / contador;
    }

    // no alcanza = nota menor a 3.0
    public int contarReprobadas() {
        int contador = 0;
        for (int s = 0; s < 10; s++) {
            for (int m = 0; m < 20; m++) {
                if (notas[s][m] != null && notas[s][m] < 3.0) {
                    contador++;
                }
            }
        }
        return contador;
    }

    // alcanza = nota mayor o igual a 3.0 (raspando a 3.0)
    public int contarAprobadas() {
        int contador = 0;
        for (int s = 0; s < 10; s++) {
            for (int m = 0; m < 20; m++) {
                if (notas[s][m] != null && notas[s][m] >= 3.0) {
                    contador++;
                }
            }
        }
        return contador;
    }

    @Override
public String mostrarInformacion() {
    String textoFacultad = "Sin asignar";
    if (facultad != null) {
        textoFacultad = facultad.getNombre();
    }
    return "ID: " + getId() + "\n"
            + "Nombre: " + getNombre() + "\n"
            + "Email: " + getEmail() + "\n"
            + "Semestre: " + semestreActual + "\n"
            + "Facultad: " + textoFacultad;
}
}
