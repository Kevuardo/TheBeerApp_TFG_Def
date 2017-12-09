package com.kcastilloe.thebeerapp_def;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
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
import com.miguelcatalan.materialsearchview.SearchAdapter;

import java.util.ArrayList;

import static android.speech.SpeechRecognizer.createSpeechRecognizer;
import static android.speech.SpeechRecognizer.isRecognitionAvailable;

/**
 * La actividad usada como inicio de la app para usuarios registrados
 * para ver la lista cervezas favoritas guardadas por el usuario.
 *
 * @author Kevin Castillo Escudero
 */


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";

    /* Elementos de vista. */
    private ListView lvListaCervezasFavoritas; /* Lista de cervezas favoritas del usuario. */
    private TextView tvNickUsuarioMenuLateral, tvEmailUsuarioMenuLateral;
    private LinearLayout llListaVacia;
    private Toolbar tbBarraSuperiorHome;
    private DrawerLayout dlMenuLateral;
    private NavigationView nvMenuLateral;
    private MaterialSearchView msvBusqueda; /* Librería importada. */

    /* Elementos con modelo hecho por mí. */
    private ListaPersonalizada adaptadorLista; /* El adaptador personalizado para la lista. */
    private Cerveza cervezaFavorita;
    private ArrayList<Cerveza> alCervezasFavoritas = new ArrayList();
    private Cerveza cervezaAlmacenada;
    private ArrayList<Cerveza> alCervezasAlmacenadasBdd = new ArrayList();
    private ArrayList<Cerveza> alBusquedasRecientes = new ArrayList();
    private String[] arrayNombresCervezas; /* Para mostrar los nombres en la búsqueda. Se instancia
        vacío en un principio, y se poblará según se llame a la BDD. */
    private String[] arrayBusquedasRecientes; /* Para mostrar las búsquedas recientes hechas por el
        usuario. Se instancia vacío en un principio, y se poblará según se llame a la BDD. */
    private String[] sinBusquedasRecientes = {"No hay búsquedas recientes"}; /* Mostrará un mensaje
        por defecto al usuario en lugar de las búsquedas recientes, ya que no habrá hecho ninguna. */

    private int idItemLista = 0; /* El id del item sobre el que se abre el menú contextual. */
    private boolean teclaBackPulsada = false; /* Variable bandera para controlar la salida de la app. */

    /* Elementos propios de Android Studio. */
    private Intent intentCambio;
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

        dlMenuLateral = (DrawerLayout) findViewById(R.id.dlMenuLateral);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, dlMenuLateral, tbBarraSuperiorHome, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dlMenuLateral.addDrawerListener(toggle);
        toggle.syncState();

        nvMenuLateral = (NavigationView) findViewById(R.id.nvMenuLateral);
        nvMenuLateral.setNavigationItemSelectedListener(this);

        /* Código propio. */
        /* Sistema de búsqueda de cervezas de la BDD. MaterialSearchView es un elemento perteneciente
        a una librería importada. Es un elemento de búsqueda que facilita mucho a la hora de trabajar
        con eventos, vistas, etc. */
        msvBusqueda = (MaterialSearchView) findViewById(R.id.msvBusqueda);
        /* Comprueba si el dispositivo soporta entrada de texto por reconocimiento de voz. */
        createSpeechRecognizer(getApplicationContext());
        if (isRecognitionAvailable(getApplicationContext())) {
            msvBusqueda.setVoiceSearch(true); /* Habilita la búsqueda por voz. */
            msvBusqueda.setVoiceIcon(getResources().getDrawable(R.drawable.ic_keyboard_voice_black_48dp));
        } else {
            msvBusqueda.setVoiceSearch(false); /* Deshabilita la búsqueda por voz. */
        }
        msvBusqueda.setEllipsize(true);

        /* Se le añade una lista a la búsqueda para las coincidencias retornadas. */
        bddFirebase = FirebaseDatabase.getInstance();
        recogerCervezasBdd(bddFirebase.getReference(ReferenciasFirebase.REFERENCIA_CERVEZAS));


        /* Lista de cervezas favoritas del usuario. */
        lvListaCervezasFavoritas = (ListView) findViewById(R.id.lvListaCervezasFavoritas);
        registerForContextMenu(lvListaCervezasFavoritas); /* Para añadir el menú contextual a los items de la lista. */
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
            /* Evalúa si la tecla "back" del teléfono ha sido accionada, y cierra la app si se
            produce una pulsación doble en un tiempo inferior a 2 segundos. */

            /* Si ya ha sido pulsada en los anteriores 2 segundos, cierra la app. */
            if (teclaBackPulsada) {
                super.onBackPressed();
                return;
            } else {
                /* Si no, cambia la variable bandera a true para que se pueda salir,
                * muestra al usuario un mensaje por pantalla, y activa un temporizador en segundo
                * plano de 2 segundos para accionar o desaccionar la salida de la app. */
                this.teclaBackPulsada = true;
                Toast.makeText(this, "Pulsa BACK de nuevo para salir", Toast.LENGTH_SHORT).show();
//                Snackbar.make(findViewById(R.id.rlContenedorHome), "Pulsa BACK de nuevo para salir", Snackbar.LENGTH_LONG).show();

                new Handler().postDelayed(new Runnable() {

                    /* De no cumplirse la condición de presionado de la tecla trasera en esos 2
                    * segundos, cambia el valor de la variable bandera a flase, de cara a un
                    * nuevo intento. */
                    @Override
                    public void run() {
                        teclaBackPulsada = false;
                    }

                }, 2000);
            }
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

        /* Referencia al nick del usuario, que es lo que se desea recuperar en este momento. */
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
                alCervezasFavoritas.clear(); /* Se vacía el Arraylist de cara a un nuevo proceso de rellenado de la lista. */
                /* El parámetro que recibe el método getValue() es la clase del tipo de objeto que se desea recoger,
                * y necesita un constructor vacío. */
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    cervezaFavorita = postSnapshot.getValue(Cerveza.class);
                    alCervezasFavoritas.add(cervezaFavorita);
                }
                rellenarLista(alCervezasFavoritas);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error de BD", databaseError.getMessage()+ "");
            }

        });

        /* Se recogen las búsquedas recientes en un ArrayList. */
        referenciaBdd = bddFirebase.getReference(ReferenciasFirebase.REFERENCIA_USUARIOS).child(usuarioActual.getUid()).child("busquedasRecientes");
        referenciaBdd.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                alBusquedasRecientes.clear(); /* Se vacía el Arraylist de cara a un nuevo proceso de rellenado de la lista. */
                /* El parámetro que recibe el método getValue() es la clase del tipo de objeto que se desea recoger,
                * y necesita un constructor vacío. */
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Cerveza cervezaReciente = postSnapshot.getValue(Cerveza.class);
                    alBusquedasRecientes.add(cervezaReciente);
                }

                /* Se declara el array de nombres, que tendrá una longitud igual al total de cervezas
                * en la BDD, y lo puebla con los nombres de las mismas. */
                arrayBusquedasRecientes = new String[alBusquedasRecientes.size()];

                for (int i = 0; i < alBusquedasRecientes.size(); i++) {
                    arrayBusquedasRecientes[i] = alBusquedasRecientes.get(i).getNombre();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error de BD", databaseError.getMessage()+ "");
            }

        });

        recogerCervezasBdd(bddFirebase.getReference(ReferenciasFirebase.REFERENCIA_CERVEZAS));

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
                    cervezaFavorita = alCervezasFavoritas.get(idItemLista);
                    intentCambio = new Intent(this, DetalleCervezaActivity.class);
                    intentCambio.putExtra("id", cervezaFavorita.getId());
                    startActivity(intentCambio);
                } catch (Exception e) {
                    Toast.makeText(this, "Se ha producido un error al tratar de acceder al detalle de la cerveza.", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_context_compartir:
                /* Comparte el contacto por el método que se seleccione posteriormente. */
                cervezaFavorita = alCervezasFavoritas.get(idItemLista);
                compartirCerveza(cervezaFavorita);
                return true;
            case R.id.action_context_ubicar:
                /* Muestra las distintas ubicaciones almacenadas en Firebase de la cerveza seleccionada. */
                cervezaFavorita = alCervezasFavoritas.get(idItemLista);
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
//                builderLogout.setTitle("Sesión de usuario actual");

                /* Botón de decisión afirmativo. */
                builderLogout.setPositiveButton("Cerrar sesión", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
     * @param usuarioActual El usuario autenticado en el dispositivo actualmente, del que se tomará
     *                      el correo electrónico.
     * @param nickUsuario El nick del usuario autenticado en el dispositivo actualmente.
     */
    private void modificarCampos(FirebaseUser usuarioActual, String nickUsuario) {

        String emailUsuario = usuarioActual.getEmail();

        tvNickUsuarioMenuLateral.setText(nickUsuario);
        tvEmailUsuarioMenuLateral.setText(emailUsuario);

    }

    /**
     * Recoge las cervezas almacenadas en la BDD, vuelca sus nombres en un array, lso selecciona como
     * sugerencias de búsqueda y abre el DetalleCervezaActivity correspondiente a la cerveza elegida.
     *
     * @param referenciaBdd La referencia al nodo "cervezas" de la BDD de Firebase.
     */
    private void recogerCervezasBdd(DatabaseReference referenciaBdd) {

        /* Se recogen las cervezas almacenadas en la BDD en un ArrayList para mostrarlas como
        sugerencias de búsqueda. */
        referenciaBdd.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                alCervezasAlmacenadasBdd.clear(); /* Se vacía el Arraylist de cara a un nuevo proceso de rellenado de la lista. */
                /* El parámetro que recibe el método getValue() es la clase del tipo de objeto que se desea recoger,
                * y necesita un constructor vacío. */
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    cervezaAlmacenada = postSnapshot.getValue(Cerveza.class);
                    alCervezasAlmacenadasBdd .add(cervezaAlmacenada);
                }

                /* Se declara el array de nombres, que tendrá una longitud igual al total de cervezas
                * en la BDD, y lo puebla con los nombres de las mismas. */
                arrayNombresCervezas = new String[alCervezasAlmacenadasBdd.size()];

                for (int i = 0; i < alCervezasAlmacenadasBdd.size(); i++) {
                    arrayNombresCervezas[i] = alCervezasAlmacenadasBdd.get(i).getNombre();
                }

                /* Se asigna el array de nombres como sugerencia del MaterialSearchView. */
                msvBusqueda.setSuggestions(arrayNombresCervezas);
                /* Por último, se trabajan los eventos para evalúar que ocurrirá con la búsqueda. */

                /* Evento activado al producirse una acción sobre el texto de búsqueda (modificación y envío). */
                msvBusqueda.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                /* Cuando se selecciona la opción de envío de búsqueda. */
                                Snackbar.make(findViewById(R.id.rlContenedorHome), "Selecciona una de las sugerencias", Snackbar.LENGTH_LONG).show();
                                return true; /* Si devuelve false, cierra el teclado; si devuelve true, lo deja abierto. */
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
//                                /* Cuando el texto cambia, evalúa si hay coincidencias. */
//                                /* Si el texto está vacío, cambia las sugerencias a las búsquedas
//                                recientes. Si no, carga todas las cervezas. */
//                                if (newText.trim().compareToIgnoreCase("") == 0) {
//                                    /* Comprueba si hay búsquedas  recientes. */
//                                    if (arrayBusquedasRecientes.length == 0) {
//                                        msvBusqueda.setSuggestions(sinBusquedasRecientes);
//                                        msvBusqueda.setAdapter(new SearchAdapter(msvBusqueda.getContext(), sinBusquedasRecientes));
//                                        msvBusqueda.showSuggestions();
//                                    } else {
//                                        msvBusqueda.setSuggestions(arrayBusquedasRecientes);
//                                        msvBusqueda.setAdapter(new SearchAdapter(msvBusqueda.getContext(), arrayBusquedasRecientes));
//                                        msvBusqueda.showSuggestions();
//                                    }
//                                } else {
//                                    msvBusqueda.setSuggestions(arrayNombresCervezas);
//                                    msvBusqueda.setAdapter(new SearchAdapter(msvBusqueda.getContext(), arrayNombresCervezas));
//                                    msvBusqueda.showSuggestions();
//                                }
                                return true;
                            }
                        });

                /* Evento activado al clicar en una sugerencia. */
                msvBusqueda.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String sugerenciaSeleccionada = (String) parent.getItemAtPosition(position);
                        for (int j = 0; j < alCervezasAlmacenadasBdd.size(); j++) {
                            /* Compara la sugerencia seleccionada con los nombres de las cervezas almacenadas para abrir sus detalles y no los de otra. */
                            if (sugerenciaSeleccionada.compareToIgnoreCase(alCervezasAlmacenadasBdd.get(j).getNombre()) == 0) {

                                /* Añade el elemento añadido a las búsquedas recientes del usuario y abre el detalle de la cerveza. */
                                DatabaseReference referenciaBusquedaBdd = bddFirebase.getReference(ReferenciasFirebase.REFERENCIA_USUARIOS);
                                referenciaBusquedaBdd.child(usuarioActual.getUid()).child("busquedasRecientes").child(alCervezasAlmacenadasBdd.get(j).getId()).setValue(alCervezasAlmacenadasBdd.get(j));
                                intentCambio = new Intent(HomeActivity.this, DetalleCervezaActivity.class);
                                intentCambio.putExtra("id", alCervezasAlmacenadasBdd.get(j).getId());
                                startActivity(intentCambio);
                                msvBusqueda.closeSearch();
                                break;

                            }
                        }

                    }
                });

                /* Evento activado al producirse una acción sobre el elemento MaterialSearchView (mostrarse
                y cerrarse). */
                msvBusqueda.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
                            @Override
                            public void onSearchViewShown() {

                            }

                            @Override
                            public void onSearchViewClosed() {

                            }
                        });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error de BD", databaseError.getMessage()+ "");
            }

        });

    }

    /**
     * Para rellenar la lista de cervezas favoritas cada vez que se inicia la actividad. Contacta con la BDD,
     * recoge los datos necesarios, crea objetos Cerveza para cada registro, y los muestra en los items de la lista.
     *
     * @param alCervezasFavoritas La colección de cervezas favoritas del usuario actual.
     */
    private void rellenarLista(final ArrayList<Cerveza> alCervezasFavoritas) {

        try {
            /* Evalúa si la lista está vacía. De estarlo, mostrará una imagen y un texto por defecto para comunicárselo al usuario. */
            if (alCervezasFavoritas.size() == 0) {
                /* Se fuerza el estado a visible de la vista, por si acaso estaba invisible previamente. */
                llListaVacia.setVisibility(View.VISIBLE);
                lvListaCervezasFavoritas.setEmptyView(llListaVacia);
            } else {
                /* Se fuerza el estado a invisible de la vista, ya que de otra manera se mezclarían
                en la vista la emptyView (imagen y texto) y los items de la lista. */
                llListaVacia.setVisibility(View.INVISIBLE);
                adaptadorLista = new ListaPersonalizada(this, R.layout.item_lista_layout, alCervezasFavoritas);
                lvListaCervezasFavoritas.setAdapter(adaptadorLista);

                /* El OnClickListener para los items del ListView. */
                lvListaCervezasFavoritas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    /* Llama al Intent para que cambie a la actividad que muestra el detalle de la cerveza. */
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            cervezaFavorita = alCervezasFavoritas.get(position);
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
