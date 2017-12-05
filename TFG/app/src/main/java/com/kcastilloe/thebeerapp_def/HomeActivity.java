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
import com.miguelcatalan.materialsearchview.MaterialSearchView;

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

    /* Para el SearchView. */
    private MaterialSearchView msvBusqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); /* Fuerza la posición a vertical. */

        /* Código autogenerado por Android Studio. */
        tbBarraSuperiorHome = (Toolbar) findViewById(R.id.tbBarraSuperiorHome);
        setSupportActionBar(tbBarraSuperiorHome);

//        fabBuscarCervezas = (FloatingActionButton) findViewById(R.id.fabBuscarCervezas);
//        fabBuscarCervezas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        dlMenuLateral = (DrawerLayout) findViewById(R.id.dlMenuLateral);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, dlMenuLateral, tbBarraSuperiorHome, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dlMenuLateral.addDrawerListener(toggle);
        toggle.syncState();

        nvMenuLateral = (NavigationView) findViewById(R.id.nvMenuLateral);
        nvMenuLateral.setNavigationItemSelectedListener(this);

        /* Código propio. */
        /* MaterialSearchView es un elemento perteneciente a una librería importada. Es un elemento
        de búsqueda que facilita mucho a la hora de trabajar con eventos, vistas, etc. */
        msvBusqueda = (MaterialSearchView) findViewById(R.id.msvBusqueda);
        msvBusqueda.setVoiceSearch(true); /* Habilita la búsqueda por voz. */

        /* Se le añade una lista a la búsqueda para las coincidencias retornadas. */


        /* Por último, se trabajan los eventos para evalúar que ocurrirá con la búsqueda. */
        msvBusqueda.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /* Cuando se selecciona la opción de envío de búsqueda. */
                Snackbar.make(findViewById(R.id.rlContenedorHome), "Selecciona una de las sugerencias", Snackbar.LENGTH_LONG).show();
//                Toast.makeText(HomeActivity.this, "Selecciona una de las sugerencias", Toast.LENGTH_SHORT).show();
                return true; /* Si devuelve false, cierra el teclado; si devuelve true, lo deja abierto. */
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /* Cuando el texto cambia, evalúa si hay coincidencias. */
                
                return false;
            }
        });

        msvBusqueda.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        lvListaCervezasFavoritas = (ListView) findViewById(R.id.lvListaCervezasFavoritas);
        registerForContextMenu(lvListaCervezasFavoritas); /* Para añadir el menú contextual a los items de la lista. */
//        tvListaVacia = (TextView) findViewById(R.id.tvListaVacia) ;
        llListaVacia = (LinearLayout) findViewById(R.id.llListaVacia);

        /* Los campos de la cabecera del NavigationDrawer. */
        View navHeaderView = nvMenuLateral.getHeaderView(0); /* Se recoge la vista de la cabecera
            para trabajar con sus campos. De no hacerse así y tratar de acceder a los campos directamente,
            dará un error de NullPointerException. */
        tvNickUsuarioMenuLateral = (TextView) navHeaderView.findViewById(R.id.tvNickUsuarioMenuLateral);
        tvEmailUsuarioMenuLateral = (TextView) navHeaderView.findViewById(R.id.tvEmailUsuarioMenuLateral);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout dlMenuLateral = (DrawerLayout) findViewById(R.id.dlMenuLateral);
        if (dlMenuLateral.isDrawerOpen(GravityCompat.START)) {
            dlMenuLateral.closeDrawer(GravityCompat.START);
        } else if (msvBusqueda.isSearchOpen()) {
            msvBusqueda.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /* Se debe hacer la instanciación de FirebaseAuth y FirebaseDatabase en onResume() ya que de
        * hacerlo en onCreate() sólo funcionaría la primera vez, y cada que vez que se reanudará la
        * HomeActivity tras ser creada habría una inconsistencia de referencias en el código al
        * tratar de acceder a valores nulos. */
        autenticacionFirebase = FirebaseAuth.getInstance();
        usuarioActual = autenticacionFirebase.getCurrentUser();

        bddFirebase = FirebaseDatabase.getInstance();
        /* Referencia al nick del usuario, que es lo que se desea recuperar en este moemento. */
        referenciaBdd = bddFirebase.getReference(ReferenciasFirebase.REFERENCIA_USUARIOS).child(usuarioActual.getUid()).child("nick");

        /* Se actualizan los datos de los campos de la vista cada vez que HomeActivity se reanuda (creación incluida). */
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

        /* Se recogen las cervezas favoritas en un ArrayList. */
        referenciaBdd = bddFirebase.getReference(ReferenciasFirebase.REFERENCIA_USUARIOS).child(usuarioActual.getUid()).child("favoritas");
        referenciaBdd.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                alCervezas.clear(); /* Se vacía el Arraylist de cara a un nuevo proceso de rellenado de la lista. */
                /* El parámetro que recibe el método getValue() es la clase del tipo de objeto que se desea recoger,
                * y necesita un constructor vacío. */
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    cervezaFavorita = postSnapshot.getValue(Cerveza.class);
                    alCervezas.add(cervezaFavorita);
                    Log.i("Cerveza favorita", cervezaFavorita.getNombre() + "");
                }
                rellenarLista(alCervezas);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error de BD", databaseError.getMessage()+ "");
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        /* Asigna el item de búsqueda del menú del Toolbar a la MaterialSearchView. */
        msvBusqueda.setMenuItem(menu.findItem(R.id.action_buscar));
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
            case R.id.action_buscar:
                AlertDialog.Builder builderBusqueda = new AlertDialog.Builder(this);
                builderBusqueda.setMessage("Aquí va la búsqueda");
                builderBusqueda.setTitle("Búsqueda de la app");
                builderBusqueda.setPositiveButton("Cerrar búsqueda", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialogBusqueda = builderBusqueda.create();
                dialogBusqueda.show();
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
                /* Abre la DetalleCervezaActivity con el id en Firebase de la cerveza seleciconada. */
                try {
                    cervezaFavorita = alCervezas.get(idItemLista);
                    intentCambio = new Intent(this, DetalleCervezaActivity.class);
//                    intentCambio.putExtra("id", cervezaFavorita.getId());
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dlMenuLateral);

        switch (item.getItemId()) {
            case R.id.nav_cuenta:

                AlertDialog.Builder builderCuenta = new AlertDialog.Builder(this);
                builderCuenta.setMessage("Has entrado en los ajustes de la app. Pero aquí no hay nah.");
                builderCuenta.setTitle("Ajustes de la app");
                builderCuenta.setPositiveButton("Cerrar ajustes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialogCuenta = builderCuenta.create();
                dialogCuenta.show();
                drawer.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_logout:

                AlertDialog.Builder builderLogout = new AlertDialog.Builder(this);
                builderLogout.setMessage("¿Deseas cerrar la sesión de usuario actual?");
                builderLogout.setTitle("Sesión de usuario actual");

                /* Botón de decisión afirmativo. */
                builderLogout.setPositiveButton("Cerrar sesión", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /* Dev Only - Cierra la sesión actual. */
                        autenticacionFirebase.signOut();
                        Toast.makeText(HomeActivity.this, "Sesión cerrada.", Toast.LENGTH_SHORT).show();
                        intentCambio = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(intentCambio);
                        finish(); /* Hace que no se pueda navegar desde HomeActivity hasta MainActivity de nuevo. */
                    }
                });

                /* Botón de decisión negativa. */
                builderLogout.setNegativeButton("No cerrar sesión", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialogLogout = builderLogout.create();
                dialogLogout.show();
                drawer.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_info:

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
                drawer.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_reportar:
                AlertDialog.Builder builderReportar = new AlertDialog.Builder(this);
                builderReportar.setMessage("No hay nada que reportar, granujilla ;)");
                builderReportar.setTitle("Reportar incidencias");
                builderReportar.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialogReportar = builderReportar.create();
                dialogReportar.show();
                drawer.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_sugerencias:

                AlertDialog.Builder builderSugerencias = new AlertDialog.Builder(this);
                builderSugerencias.setMessage("Todas las sugerencias son bienvenidas, pero no obligatoriamente tomadas en cuenta.");
                builderSugerencias.setTitle("Sugerencias de desarrollo");
                builderSugerencias.setPositiveButton("Cerrar ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialogSugerencias = builderSugerencias.create();
                dialogSugerencias.show();
                drawer.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_extra:

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
                drawer.closeDrawer(GravityCompat.START);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

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
     *
     * @param alCervezas La colección de cervezas favoritas del usuario actual.
     */
    private void rellenarLista(final ArrayList<Cerveza> alCervezas) {

        try {
            /* Evalúa si la lista está vacía. De estarlo, mostrará una imagen y un texto por defecto para comunicárselo al usuario. */
            if (alCervezas.size() == 0) {
//                lvListaCervezasFavoritas.setEmptyView(tvListaVacia);
                /* Se fuerza el estado a visible de la vista, por si acaso estaba invisible previamente. */
                llListaVacia.setVisibility(View.VISIBLE);
                lvListaCervezasFavoritas.setEmptyView(llListaVacia);
            } else {
                /* Se fuerza el estado a invisible de la vista, ya que de otra manera se mezclarían
                en la vista la emptyView (imagen y texto) y los items de la lista. */
                llListaVacia.setVisibility(View.INVISIBLE);
                adaptadorLista = new ListaPersonalizada(this, R.layout.item_lista_layout, alCervezas);
                lvListaCervezasFavoritas.setAdapter(adaptadorLista);

                /* El OnClickListener para los items del ListView. */
                lvListaCervezasFavoritas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    /* Llama al Intent para que cambie a la actividad que muestra el detalle de la cerveza. */
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
            Toast.makeText(this, "Se ha producido un error al listar los favoritos.", Toast.LENGTH_LONG).show();
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
