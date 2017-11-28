package com.kcastilloe.thebeerapp_def;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kcastilloe.thebeerapp_def.modelo.Cerveza;
import com.kcastilloe.thebeerapp_def.modelo.ReferenciasFirebase;

import java.util.ArrayList;

/**
 * La actividad usada como inicio de la app para usuarios registrados
 * para ver la lista cervezas favoritas guardadas por el usuario.
 *
 * @author Kevin Castillo Escudero
 */


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";

    private ListView lvListaCervezasFavoritas; /* Lista de cervezas favoritas del usuario. */
    private TextView tvListaVacia, tvNickUsuarioMenuLateral, tvEmailUsuarioMenuLateral;
    private LinearLayout llListaVacia;
    private Toolbar tbBarraSuperiorHome;
    private FloatingActionButton fabBuscarCervezas;
    private DrawerLayout dlMenuLateral;
    private NavigationView nvMenuLateral;

    private ListaPersonalizada adaptadorLista; /* El adaptador personalizado para la lista. */
    private Cerveza cervezaFavorita;
    private ArrayList<Cerveza> alCervezas = new ArrayList();
    private Intent intentCambio;
    //    private boolean listaVacia = false; /* Variable bandera para evaluar si la lista de favoritos está vacía. */
    private int idItemLista = 0; /* El id del item sobre el que se abre el menú contextual. */
    private FirebaseAuth autenticacionFirebase; /* El controlador de autenticación de usuarios de Firebase. */
    private FirebaseUser usuarioActual; /* El modelo de usuario que se almacenará en Firebase. */
    private FirebaseDatabase bddFirebase;
    private DatabaseReference referenciaBdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); /* Fuerza la posición a vertical. */

        /* Código autogenerado por Android Studio. */
        tbBarraSuperiorHome = (Toolbar) findViewById(R.id.tbBarraSuperiorHome);
        setSupportActionBar(tbBarraSuperiorHome);

        fabBuscarCervezas = (FloatingActionButton) findViewById(R.id.fabBuscarCervezas);
        fabBuscarCervezas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        dlMenuLateral = (DrawerLayout) findViewById(R.id.dlMenuLateral);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, dlMenuLateral, tbBarraSuperiorHome, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dlMenuLateral.addDrawerListener(toggle);
        toggle.syncState();

        nvMenuLateral = (NavigationView) findViewById(R.id.nvMenuLateral);
        nvMenuLateral.setNavigationItemSelectedListener(this);

        /* Código propio. */
        lvListaCervezasFavoritas = (ListView) findViewById(R.id.lvListaCervezasFavoritas);
        registerForContextMenu(lvListaCervezasFavoritas); /* Para añadir el menú contextual a los items de la lista. */
//        tvListaVacia = (TextView) findViewById(R.id.tvListaVacia) ;
        llListaVacia = (LinearLayout) findViewById(R.id.llListaVacia);

        /* Los campos de la cabecera del NavigationDrawer. */
        View navHeaderView = nvMenuLateral.getHeaderView(0); /* Se recoge la vista de la cabecera
            para trabajar con sus campos. De no hacerse así y tratar de acceder a los campos directamente
            dará un error de NullPointerException. */
        tvNickUsuarioMenuLateral = (TextView) navHeaderView.findViewById(R.id.tvNickUsuarioMenuLateral);
        tvEmailUsuarioMenuLateral = (TextView) navHeaderView.findViewById(R.id.tvEmailUsuarioMenuLateral);

        autenticacionFirebase = FirebaseAuth.getInstance();
        usuarioActual = autenticacionFirebase.getCurrentUser();

        bddFirebase = FirebaseDatabase.getInstance();
        referenciaBdd = bddFirebase.getReference(ReferenciasFirebase.REFERENCIA_USUARIOS).child(usuarioActual.getUid()).child("nick");

        /* Al cargar la Activity, modifica los datos según el usuario que esté logeado. */

        /* Ejecución dinámica de recogida de datos por cambio de los mismos. */
        referenciaBdd.addValueEventListener(new ValueEventListener() {

            /* Cuando cambien los datos. */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /* DataSnapshot es la colección de datos recogida de la BDD, y puede ser 1 solo valor o una lista de valores. */
                String nickUsuario = dataSnapshot.getValue(String.class);
                modificarCampos(usuarioActual, nickUsuario);
            }

            /* Cuando se cancele la referencia a la BDD por algún motivo, o se produzca un error en la BDD. */
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error en BDD: ", databaseError.getMessage());
            }

        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dlMenuLateral);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        rellenarLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_agregar_cerveza:

                /* Abre la NuevaCervezaActivity. */
                Intent intentCambio = new Intent(this, NuevaCervezaActivity.class);
                startActivity(intentCambio);

                return true;
            case R.id.action_info:
                AlertDialog.Builder builderInfo = new AlertDialog.Builder(this);
                builderInfo.setMessage("Creado por Kevin Castillo Escudero, 2017.\n\nContacto: kcastilloescudero@gmail.com");
                builderInfo.setTitle("Información de la app");
                builderInfo.setPositiveButton("Cerrar info", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialogInfo = builderInfo.create();
                dialogInfo.show();
                return true;
            case R.id.action_ajustes:

                AlertDialog.Builder builderAjustes = new AlertDialog.Builder(this);
                builderAjustes.setMessage("Has entrado en los ajustes de la app. Pero aquí no hay nah.");
                builderAjustes.setTitle("Ajustes de la app");
                builderAjustes.setPositiveButton("Cerrar ajustes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialogAjustes = builderAjustes.create();
                dialogAjustes.show();

                /* Dev Only - Cierra la sesión actual. */
                autenticacionFirebase.signOut();
                Toast.makeText(this, "Sesión cerrada.", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.action_extra:
                AlertDialog.Builder builderExtra = new AlertDialog.Builder(this);
                builderExtra.setMessage("Próximamente disponible, ¡no desesperes! ;)");
                builderExtra.setTitle("¡Pero si está incompleto!");
                builderExtra.setPositiveButton("Cerrar info", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialogExtra = builderExtra.create();
                dialogExtra.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextual_listview, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        idItemLista = info.position; /* Recoge el item de la lista en el que se ha llamado al menú contextual. */
        switch (item.getItemId()) {
            case R.id.action_context_consultar:
                /* Abre la DetalleContactoActivity con el id del contacto almacenado. */
                try {
                    cervezaFavorita = alCervezas.get(idItemLista);
                    intentCambio = new Intent(this, DetalleCervezaActivity.class);
                    intentCambio.putExtra("id", cervezaFavorita.getId());
                    startActivity(intentCambio);
                } catch (Exception e) {
                    Toast.makeText(this, "Se ha producido un error al tratar de acceder al detalle de la cerveza.", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_context_compartir:
                /* Comparte el contacto por el método que se seleccione posteriormente. */
                cervezaFavorita = alCervezas.get(idItemLista);
                compartirCerveza(cervezaFavorita);
                return true;
            case R.id.action_context_ubicar:
                /* Muestra las distintas ubicaciones almacenadas en Firebase de la cerveza seleccionada. */
                cervezaFavorita = alCervezas.get(idItemLista);
//                llamarContacto(contactoAlmacenado.getTelefono());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dlMenuLateral);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Recibe el usuario actual y modifica los campos de la vista de la actividad en función de sus
     * datos personales.
     *
     * @param usuarioActual El usuario autenticado en el dispositivo actualmente.
     *
     */
    private void modificarCampos(FirebaseUser usuarioActual, String nickUsuario) {

        String emailUsuario = usuarioActual.getEmail();

        tvNickUsuarioMenuLateral.setText(nickUsuario);
        tvEmailUsuarioMenuLateral.setText(emailUsuario);

    }

    /**
     * Para rellenar la lista de cervezas favoritas cada vez que se inicia la actividad. Contacta con la BDD,
     * recoge los datos necesarios, crea objetos Cerveza para cada registro, y los muestra en los items de la lista.
     */
    private void rellenarLista() {
        alCervezas.clear(); /* Se vacía el Arraylist de cara a un nuevo proceso de rellenado de la lista. */
        try {
            /* Se recogen las cervezas favoritas en un ArrayList. */
            /* Evalúa si la lista está vacía. De estarlo, mostrará una imagen y un texto por defecto para comunicárselo al usuario. */
            if (alCervezas.size() == 0) {
//                lvListaCervezasFavoritas.setEmptyView(tvListaVacia);
                lvListaCervezasFavoritas.setEmptyView(llListaVacia);
            } else {
                //alCervezas = gbd.listarContactos();
                adaptadorLista = new ListaPersonalizada(this, R.layout.item_lista_layout, alCervezas);
                lvListaCervezasFavoritas.setAdapter(adaptadorLista);

            /* El OnClickListener para los items del ListView. */
                lvListaCervezasFavoritas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    /* Llama al Intent para que cambie a la actividad que muestra el detalle del contacto. */
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            cervezaFavorita = alCervezas.get(position);
                            intentCambio = new Intent(view.getContext(), DetalleCervezaActivity.class);
                            intentCambio.putExtra("id", cervezaFavorita.getId());
                            startActivity(intentCambio);
                        } catch (Exception e) {
                            Toast.makeText(view.getContext(), "Se ha producido un error al tratar de acceder al detalle de la cerveza.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(this, "Se ha producido un error al listar los contactos.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Sirve para compartir la cerveza por cualquier medio de los que se ofrecen con el Chooser.
     *
     * @param cervezaAlmacenada La cerveza de la que se recogerán los datos a compartir.
     */
    private void compartirCerveza(Cerveza cervezaAlmacenada) {
        Intent intentCompartir = new Intent(Intent.ACTION_SEND);
        intentCompartir.setType("text/plain");
        intentCompartir.putExtra(Intent.EXTRA_TEXT, "¡Echa un vistazo a esta cerveza! " +
                "\n\nNombre: " + cervezaAlmacenada.getNombre() +
                ";\nGrados: " + cervezaAlmacenada.getGrados() +
                ";\nTipo: " + cervezaAlmacenada.getTipo() +
                ";\nPaís Origen: " + cervezaAlmacenada.getPaisOrigen());
        startActivity(Intent.createChooser(intentCompartir, "Compartir con..."));
    }

}
