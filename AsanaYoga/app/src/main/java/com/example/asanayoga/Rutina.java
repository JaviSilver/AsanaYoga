package com.example.asanayoga;

import java.util.List;

/**
 * Clase que representa una rutina creada por un usuario.
 */
public class Rutina {
    private int id; // ID único de la rutina
    private String nombre; // Nombre de la rutina
    private List<Asana> asanas; // Lista de asanas que forman parte de la rutina

    // Constructor para cuando solo se necesita el nombre de la rutina
    public Rutina(String nombre) {
        this.nombre = nombre;
    }

    // Constructor para cuando se necesita el nombre y las asanas
    public Rutina(String nombre, List<Asana> asanas) {
        this.nombre = nombre;
        this.asanas = asanas;
    }

    // Constructor para cuando se necesita el ID, nombre y lista de asanas
    public Rutina(int id, String nombre, List<Asana> asanas) {
        this.id = id;
        this.nombre = nombre;
        this.asanas = asanas;
    }

    // Constructor para cuando solo se necesita el ID y nombre
    public Rutina(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getter para el nombre
    public String getNombre() {
        return nombre;
    }

    // Setter para el nombre
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter para el ID
    public int getId() {
        return id;
    }

    // Setter para el ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter para la lista de asanas
    public List<Asana> getAsanas() {
        return asanas;
    }

    // Setter para la lista de asanas
    public void setAsanas(List<Asana> asanas) {
        this.asanas = asanas;
    }

    /**
     * Genera una descripción textual de la rutina, listando todas las asanas.
     *
     * @return Una cadena con los nombres de las asanas en formato de lista.
     */
    public String generarDescripcion() {
        StringBuilder descripcion = new StringBuilder("Asanas:\n");
        for (Asana asana : asanas) {
            descripcion.append("- ").append(asana.getNombre()).append("\n");
        }
        return descripcion.toString();
    }
}
