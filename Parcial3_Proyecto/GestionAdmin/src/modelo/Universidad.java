package modelo;

import estructuras.Edificio;
import estructuras.Grafo;
import estructuras.Oper;
import excepciones.ColaDeEsperaVaciaException;
import excepciones.CupoLlenoException;
import excepciones.EstudianteNoEncontradoException;
import excepciones.MateriaNoEncontradaException;
import excepciones.PilaDeshacerVaciaException;
import excepciones.PreRequisitoNoAprobadoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.TreeMap;

public class Universidad {

    private HashMap<String, Estudiante> estudiantes = new HashMap<>();
    private HashMap<String, Materia> materias = new HashMap<>();
    private Stack<Oper> pilaDeshacer = new Stack<>();
    private Stack<Oper> pilaRehacer = new Stack<>();
    private Stack<Estudiante> pilaReportes = new Stack<>();
    private Stack<Estudiante> pilaReportesAdelante = new Stack<>();
    private TreeMap<String, Aula> aulas = new TreeMap<>();
    private Grafo campus;
    private Facultad[] facultades = new Facultad[5];

    // aqui lo predeterminado
    public Universidad() {

        //las aulas
        aulas.put("101", new Aula("101", 30));
        aulas.put("102", new Aula("102", 40));
        aulas.put("Lab1", new Aula("Lab1", 20));

        //los departamentos o campus
        campus = new Grafo(10);
        campus.agregarEdificio(new Edificio(0, "Ingenieria"));
        campus.agregarEdificio(new Edificio(1, "Biblioteca"));
        campus.agregarEdificio(new Edificio(2, "Cafeteria"));
        campus.agregarEdificio(new Edificio(3, "Rectoria"));
        campus.agregarEdificio(new Edificio(4, "Laboratorios"));

        campus.agregarConexion(0, 2, 150);
        campus.agregarConexion(2, 3, 180);
        campus.agregarConexion(0, 1, 200);
        campus.agregarConexion(1, 3, 400);

        // las materias o faculytad
        facultades[0] = new Facultad("Ingenieria");
        facultades[1] = new Facultad("Medicina");
        facultades[2] = new Facultad("Derecho");
        facultades[3] = new Facultad("Ciencias Economicas");
        facultades[4] = new Facultad("Artes");

    }

    // estudiante desde aki
    // true si se registró, false si el ID ya existía
    public boolean registrarEstudiante(Estudiante e) {
        if (estudiantes.containsKey(e.getId())) {
            return false;
        }
        estudiantes.put(e.getId(), e);
        return true;
    }

    public Estudiante buscarEstudiante(String id) throws EstudianteNoEncontradoException {
        if (!estudiantes.containsKey(id)) {
            throw new EstudianteNoEncontradoException("No existe estudiante con ID: " + id);
        }
        return estudiantes.get(id);
    }

    public ArrayList<Estudiante> listarEstudiantes() {
        return new ArrayList<>(estudiantes.values());
    }

    public Estudiante eliminarEstudiante(String id) throws EstudianteNoEncontradoException {
        Estudiante e = buscarEstudiante(id);
        estudiantes.remove(id);
        // Sacarlo de las materias pa que no siga ocupando un cupo
        for (Materia m : materias.values()) {
            if (m.estaInscrito(e) || m.estaEnCola(e)) {
                m.cancelarInscripcion(e);
            }
        }
        return e;
    }

    // materias desde aki
    public boolean crearMateria(Materia m) {
        if (materias.containsKey(m.getCodigo())) {
            return false;
        }
        materias.put(m.getCodigo(), m);
        return true;
    }

    // Devuelve null si no existe
    public Materia buscarMateria(String codigo) {
        return materias.get(codigo);
    }

    // inscirpciones aki
    public boolean inscribir(String idEstudiante, String codigoMateria)
            throws EstudianteNoEncontradoException,
            PreRequisitoNoAprobadoException,
            CupoLlenoException,
            MateriaNoEncontradaException {

        Estudiante e = buscarEstudiante(idEstudiante);
        Materia m = materias.get(codigoMateria);
        if (m == null) {
            throw new MateriaNoEncontradaException(
                    "No existe la materia con codigo: " + codigoMateria);
        }

        if (m.estaInscrito(e) || m.estaEnCola(e)) {
            return false; // ya estaba
        }

        Materia faltante = m.prerrequisitoFaltante(e);
        if (faltante != null) {
            throw new PreRequisitoNoAprobadoException(
                    "Falta aprobar " + faltante.getCodigo() + " - " + faltante.getNombre());
        }

        if (!m.tieneCupo()) {
            m.encolar(e);
            throw new CupoLlenoException("Materia llena. Agregada a cola de espera....");
        }

        m.agregarInscrito(e);
        return true;
    }

    public Estudiante cancelarInscripcion(String idEstudiante, String codigoMateria)
            throws EstudianteNoEncontradoException,
            MateriaNoEncontradaException {
        Estudiante e = buscarEstudiante(idEstudiante);
        Materia m = materias.get(codigoMateria);
        if (m == null) {
            throw new MateriaNoEncontradaException(
                    "No existe la materia con codigo: " + codigoMateria);
        }
        return m.cancelarInscripcion(e);
    }

    // metodo grafo
    public Aula buscarAula(String nombre) {
        return aulas.get(nombre);
    }

    public void listarAulas() {
        System.out.println("Aulas registradas:");
        for (Aula a : aulas.values()) {
            System.out.println(" - " + a.getNombre() + " (Capacidad: " + a.getCapacidad() + ")");
        }
    }

    public void listarFacultades() {
        System.out.println("Facultades:");
        for (int i = 0; i < facultades.length; i++) {
            System.out.println("  " + (i + 1) + ". " + facultades[i].getNombre());
        }
    }

// posicion 0-4; devuelve vacioo si no existe
    public Facultad getFacultad(int posicion) {
        if (posicion < 0 || posicion >= facultades.length) {
            return null;
        }
        return facultades[posicion];
    }

    public Grafo getCampus() {
        return campus;
    }

    // Asigna el primero de la cola
    public Estudiante asignarSiguienteDeCola(String codigoMateria)
            throws ColaDeEsperaVaciaException,
            MateriaNoEncontradaException {
        Materia m = materias.get(codigoMateria);
        if (m == null) {
            throw new MateriaNoEncontradaException(
                    "No existe la materia con codigo: " + codigoMateria);
        }
        if (m.getColaEspera().isEmpty()) {
            throw new ColaDeEsperaVaciaException("No hay nadie en la cola de " + codigoMateria);
        }
        Estudiante siguiente = m.getColaEspera().poll();
        m.agregarInscrito(siguiente);
        return siguiente;
    }

    //°°°°°°°° aqui van las operaciones °°°°°°°°°°
    public void guardarOperacion(Oper operacion) {
        pilaDeshacer.push(operacion);
        pilaRehacer.clear(); // Si el usuario hace algo nuevo, borra el futuro de rehacer
    }

    public void deshacer() throws PilaDeshacerVaciaException {
        if (pilaDeshacer.isEmpty()) {
            throw new PilaDeshacerVaciaException("No hay operaciones para deshacer.");
        }

        Oper ultimaAccion = pilaDeshacer.pop();
        ultimaAccion.deshacer();
        pilaRehacer.push(ultimaAccion);       // La mueve a la pila de rehacer

        System.out.println("Operacion deshecha: " + ultimaAccion.getDescripcion());
    }

    public void rehacer() throws PilaDeshacerVaciaException {
        if (pilaRehacer.isEmpty()) {
            throw new PilaDeshacerVaciaException("No hay operaciones para rehacer.");
        }

        Oper ultimaAccion = pilaRehacer.pop();  //lo saca de la pila de rehacer
        ultimaAccion.rehacer();
        pilaDeshacer.push(ultimaAccion);       // La devuelve a la pila principal

        System.out.println("Operacion rehecha: " + ultimaAccion.getDescripcion());
    }

    //aqui los reportes 
    public void guardarReporteVisto(Estudiante e) {
        pilaReportes.push(e);
        pilaReportesAdelante.clear(); // ver un reporte nuevo borra el adelante
    }

    public void verReporteAnterior() throws PilaDeshacerVaciaException {
        if (pilaReportes.size() < 2) {
            throw new PilaDeshacerVaciaException("No hay reportes anteriores.");
        }
        pilaReportesAdelante.push(pilaReportes.pop()); // el que estaba viendo pasa a adelante
        mostrarReporteEstudiante(pilaReportes.peek()); // muestra el anterior sin sacarlo
    }

    public void verReporteSiguiente() throws PilaDeshacerVaciaException {
        if (pilaReportesAdelante.isEmpty()) {
            throw new PilaDeshacerVaciaException("No hay reportes siguientes.");
        }
        Estudiante e = pilaReportesAdelante.pop();
        pilaReportes.push(e);
        mostrarReporteEstudiante(e);
    }

    public void mostrarReporteEstudiante(Estudiante e) {
        System.out.println("\n=== Reporte Academico ===");
        System.out.println("Estudiante: " + e.getNombre() + " (ID: " + e.getId() + ")");

        Double[][] notas = e.getNotas(); // la matriz [semestre][materia]

        // notas de cada semestre
        for (int s = 0; s < 10; s++) {
            boolean hayNotas = false;
            for (int m = 0; m < 20; m++) {
                if (notas[s][m] != null) {
                    hayNotas = true;
                }
            }
            if (hayNotas) {
                System.out.println("\nSemestre " + (s + 1) + ":");
                for (int m = 0; m < 20; m++) {
                    if (notas[s][m] != null) {
                        System.out.println("  " + e.getCodigo(s, m) + ": " + notas[s][m]);
                    }
                }
                System.out.println("  Promedio: " + Math.round(e.promedioSemestre(s) * 100.0) / 100.0);
            }
        }

        // resumen
        System.out.println("\n=== Resumen ===");
        System.out.println("Promedio acumulado: " + Math.round(e.promedioAcumulado() * 100.0) / 100.0);
        System.out.println("Materias aprobadas: " + e.contarAprobadas());
        System.out.println("Materias reprobadas: " + e.contarReprobadas());

        // cuales son las reprobadas
        for (int s = 0; s < 10; s++) {
            for (int m = 0; m < 20; m++) {
                if (notas[s][m] != null && notas[s][m] < 3.0) {
                    System.out.println("  - " + e.getCodigo(s, m) + " (semestre " + (s + 1) + "): " + notas[s][m]);
                }
            }
        }

        // historial de materias
        System.out.println("Historial de materias cursadas:");
        if (e.getHistorialMaterias().isEmpty()) {
            System.out.println("  (ninguna)");
        }
        for (Materia mat : e.getHistorialMaterias()) {
            System.out.println("  - " + mat.getCodigo() + " " + mat.getNombre());
        }
    }
}
