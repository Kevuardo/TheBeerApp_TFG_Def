package com.kcastilloe.thebeerapp_def.modelo;

/**
 * Clase modelo dedicada a las cervezas que se almacenarán y recogerán en la BDD y que el
 * usuario verá en la app.
 *
 * @author Kevin Castillo Escudero
 */

public class Cerveza {

    private String nombre = null;
    private float grados = 0f;
    private String tipo = null;
    private String paisOrigen = null;
    private String descripcion = null;
    private String id = null;
    /* ¡Falta añadir un ArrayList de ubicaciones de cada cerveza! */
    private byte[] foto;

    /**
     * Constructor vacío, para usar sólo su modelo de clase en la recogida de favoritos del usuario.
     */
    public Cerveza() {
    }

    /**
     * Constructor sin id para la inserción en la BDD. (El id se modificará después de insertarse).
     *
     * @param nombre El nombre de la cerveza.
     * @param grados Los grados de la cerveza.
     * @param tipo El tipo de la cerveza.
     * @param paisOrigen El país de origen de la cerveza.
     * @param descripcion La descripción comercial de la cerveza.
     * @param foto La foto de la cerveza.
     */
    public Cerveza(String nombre, float grados, String tipo, String paisOrigen, String descripcion, byte[] foto) {
        this.nombre = nombre;
        this.grados = grados;
        this.tipo = tipo;
        this.paisOrigen = paisOrigen;
        this.descripcion = descripcion;
        this.foto = foto;
    }

    /**
     * Constructor con ID para la recogida de datos de la BDD.
     *
     * @param nombre El nombre de la cerveza.
     * @param grados Los grados de la cerveza.
     * @param tipo El tipo de la cerveza.
     * @param paisOrigen El país de origen de la cerveza.
     * @param descripcion La descripción comercial de la cerveza.
     * @param foto La foto de la cerveza.
     * @param id El id de la cerveza almacenada en la BDD.
     */
    public Cerveza(String nombre, float grados, String tipo, String paisOrigen, String descripcion, String id, byte[] foto) {
        this.nombre = nombre;
        this.grados = grados;
        this.tipo = tipo;
        this.paisOrigen = paisOrigen;
        this.descripcion = descripcion;
        this.id = id;
        this.foto = foto;
    }

    /* Sólo testeo - constructor de cerveza sin imagen. */
    public Cerveza(String nombre, float grados, String tipo, String paisOrigen) {
        this.nombre = nombre;
        this.grados = grados;
        this.tipo = tipo;
        this.paisOrigen = paisOrigen;
    }

    /* Getters y Setters. */
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}
