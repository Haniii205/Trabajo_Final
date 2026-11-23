package modelo;

import excepciones.HorarioConflictivoException;

public class Aula {

    //Dias de la semana a elegir
    private static final String[] DIAS =
        {"Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};

    private String nombre;
    private int capacidad;
    private boolean[][] horario = new boolean[7][24];

    public Aula(String nombre, int capacidad) {
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public String getNombre() { 
        return nombre; }
    public int getCapacidad() { 
        return capacidad; }

    public boolean consultarDisponibilidad(int dia, int hora) {
        validar(dia, hora);
        return !horario[dia][hora]; //invierte el valor, osea que si hay disponibilidad
    }

    public void reservar(int dia, int hora, int duracion) throws HorarioConflictivoException {
        validar(dia, hora);
        if (duracion < 1 || hora + duracion > 24) {
            throw new IllegalArgumentException("Duración inválida");
        }

        // revisa toas las horas antes de marcar nada
        for (int h = hora; h < hora + duracion; h++) {
            if (horario[dia][h]) {
                throw new HorarioConflictivoException(
                    DIAS[dia] + " " + h + ":00 ya esta reservado");
            }
        }

        // si todas tan libres, reservar
        for (int h = hora; h < hora + duracion; h++) {
            horario[dia][h] = true;
        }
    }

    public void liberar(int dia, int hora, int duracion) {
        validar(dia, hora);
        for (int h = hora; h < hora + duracion && h < 24; h++) {
            horario[dia][h] = false;
        }
    }

    private void validar(int dia, int hora) {
        if (dia < 0 || dia > 6 || hora < 0 || hora > 23) {
            throw new IllegalArgumentException("El día o la hora está fuera del rango");
        }
    }
}
