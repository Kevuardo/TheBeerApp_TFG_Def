package com.kcastilloe.thebeerapp_def.modelo;

/**
 * Clase modelo dedicada a las cervezas que se almacenarán y recogerán en la BD y que el
 * usuario verá en la app.
 *
 * @author Kevin Castillo Escudero
 */

public class Cerveza {

    private int id = 0;
    private String nombre = null;
    private float grados = 0f;
    private String tipo = null;
    private String paisOrigen = null;
    private byte[] foto;

    /**
     * Constructor sin id (para la inserción en la BD).
     *
     * @param nombre El nombre de la cerveza.
     * @param grados Los grados de la cerveza.
     * @param tipo El tipo de la cerveza.
     * @param paisOrigen El país de origen de la cerveza.
     * @param foto La foto de la cerveza.
     */
    public Cerveza(String nombre, float grados, String tipo, String paisOrigen, byte[] foto) {
        this.nombre = nombre;
        this.grados = grados;
        this.tipo = tipo;
        this.paisOrigen = paisOrigen;
        this.foto = foto;
    }

    /**
     * Constructor con id (para la recuperación de datos en la BD y posterior muestra en el ListView).
     *
     * @param id El id de la cerveza.
     * @param nombre El nombre de la cerveza.
     * @param grados Los grados de la cerveza.
     * @param tipo El tipo de la cerveza.
     * @param paisOrigen El país de origen de la cerveza.
     * @param foto La foto de la cerveza.
     */
    public Cerveza(int id, String nombre, float grados, String tipo, String paisOrigen, byte[] foto) {
        this.id = id;
        this.nombre = nombre;
        this.grados = grados;
        this.tipo = tipo;
        this.paisOrigen = paisOrigen;
        this.foto = foto;
    }

    /* Getters y Setters. */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getGrados() {
        return grados;
    }

    public void setGrados(float grados) {
        this.grados = grados;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}
