import estructuras.*;
import excepciones.*;
import java.util.ArrayList;
import java.util.Scanner;
import modelo.*;

public class Main {

    private static Universidad uni = new Universidad();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion = 0;

        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion para continuar: ");

            switch (opcion) {
                case 1:
                    opcRegistrarEstudiante();
                    break;
                case 2:
                    opcBuscarEstudiante();
                    break;
                case 3:
                    opcListarEstudiantes();
                    break;
                case 4:
                    opcEliminarEstudiante();
                    break;
                case 5:
                    opcCrearMateria();
                    break;
                case 6:
                    opcAgregarPrerrequisito();
                    break;
                case 7:
                    opcMostrarPrerrequisitos();
                    break;
                case 8:
                    opcInscribir();
                    break;
                case 9:
                    opcCancelar();
                    break;
                case 10:
                    opcMostrarCola();
                    break;
                case 11:
                    opcReservarHorario();
                    break;
                case 12:
                    opcLiberarHorario();
                    break;
                case 13:
                    opcConsultarDisponible();
                    break;
                case 14:
                    opcAgregarConexion();
                    break;
                case 15:
                    opcCalcularRuta();
                    break;
                case 16:
                    opcRegistrarNota();
                    break;
                case 17:
                    opcVerReporte();
                    break;
                case 18:
                    opcNavegadorReportes();
                    break;
                case 19:
                    opcDeshacer();
                    break;
                case 20:
                    opcRehacer();
                    break;
                case 21:
                    opcProcesarCSV();
                    break;
                case 22:
                    System.out.println("Saliendo del sistema.");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 22);
    }

    //validaciones por si se llega  a escribir otra opcion no valida
    private static int leerEntero(String msg) {
        System.out.print(msg);
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String leerTexto(String msg) {
        System.out.print(msg);
        return sc.nextLine();
    }

    private static double leerDouble(String msg) {
        System.out.print(msg);
        try {
            return Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n°================ PLANIFICACION ACADEMICA ================°");
        System.out.println("--- ESTUDIANTES ---");
        System.out.println("1. Registrar   \n2. Buscar    \n3. Listar    \n4. Eliminar");
        System.out.println("\n--- MATERIAS ---");
        System.out.println("5. Crear       \n6. Agregar prerrequisitos   \n7. Ver prerrequisitos");
        System.out.println("8. Inscribir   \n9. Cancelar         \n10. Ver cola");
        System.out.println("\n--- HORARIOS ---");
        System.out.println("11. Reservar   \n12. Liberar         \n13. Consultar");
        System.out.println("\n--- RUTAS ---");
        System.out.println("14. Agregar conexion   \n15. Ruta mas corta");
        System.out.println("\n--- REPORTES ---");
        System.out.println("16. Registrar nota   \n17. Ver reporte   \n18. Navegar");
        System.out.println("\n--- DESHACER/REHACER ---");
        System.out.println("19. Deshacer | 20. Rehacer");
        System.out.println("\n--- BATCH ---");
        System.out.println("21. Procesar CSV \n22. Salir");
        System.out.println("°=========================================================°");
    }

    //Registrar un estudiante
    private static void opcRegistrarEstudiante() {
        System.out.println("\n°°° Registro de estudiantes °°°");
        String id = leerTexto("ID: ");
        String nombre = leerTexto("Nombre: ");
        String email = leerTexto("Email: ");
        int semestre = leerEntero("Semestre actual: ");

        if (nombre.trim().isEmpty() || !email.contains("@") || semestre < 1 || semestre > 10) {
            System.out.println("Error: nombre vacio, email sin @ o semestre fuera de 1-10.");
            return;
        }

        Estudiante e = new Estudiante(nombre, id, email, semestre);
        uni.listarFacultades();
        int fac = leerEntero("Facultad (1-5): ");
        Facultad f = uni.getFacultad(fac - 1);
        if (f != null) {
            e.setFacultad(f);
        }

        if (uni.registrarEstudiante(e)) {
            System.out.println("Estudiante registrado exitosamente.");
        } else {
            System.out.println("Error: Ya existe un estudiante con ese ID.");
        }
    }

    //Buscar un estudiante
    private static void opcBuscarEstudiante() {
        System.out.println("\n--- Buscar estudiante ---");
        String id = leerTexto("ID: ");
        try {
            Estudiante e = uni.buscarEstudiante(id);
            System.out.println("\nResultado encontrado:\n" + e.mostrarInformacion());
            System.out.println("Promedio acumulado: " + e.promedioAcumulado());
        } catch (EstudianteNoEncontradoException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    //Lista de estudiantes
    private static void opcListarEstudiantes() {
        System.out.println("\n--- Lista de estudiantes ---");
        ArrayList<Estudiante> lista = uni.listarEstudiantes();
        if (lista.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }
        for (Estudiante e : lista) {
            System.out.println("ID: " + e.getId() + " | Nombre: " + e.getNombre() + " | Semestre: " + e.getSemestreActual());
        }
        System.out.println("Total: " + lista.size());
    }

    private static void opcEliminarEstudiante() {
        System.out.println("\n--- Eliminar estudiante ---");
        String id = leerTexto("ID a eliminar: ");
        try {
            Estudiante e = uni.eliminarEstudiante(id);
            System.out.println("Estudiante eliminado: " + e.getNombre());
            uni.guardarOperacion(new OperEliminar(uni, e));
        } catch (EstudianteNoEncontradoException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }


    //aqui creamos la materia con los atributos que requieren, y si se desea asignar un profesor
    private static void opcCrearMateria() {
        System.out.println("\n°°° Crear una materia °°°");
        String codigo = leerTexto("Codigo (ej: CALC101, FIS101, etc.): ");
        String nombre = leerTexto("Nombre: ");
        int cupos = leerEntero("Cupos maximos: ");
        int creditos = leerEntero("Creditos: ");
        if (cupos <= 0 || creditos <= 0) {
            System.out.println("Error: cupos y creditos deben ser mayores que 0.");
            return;
        }

        Materia m = new Materia(codigo, nombre, cupos, creditos);
        String grupo = leerTexto("Grupo (A/B/C, o ENTER para omitir): ");
        if (!grupo.trim().isEmpty()) {
            m.setGrupo(grupo);
        }

        //aqui colocamos la opcion del profesor, si se necesita o no
        String deseaProfe = leerTexto("¿Asignar profesor? (s/n): ");
        if (deseaProfe.equalsIgnoreCase("s")) {
            String idProfe = leerTexto("ID Profesor: ");
            String nombreProfe = leerTexto("Nombre Profesor: ");
            String emailProfe = leerTexto("Email Profesor: ");
            uni.listarFacultades(); // Muestra las 5 facultades
            int facProfe = leerEntero("Seleccione el departamento del profesor (1-5): ");
            Facultad fProfe = uni.getFacultad(facProfe - 1); 
            String depto = "";             //aca esta vacia pero mas adelante se le asignará un valor
            
            // si es diferente a vacio, obtenemos el nombre
            if (fProfe != null) {
                depto = fProfe.getNombre();
            } else {
                System.out.println("Error: Departamento inválido. Se asignará 'Sin asignar'.");
                depto = "";
            }

            // Validar correo y departamento
            if (nombreProfe.trim().isEmpty() || !emailProfe.contains("@")) {
                System.out.println("Error: El nombre del profesor no puede estar vacío y el email debe contener '@'.");
                System.out.println("La materia se creará sin profesor 'no asignado'.");
            } else {
                m.setProfesor(new Profesor(nombreProfe, idProfe, emailProfe, depto));
                System.out.println("Profesor asignado correctamente al departamento de " + depto + ".");
            }
        }

        if (uni.crearMateria(m)) {
            System.out.println("Materia creada exitosamente:\n" + m);
        } else {
            System.out.println("Error: Ya existe una materia con ese codigo.");
        }
    }

    //Agregar prerrequisitos
    private static void opcAgregarPrerrequisito() {
        System.out.println("\n--- Agregar Prerequisito ---");
        String codMateria = leerTexto("Codigo de la materia: ");
        String codReq = leerTexto("Codigo del prerrequisito: ");

        Materia m = uni.buscarMateria(codMateria);
        Materia req = uni.buscarMateria(codReq);

        if (m == null) {
            System.out.println("Error: No existe la materia " + codMateria);
            return;
        }
        if (req == null) {
            System.out.println("Error: No existe la materia " + codReq);
            return;
        }

        if (m.agregarPrerrequisito(req)) {
            System.out.println("Prerrequisito agregado.");
        } else {
            System.out.println("No se pudo agregar.");
        }
    }

    //Muestras de prerrequisitos con las materias enlazadas a ella
    private static void opcMostrarPrerrequisitos() {
        System.out.println("\n--- Prerrequisitos ---");
        String cod = leerTexto("Codigo de la materia: ");
        Materia m = uni.buscarMateria(cod);
        if (m == null) {
            System.out.println("Error: No existe la materia.");
            return;
        }

        System.out.println("Materia: " + m);
        if (m.getPrerrequisitos().isEmpty()) {
            System.out.println("No tiene prerrequisitos.");
        } else {
            for (Materia req : m.getPrerrequisitos()) {
                System.out.println("  - " + req);
            }
        }
    }

    //Inscripcion del estudiante con la materia
    private static void opcInscribir() {
        System.out.println("\n--- Inscribir estudiante ---");
        String id = leerTexto("ID Estudiante: ");
        String cod = leerTexto("Codigo Materia: ");
        try {
            if (uni.inscribir(id, cod)) {
                System.out.println("Inscripcion exitosa.");
                uni.guardarOperacion(new OperInscribir(uni.buscarMateria(cod), uni.buscarEstudiante(id)));
            } else {
                System.out.println("Ya estaba inscrito o en cola.");
            }
        } catch (EstudianteNoEncontradoException ex) {
            System.out.println("Error: " + ex.getMessage());
        } catch (PreRequisitoNoAprobadoException ex) {
            System.out.println("Error: " + ex.getMessage());
        } catch (MateriaNoEncontradaException ex) {
            System.out.println("Error: " + ex.getMessage());
        } catch (CupoLlenoException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //Cancelar accion de inscripcion
    private static void opcCancelar() {
        System.out.println("\n--- Cancelar inscripcion ---");
        String id = leerTexto("ID Estudiante: ");
        String cod = leerTexto("Codigo Materia: ");
        try {
            Estudiante est = uni.buscarEstudiante(id);
            Materia mat = uni.buscarMateria(cod);
            if (mat != null && !mat.estaInscrito(est) && !mat.estaEnCola(est)) {
                System.out.println("Error: el estudiante no esta inscrito ni en cola de esa materia.");
                return;
            }
            Estudiante promovido = uni.cancelarInscripcion(id, cod);
            System.out.println("Cancelacion exitosa. Cupo liberado.");
            if (promovido != null) {
                System.out.println("Asignando cupo a " + promovido.getNombre());
            }
            uni.guardarOperacion(new OperCancelar(uni.buscarMateria(cod), est));
        } catch (EstudianteNoEncontradoException ex) {
            System.out.println("Error: " + ex.getMessage());
        } catch (MateriaNoEncontradaException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void opcMostrarCola() {
        System.out.println("\n--- Cola de espera ---");
        String cod = leerTexto("Codigo de la materia: ");
        Materia m = uni.buscarMateria(cod);
        if (m == null) {
            System.out.println("Error: No existe la materia.");
            return;
        }
        System.out.println("Materia: " + m);
        m.mostrarColaEspera();
    }

    // Metodos de los horarios
    private static void opcReservarHorario() {
        System.out.println("\n--- Reservar horario ---");
        uni.listarAulas();
        String nombreAula = leerTexto("Nombre del aula: ");
        Aula aula = uni.buscarAula(nombreAula);
        if (aula == null) {
            System.out.println("Error: El aula no existe.");
            return;
        }

        int dia = leerEntero("Dias a elegir: (domingo = 0, 1 = lunes, 2 = martes, 3 = miércoles, 4 = jueves, 5 = viernes, 6 = sábado): ");
        int hora = leerEntero("Hora de inicio (0-23): ");
        int duracion = leerEntero("Duracion (horas): ");

        try {
            aula.reservar(dia, hora, duracion);
            System.out.println("Reserva exitosa.");
            uni.guardarOperacion(new OperHorario(aula, dia, hora, duracion));
        } catch (HorarioConflictivoException ex) {
            System.out.println("Error: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    //operacion para liberar el horario o devolver accion
    private static void opcLiberarHorario() {
        System.out.println("\n--- Liberar horario ---");
        String nombreAula = leerTexto("Nombre del aula: ");
        Aula aula = uni.buscarAula(nombreAula);
        if (aula == null) {
            System.out.println("Error: El aula no existe.");
            return;
        }

        int dia = leerEntero("Dias a elegir: domingo = 0, 1 = lunes, 2 = martes, 3 = miércoles, 4 = jueves, 5 = viernes, 6 = sábado: ");
        int hora = leerEntero("Hora de inicio (0-23): ");
        int duracion = leerEntero("Duracion (horas): ");

        try {
            aula.liberar(dia, hora, duracion);
            System.out.println("Horario liberado exitosamente.");
            uni.guardarOperacion(new OperLiberarHorario(aula, dia, hora, duracion));
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    //para ver si un aula esta disponible
    private static void opcConsultarDisponible() {
        System.out.println("\n--- Consultar disponibilidad ---");
        String nombreAula = leerTexto("Nombre del aula: ");
        Aula aula = uni.buscarAula(nombreAula);
        if (aula == null) {
            System.out.println("Error: El aula no existe.");
            return;
        }

        int dia = leerEntero("Dias a elegir: domingo = 0, 1 = lunes, 2 = martes, 3 = miércoles, 4 = jueves, 5 = viernes, 6 = sábado: ");
        int hora = leerEntero("Hora (0-23): ");

        try {
            if (aula.consultarDisponibilidad(dia, hora)) {
                System.out.println("El espacio está libre.");
            } else {
                System.out.println("El espacio esta ocupado.");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    // agregar ruta de conexion aqui 
    private static void opcAgregarConexion() {
        System.out.println("\n--- Agregar conexion entre edificios ---");
        uni.getCampus().mostrarEdificios();
        int origen = leerEntero("ID Edificio Origen: ");
        int destino = leerEntero("ID Edificio Destino: ");
        int distancia = leerEntero("Distancia en metros: ");
        if (uni.getCampus().agregarConexion(origen, destino, distancia)) {
            System.out.println("Conexion agregada exitosamente.");
        } else {
            System.out.println("Error: edificios invalidos, iguales o distancias no validas.");
        }
    }

    //dar una respuesta cuando se calcula una ruta de un punto a al b
    private static void opcCalcularRuta() {
        System.out.println("\n--- Calcular una ruta mas corta ---");
        uni.getCampus().mostrarEdificios();
        int origen = leerEntero("ID Edificio Origen: ");
        int destino = leerEntero("ID Edificio Destino: ");
        uni.getCampus().calcularRutaMasCorta(origen, destino);
    }

    //registramos las notas aqui
    private static void opcRegistrarNota() {
        System.out.println("\n--- Registrar nota ---");
        String id = leerTexto("ID Estudiante: ");
        String codMateria = leerTexto("Codigo de la Materia (ej: CALC101): ");
        try {
            Estudiante e = uni.buscarEstudiante(id);
            Materia mat = uni.buscarMateria(codMateria);
            if (mat == null) {
                System.out.println("Error: No existe la materia " + codMateria);
                return;
            }
            int semestre = leerEntero("Semestre (0-9): ");
            int posicion = leerEntero("Posicion materia (0-19): ");
            double nota = leerDouble("Nota (0.0 - 5.0): ");

            // validar antes de tocar la matriz
            if (semestre < 0 || semestre > 9 || posicion < 0 || posicion > 19) {
                System.out.println("Error: semestre 0-9 y posicion 0-19.");
                return;
            }
            if (nota < 0.0 || nota > 5.0) {
                System.out.println("Error: la nota debe estar entre 0.0 y 5.0.");
                return;
            }

            //colocamos las notas que tenia antes y las reemplazamos
            Double notaAnterior = e.getNota(semestre, posicion);
            e.registrarNota(semestre, posicion, nota);
            e.setCodigo(semestre, posicion, codMateria);
            if (!e.getHistorialMaterias().contains(mat)) {
                e.getHistorialMaterias().add(mat);   // lista del historial
            }

            if (nota >= 3.0) {
                e.marcarAprobada(codMateria);
                System.out.println("Nota registrada y materia marcada como APROBADA.");
            } else {
                System.out.println("Nota registrada (Materia reprobada).");
            }

            uni.guardarOperacion(new OperNota(e, semestre, posicion, notaAnterior, nota));
        } catch (EstudianteNoEncontradoException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    //ver reporte de cada estudiante
    private static void opcVerReporte() {
        System.out.println("\n--- Reporte academico ---");
        String id = leerTexto("ID Estudiante: ");
        try {
            Estudiante e = uni.buscarEstudiante(id);
            uni.mostrarReporteEstudiante(e);
            uni.guardarReporteVisto(e);
        } catch (EstudianteNoEncontradoException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    //navegar como si fuera paginas entre los reportes
    private static void opcNavegadorReportes() {
        System.out.println("\n--- Navegador de reportes ---");
        String dir = leerTexto("(A)tras o (S)iguiente: ");
        try {
            if (dir.equalsIgnoreCase("A")) {
                uni.verReporteAnterior();
            } else if (dir.equalsIgnoreCase("S")) {
                uni.verReporteSiguiente();
            } else {
                System.out.println("Opcion invalida.");
            }
        } catch (PilaDeshacerVaciaException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    //deshacer accion
    private static void opcDeshacer() {
        System.out.println("\n--- DESHACER ---");
        try {
            uni.deshacer();
        } catch (PilaDeshacerVaciaException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    //rehacer una accion
    private static void opcRehacer() {
        System.out.println("\n--- REHACER ---");
        try {
            uni.rehacer();
        } catch (PilaDeshacerVaciaException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

        //aqui se procesa el archivo csv
        private static void opcProcesarCSV() {
        System.out.println("\n--- Procesamiento ---");
        try {
            util.LectorCSV.procesar("GestionAdmin/src/inscripciones.csv", uni); 
        } catch (ArchivoInvalidoException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
