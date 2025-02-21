
package com.example.asanayoga;

public class Asana {
    private int id;
    private String nombre;
    private String descripcion;
    private String variante;
    private String dificultad;
    private String parteCuerpo;


    //Constructor para manejar el RecyclerView de la PantallaInicio
    public Asana(String nombre) {
        this.nombre = nombre;
    }


    //Constructor para manejar la BusquedaAvanzada
    public Asana(String nombre, String variante, String dificultad, String parteCuerpo, String descripcion) {
        this.nombre = nombre;
        this.variante = variante;
        this.dificultad = dificultad;
        this.parteCuerpo = parteCuerpo;
        this.descripcion = descripcion;
    }

    public Asana(int id, String nombre, String variante, String dificultad, String parteCuerpo, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.variante = variante;
        this.dificultad = dificultad;
        this.parteCuerpo = parteCuerpo;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getVariante() {
        return variante;
    }

    public String getDificultad() {
        return dificultad;
    }

    public String getParteCuerpo() {
        return parteCuerpo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setVariante(String variante) {
        this.variante = variante;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public void setParteCuerpo(String parteCuerpo) {
        this.parteCuerpo = parteCuerpo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
