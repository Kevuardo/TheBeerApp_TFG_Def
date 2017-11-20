package com.kcastilloe.thebeerapp_def.modelo;

/**
* Clase modelo dedicada a los usuarios que se darán de alta en la app.
*
* @author Kevin Castillo Escudero
*/

public class Usuario {

    String nick = null;
    String email = null;
    int edad = 0;

    /* Constructor vacío. */
    public Usuario() {
    }

    /* COnstructor para almacenamiento en la BDD de Firebase. */
    public Usuario(String nick, String email, int edad) {
        this.nick = nick;
        this.email = email;
        this.edad = edad;
    }

    /* Getters y Setters. */
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}
