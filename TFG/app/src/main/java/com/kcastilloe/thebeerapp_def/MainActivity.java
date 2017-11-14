package com.kcastilloe.thebeerapp_def;

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
    private ViewPager vpContenedor; /* EL formato de las secciones. */
    private TabLayout tlVentanas; /* El contenedor de las secciones. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: Iniciando MainActivity.");

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
    public void iniciarSesionUsuario() {
        Toast.makeText(this, "Iniciar sesión", Toast.LENGTH_SHORT).show();
    }


    /**
     * Método usado para registrar un usuario nuevo en la app con los parámetros introducidos por el usuario.
     */
    public void registrarUsuario(String nombre, String nickUsuario, int edad, String password) {
        Toast.makeText(this, "Registro", Toast.LENGTH_SHORT).show();


    }

    /**
     * Método usado para invocar el Intent a la siguiente Activity una vez que las operaciones de
     * inicio de sesión o registro han sido exitosas.
     */
    private void abrirInicio() {

    }
}
