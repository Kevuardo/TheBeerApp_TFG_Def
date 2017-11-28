package com.kcastilloe.thebeerapp_def;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kcastilloe.thebeerapp_def.fragments_inicio.FragmentVentanaLogin;
import com.kcastilloe.thebeerapp_def.fragments_inicio.FragmentVentanaRegistro;
import com.kcastilloe.thebeerapp_def.modelo.ReferenciasFirebase;
import com.kcastilloe.thebeerapp_def.modelo.Usuario;

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
    private FirebaseAuth autenticacionFirebase; /* El controlador de autenticación de usuarios de Firebase. */
    private FirebaseUser usuarioActual; /* El modelo de usuario que se almacenará en Firebase. */
    private FirebaseDatabase bddFirebase;
    private DatabaseReference referenciaBdd;
    private Usuario nuevoUsuario;

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

        /* A continuación, crea nuevas instancias de Autenticación y BDD en Firebase. */
        autenticacionFirebase = FirebaseAuth.getInstance();
        bddFirebase = FirebaseDatabase.getInstance();
        referenciaBdd = bddFirebase.getReference(ReferenciasFirebase.REFERENCIA_USUARIOS);
        /* Si la referencia al nodo en la BDD no existe en la misma, creará dicho nodo con la referencia. */
    }

    @Override
    protected void onStart() {
        super.onStart();
        /* Comprueba si el usuario está actualmente activo (ya ha iniciado sesión). */
        usuarioActual = autenticacionFirebase.getCurrentUser();
        evaluarEstadoUsuario(usuarioActual);
    }

    /**
     * Crea un AdaptadorSeccionesInicio y le añade los Fragment.
     *
     * @param viewPager El manejador que permite la navegación entre las TabLayout de las secciones del inicio.
     */
    private void configurarViewPager(ViewPager viewPager) {
        adaptadorSecciones = new AdaptadorSeccionesInicio(getSupportFragmentManager());
        adaptadorSecciones.agregarFragment(new FragmentVentanaLogin(), "Login");
        adaptadorSecciones.agregarFragment(new FragmentVentanaRegistro(), "Registro");
        viewPager.setAdapter(adaptadorSecciones);
    }

    /**
     * Evalúa el estado de conexión con la app del usuario actual.
     *
     * @param usuarioActual El usuario actual a evaluar.
     */
    private void evaluarEstadoUsuario(FirebaseUser usuarioActual) {
        /* Con Firebase, no se cierra sesión aunque se cierre la app, a menos que se fuerce el estado de desconexión
        con autenticacionFirebase.signOut(). */
        if (usuarioActual != null) {
            /* Si ya figura como que ha iniciado sesión, entonces pasa directamente a la HomeActivity. */
            abrirInicio(); /* Abre el inicio. */
        } else {
            /* Si no, muestra un mensaje de información. */
            Toast.makeText(this, "Debes iniciar sesión para acceder a la app", Toast.LENGTH_SHORT).show();
        }
    }

    /**
    * Método usado para iniciar sesión en la app con las credenciales introducidas por el usuario.
    *
    * @param email El email del usuario que iniciará sesión en la app.
    * @param password La contraseña del usuario que iniciará sesión en la app.
    */
    public void iniciarSesionUsuario(String email, String password) {
        /* Una vez recibidos los parámetros, procede a comprobar las credenciales del usuario en la BDD de Firebase. */
        autenticacionFirebase.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                /* Al completarse la tarea, evalúa el estado de la misma. */
                if (task.isSuccessful()) {
                    /* El usuario inicia sesión con éxito, y accede a la HomeActivity. */
                    abrirInicio();
                } else {
                    /* El usuario no ha podido iniciar sesión correctamente. */
                    Toast.makeText(MainActivity.this, "Inicio de sesión fallido. Compruebe los campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
    * Registra un usuario nuevo en la app con los parámetros introducidos por el usuario.
    *
    * @param nick El nick del usuario que va a registrarse en la BDD.
    * @param email El e-mail del usuario que va a registrarse en la BDD.
    * @param edad La edad del usuario que va a registrarse en la BDD.
    * @param password La contraseña del usuario que va a registrarse en la BDD.
    */
    public void registrarUsuario(final String nick, final String email, final int edad, final String password) {
        /* Una vez recibidos los parámetros, procede a registrar al usuario en la BDD de Firebase. */
        autenticacionFirebase.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    /* El usuario se registra con éxito en la BDD, lo almacena con sus parámetros y fuerza su login. */
                    almacenarNuevoUsuario(nick, email, edad);
                    iniciarSesionUsuario(email, password);
                } else {
                    /* El usuario no ha podido registrase en la BDD correctamente. */
                    Toast.makeText(MainActivity.this, "Registro fallido. Compruebe los campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Almacena un nuevo usuario en la BDD referenciando su User ID para almacenar otros parámetros además de e-mail y password.
     *
     * @param nick El nick del usuario a almacenar.
     * @param email El email del usuario a almacenar.
     * @param edad El edad del usuario a almacenar.
     */
    private void almacenarNuevoUsuario(String nick, String email, int edad) {
        /* Crea un objeto Usuario para almacenarlo en la BDD. */
        nuevoUsuario = new Usuario(nick, email, edad);
        usuarioActual = autenticacionFirebase.getCurrentUser();
        /* Con child se puede seleccionar la clave que tendrá el nodo, y en este caso se desea que
        sea el UID (User ID) que Firebase proporciona automáticamente a cada usuario autenticado. */
        referenciaBdd.child(usuarioActual.getUid()).setValue(nuevoUsuario);
    }

    /**
     * Invoca el Intent a la siguiente Activity una vez que las operaciones de
     * inicio de sesión o registro han sido exitosas.
     */
    public void abrirInicio() {
        Intent intentCambio = new Intent(this, HomeActivity.class);
        startActivity(intentCambio);
        finish(); /* Hace que no se pueda navegar desde HomeActivity hasta MainActivity de nuevo. */
    }

//    Eliminar método público tras desarrollo. Ahora está así porque está siendo usado para acceder con
//    los botones de los fragment del TabLayout.

//    private void abrirInicio() {
//        Intent intentCambio = new Intent(this, HomeActivity.class);
//        startActivity(intentCambio);
//        finish(); /* Hace que no se pueda navegar desde HomeActivity hasta MainActivity de nuevo. */
//    }
}
