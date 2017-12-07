package com.kcastilloe.thebeerapp_def;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kcastilloe.thebeerapp_def.modelo.Cerveza;
import com.kcastilloe.thebeerapp_def.modelo.ReferenciasFirebase;
import com.kcastilloe.thebeerapp_def.modelo.Usuario;

import java.util.ArrayList;

/**
 * La actividad usada para ver los detalles de la cerveza seleccionada.
 *
 * @author Kevin Castillo Escudero.
 */

public class DetalleCervezaActivity extends AppCompatActivity {

    private static final String BORRAR_FAVORITO = "BORRAR FAVORITO";
    private static final String AGREGAR_FAVORITO = "AGREGAR FAVORITO";


    private TextView tvNombreCervezaDetalle, tvGradacionCervezaDetalle, tvTipoCervezaDetalle,
            tvPaisOrigenCervezaDetalle, tvDescripcionCervezaDetalle;
    private Button btnFavorito;

    private Intent intentCambio;

    private FirebaseAuth autenticacionFirebase;
    private FirebaseUser usuarioActual;
    private FirebaseDatabase bddFirebase;
    private DatabaseReference referenciaBdd;

    private Cerveza cervezaAlmacenada; /* La cerveza almacenada en la BDD. */
    private ArrayList<Cerveza> alCervezasFavoritasUsuario = new ArrayList();
    private Usuario usuarioAlmacenado; /* El usuario almacenado en la BDD. */

    private String idCerveza; /* La cadena identificadora de la cerveza en la BDD. */
    private boolean esFavorita = false; /* Variable bandera. Si ya es favorita, la borra. Si no lo es, la añade. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cerveza);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); /* Fuerza la posición a vertical. */


        /* Recoge la cerveza almacenada correspondiente al Token (el identificador) en la BDD. */
        tvNombreCervezaDetalle = (TextView) findViewById(R.id.tvNombreCervezaDetalle);
        tvGradacionCervezaDetalle = (TextView) findViewById(R.id.tvGradacionCervezaDetalle);
        tvTipoCervezaDetalle = (TextView) findViewById(R.id.tvTipoCervezaDetalle);
        tvPaisOrigenCervezaDetalle = (TextView) findViewById(R.id.tvPaisOrigenCervezaDetalle);
        tvDescripcionCervezaDetalle = (TextView) findViewById(R.id.tvDescripcionCervezaDetalle);
        btnFavorito = (Button) findViewById(R.id.btnFavorito);

        /* Recoge el ID que le envía el Intent. */
        Intent intentApertura = getIntent();
        String idCerveza = intentApertura.getStringExtra("id");

        /* Evalúa si el usuario ya ha marcado esa cerveza como favorita; de ser así, cambiará su texto
        * y estilo. Para ello, evalúa si el ID de la cerveza se corresponde con alguno de los favoritos
        * del usuario. */
        evaluarFavorito(idCerveza);

        btnFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (esFavorita) {
                    guardarFavorita(false);
                } else {
                    guardarFavorita(true);
                }

            }
        });

        /* Recoger una cerveza de la BD, volcar datos en la vista y luego agregar a favoritos. */
        bddFirebase = FirebaseDatabase.getInstance();
        /* La clave en child() será igual a la clave ID de la cerveza que recibe en el Intent. */
        referenciaBdd = bddFirebase.getReference(ReferenciasFirebase.REFERENCIA_CERVEZAS).child(idCerveza);
        autenticacionFirebase = FirebaseAuth.getInstance();
        usuarioActual = autenticacionFirebase.getCurrentUser();

    }

    @Override
    protected void onResume() {

        super.onResume();
        /* Se actualizan los datos de los campos de la vista cada vez que DetalleCervezaActivity se reanuda (creación incluida). */
        /* Ejecución dinámica de recogida de datos por cambio de los mismos. */
        referenciaBdd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /* El parámetro que recibe el método getValue() es la clase del tipo de objeto que se desea recoger,
                * y necesita un constructor vacío. */
                cervezaAlmacenada = dataSnapshot.getValue(Cerveza.class);
                modificarCampos(cervezaAlmacenada);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error de BD", databaseError.getMessage()+ "");
            }
        });

    }

    /**
     * Modifica los campos de la vista de detalle con los valores propios de la cerveza seleccioanda.
     *
     * @param cervezaAlmacenada La cerveza almacenada en la BDD que se ha referenciado al entrar en la Activity.
     */
    private void modificarCampos(Cerveza cervezaAlmacenada) {

        tvNombreCervezaDetalle.setText(cervezaAlmacenada.getNombre());
        tvGradacionCervezaDetalle.setText("Grados: " + Float.toString(cervezaAlmacenada.getGrados()) + "%");
        tvTipoCervezaDetalle.setText("Tipo: " + cervezaAlmacenada.getTipo());
        tvPaisOrigenCervezaDetalle.setText("País de origen: " + cervezaAlmacenada.getPaisOrigen());

    }

    private void evaluarFavorito(final String idCerveza) {


        autenticacionFirebase = FirebaseAuth.getInstance();
        usuarioActual = autenticacionFirebase.getCurrentUser();

        bddFirebase = FirebaseDatabase.getInstance();
        referenciaBdd = bddFirebase.getReference(ReferenciasFirebase.REFERENCIA_USUARIOS).child(usuarioActual.getUid()).child("favoritas");

        referenciaBdd.addValueEventListener(new ValueEventListener() {

            /* Cuando cambien los datos. */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                alCervezasFavoritasUsuario.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Cerveza cervezaRecogida = postSnapshot.getValue(Cerveza.class);
                    alCervezasFavoritasUsuario.add(cervezaRecogida);
                    Log.i("Cerveza favorita", cervezaRecogida.getNombre() + "");
                }

                for (int i = 0; i < alCervezasFavoritasUsuario.size(); i++) {
                    if (idCerveza.compareToIgnoreCase(alCervezasFavoritasUsuario.get(i).getId()) == 0) {
                        esFavorita = true;
                        btnFavorito.setBackgroundColor(Color.parseColor("#000000"));
                        btnFavorito.setTextColor(Color.parseColor("#FFFFFF"));
                        btnFavorito.setText(BORRAR_FAVORITO);
                        break;
                    } else {
                        esFavorita = false;
                        btnFavorito.setBackgroundColor(Color.parseColor("#b28223"));
                        btnFavorito.setTextColor(Color.parseColor("#FFFFFF"));
                        btnFavorito.setText(AGREGAR_FAVORITO);
                    }
                }
            }

            /* Cuando se cancele la referencia a la BDD por algún motivo, o se produzca un error en la BDD. */
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error en BDD: ", databaseError.getMessage());
            }

        });

    }

    private void guardarFavorita(boolean guardable) {

        usuarioActual = autenticacionFirebase.getCurrentUser();
        referenciaBdd = bddFirebase.getReference(ReferenciasFirebase.REFERENCIA_USUARIOS);

        /* Con child se puede seleccionar la clave que tendrá el nodo, y en este caso se desea que
        sea el UID (User ID) que Firebase proporciona automáticamente a cada usuario autenticado. */
        if (guardable) {
            referenciaBdd.child(usuarioActual.getUid()).child("favoritas").child(cervezaAlmacenada.getId()).setValue(cervezaAlmacenada);
            btnFavorito.setText(BORRAR_FAVORITO);
            btnFavorito.setBackgroundColor(Color.parseColor("#000000"));
            btnFavorito.setTextColor(Color.parseColor("#FFFFFF"));
            Snackbar.make(findViewById(R.id.clContenedorDetalle), "Cerveza añadida a favoritos", Snackbar.LENGTH_SHORT).show();

        } else {
            AlertDialog.Builder builderBorrarFavorito = new AlertDialog.Builder(this);
            builderBorrarFavorito.setMessage("¿Estás seguro de querer borrar esta cerveza de tu lista de favoritos?");
            builderBorrarFavorito.setTitle("Borrar favorito");

            /* Botón de decisión afirmativo. */
            builderBorrarFavorito.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    referenciaBdd.child(usuarioActual.getUid()).child("favoritas").child(cervezaAlmacenada.getId()).removeValue();
                    btnFavorito.setBackgroundColor(Color.parseColor("#b28223"));
                    btnFavorito.setTextColor(Color.parseColor("#FFFFFF"));
                    btnFavorito.setText(AGREGAR_FAVORITO);

                    /* Muestra una Snackbar para que el usuario pueda deshacer la acción de ser necesario. */
                    Snackbar.make(findViewById(R.id.clContenedorDetalle), "Cerveza eliminada de favoritos", Snackbar.LENGTH_LONG).setAction("Deshacer", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            referenciaBdd.child(usuarioActual.getUid()).child("favoritas").child(cervezaAlmacenada.getId()).setValue(cervezaAlmacenada);
                            btnFavorito.setText(BORRAR_FAVORITO);
                            btnFavorito.setBackgroundColor(Color.parseColor("#000000"));
                            btnFavorito.setTextColor(Color.parseColor("#FFFFFF"));
                            Snackbar.make(findViewById(R.id.clContenedorDetalle), "Cerveza añadida a favoritos", Snackbar.LENGTH_SHORT).show();
                        }
                    }).setActionTextColor(Color.parseColor("#b28223")).show();

//                    Toast.makeText(DetalleCervezaActivity.this, "Favorito eliminado.", Toast.LENGTH_SHORT).show();
//
//                    intentCambio = new Intent(DetalleCervezaActivity.this, HomeActivity.class);
//                    startActivity(intentCambio);
//                    finish(); /* Hace que no se pueda navegar desde DetalleCervezaActivity hasta HomeActivity de nuevo. */
                }
            });

            /* Botón de decisión negativa. */
            builderBorrarFavorito.setNegativeButton("No borrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog dialogBorrarFavorito = builderBorrarFavorito.create();
            dialogBorrarFavorito.show();
        }

    }
}
