package com.kcastilloe.thebeerapp_def.modelo;

/**
 * Clase modelo dedicada a las ubicaciones de las cervezas.
 *
 * @author Kevin Castillo Escudero
 */

public class UbicacionCerveza {

    private String titulo = null;
    private Float latitud = 0f;
    private Float longitud = 0f;
    private String id = null;

    /**
     * Constructor vacío, para usar sólo su modelo de clase en la recogida de ubicaciones de las
     * cervezas.
     */
    public UbicacionCerveza() {
    }

    /**
     * Constructor sin id para la inserción en BDD.
     *
     * @param titulo El título de la ubicación
     * @param latitud La latitud de la ubicación.
     * @param longitud La longitud de la ubicación.
     */
    public UbicacionCerveza(String titulo, Float latitud, Float longitud) {
        this.titulo = titulo;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    /**
     * Constructor con id para la recogida de la BDD.
     *
     * @param titulo El título de la ubicación
     * @param latitud La latitud de la ubicación.
     * @param longitud La longitud de la ubicación.
     * @param id El id de la ubicación en la BDD.
     */
    public UbicacionCerveza(String titulo, Float latitud, Float longitud, String id) {
        this.titulo = titulo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.id = id;
    }

    /* Getters y Setters. */
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Float getLatitud() {
        return latitud;
    }

    public void setLatitud(Float latitud) {
        this.latitud = latitud;
    }

    public Float getLongitud() {
        return longitud;
    }

    public void setLongitud(Float longitud) {
        this.longitud = longitud;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
