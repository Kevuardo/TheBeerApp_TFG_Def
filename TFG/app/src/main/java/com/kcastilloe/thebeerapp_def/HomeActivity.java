package com.kcastilloe.thebeerapp_def;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.kcastilloe.thebeerapp_def.modelo.Cerveza;

import java.util.ArrayList;

/**
 * La actividad usada como inicio de la app para usuarios registrados
 * para ver la lista cervezas favoritas guardadas por el usuario.
 *
 * @author Kevin Castillo Escudero
 */

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private ListView lvListaCervezasFavoritas; /* Lista de cervezas favoritas del usuario. */
    private TextView tvListaVacia;
    private LinearLayout llListaVacia;
    private ListaPersonalizada adaptadorLista; /* El adaptador personalizado para la lista. */
    private Cerveza cervezaFavorita;
    private ArrayList<Cerveza> alCervezas = new ArrayList();
    private Intent intentCambio;
//    private boolean listaVacia = false; /* Variable bandera para evaluar si la lista de favoritos está vacía. */
    private int idItemLista = 0; /* El id del item sobre el que se abre el menú contextual. */
    private FirebaseAuth autenticacionFirebase; /* El controlador de autenticación de usuarios de Firebase. */
    private FirebaseUser usuarioActual; /* El modelo de usuario que se almacenará en Firebase. */

    private DrawerLayout dlMenuLateral;
    private NavigationView nvMenuLateral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); /* Fuerza la posición a vertical. */

        lvListaCervezasFavoritas = (ListView) findViewById(R.id.lvListaCervezasFavoritas);
        registerForContextMenu(lvListaCervezasFavoritas); /* Para añadir el menú contextual. */
//        tvListaVacia = (TextView) findViewById(R.id.tvListaVacia) ;
        llListaVacia = (LinearLayout) findViewById(R.id.llListaVacia);

        autenticacionFirebase = FirebaseAuth.getInstance();
        usuarioActual = autenticacionFirebase.getCurrentUser();

        /* Los elementos del Navigation Drawer (menú lateral) y su configuración lógica. */

        /* La configuración de los items que forman el NavigationDrawer. */
        nvMenuLateral.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_seccion_1:
                        Toast.makeText(HomeActivity.this, "Menú sección 1 pulsado", Toast.LENGTH_SHORT).show();
                        item.setChecked(true);
                        getSupportActionBar().setTitle(item.getTitle());
                        dlMenuLateral.closeDrawers();
                        break;
                    case R.id.menu_seccion_2:
                        Toast.makeText(HomeActivity.this, "Menú sección 2 pulsado", Toast.LENGTH_SHORT).show();
                        item.setChecked(true);
                        getSupportActionBar().setTitle(item.getTitle());
                        dlMenuLateral.closeDrawers();
                        break;
                    case R.id.menu_seccion_3:
                        Toast.makeText(HomeActivity.this, "Menú sección 3 pulsado", Toast.LENGTH_SHORT).show();
                        item.setChecked(true);
                        getSupportActionBar().setTitle(item.getTitle());
                        dlMenuLateral.closeDrawers();
                        break;
                    case R.id.menu_opcion_1:
                        Toast.makeText(HomeActivity.this, "Menú opción 1 pulsado", Toast.LENGTH_SHORT).show();
                        dlMenuLateral.closeDrawers();
                        break;
                    case R.id.menu_opcion_2:
                        Toast.makeText(HomeActivity.this, "Menú opción 2 pulsado", Toast.LENGTH_SHORT).show();
                        dlMenuLateral.closeDrawers();
                        break;
                    case R.id.menu_opcion_3:
                        Toast.makeText(HomeActivity.this, "Menú opción 3 pulsado", Toast.LENGTH_SHORT).show();
                        dlMenuLateral.closeDrawers();
                        break;
                    default:
                        Toast.makeText(HomeActivity.this, "Algo no ha funcionado como debería con el ND.", Toast.LENGTH_SHORT).show();
                        dlMenuLateral.closeDrawers();
                        break;
                }

                return true;

            }
        });

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
