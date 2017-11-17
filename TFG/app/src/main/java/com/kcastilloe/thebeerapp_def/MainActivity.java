package com.kcastilloe.thebeerapp_def;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.kcastilloe.thebeerapp_def.fragments_inicio.FragmentVentanaLogin;
import com.kcastilloe.thebeerapp_def.fragments_inicio.FragmentVentanaRegistro;

/**
 * La actividad de inicio de la app, en la que se muestran las opciones de iniciar sesión o
 * registrar un nuevo usuario.
 *
 * @author Kevin Castillo Escudero.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private AdaptadorSeccionesInicio adaptadorSecciones; /* El adaptador de las secciones del TabLayout. */
    private ViewPager vpContenedor; /* El formato de las secciones. */
    private TabLayout tlVentanas; /* El contenedor de las secciones. */
    private Intent intentCambio; /* El Intent para abrir la Home Activity. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); /* Fuerza la posición a vertical. */

        /* Tras crear la vista, añade las secciones del TabLayout. */
        adaptadorSecciones = new AdaptadorSeccionesInicio(getSupportFragmentManager());

        /* Configura el ViewPager con el AdaptadorSecciones. */
        vpContenedor = (ViewPager) findViewById(R.id.vpContenedor);
        configurarViewPager(vpContenedor);

        /* Después, añade el ViewPager al contenedor de ventanas del Layout (el TabLayout). */
        tlVentanas = (TabLayout) findViewById(R.id.tlVentanas);
        tlVentanas.setupWithViewPager(vpContenedor);
    }

    /* Crea un AdaptadorSeccionesInicio y le añade los Fragment.*/
    private void configurarViewPager(ViewPager viewPager) {
        AdaptadorSeccionesInicio adaptador = new AdaptadorSeccionesInicio(getSupportFragmentManager());
        adaptador.agregarFragment(new FragmentVentanaLogin(), "Login");
        adaptador.agregarFragment(new FragmentVentanaRegistro(), "Registro");
        viewPager.setAdapter(adaptador);
    }

    /**
     * Método usado para iniciar sesión en la app con las credenciales introducidas por el usuario.
     */
    public void iniciarSesionUsuario(String nickUsuario, String password) {
        Toast.makeText(this, "Iniciar sesión", Toast.LENGTH_SHORT).show();
        /* Una vez recibidos los parámetros, procede a comprobar las credenciales del
        usuario en la BDD de Firebase. */

        /* Una vez comprobado, abre la HomeActivity. */
        abrirInicio();
    }

    /**
     * Método usado para registrar un usuario nuevo en la app con los parámetros introducidos por el usuario.
     */
    public void registrarUsuario(String nickUsuario, int edad, String password) {
        Toast.makeText(this, "Registro", Toast.LENGTH_SHORT).show();
        /* Una vez recibidos los parámetros, procede a registrar al usuario en la BDD de Firebase. */

        /* Una vez registrado el usuario, hace su login. */
        iniciarSesionUsuario(nickUsuario, password);
    }

    /**
     * Método usado para invocar el Intent a la siguiente Activity una vez que las operaciones de
     * inicio de sesión o registro han sido exitosas.
     */
//    Eliminar método público tras desarrollo.

//    private void abrirInicio() {
//        Intent intentCambio = new Intent(this, HomeActivity.class);
//        startActivity(intentCambio);
//        finish(); /* Hace que no se pueda navegar desde HomeActivity hasta MainActivity de nuevo. */
//    }
    public void abrirInicio() {
        Intent intentCambio = new Intent(this, HomeActivity.class);
        startActivity(intentCambio);
        finish(); /* Hace que no se pueda navegar desde HomeActivity hasta MainActivity de nuevo. */
    }
}
