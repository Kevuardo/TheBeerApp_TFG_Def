package com.kcastilloe.thebeerapp_def;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.kcastilloe.thebeerapp_def.modelo.Usuario;

/**
 * La actividad usada para ver los detalles de la cerveza seleccionada.
 *
 * @author Kevin Castillo Escudero.
 */

public class DetalleCervezaActivity extends AppCompatActivity {

    private Intent intentCambio;
    private FirebaseAuth autenticacionFirebase;
    private FirebaseUser usuarioActual;
    private FirebaseDatabase bddFirebase;
    private DatabaseReference referenciaBdd;
    private Cerveza nuevaCerveza;
    private Usuario usuarioAlmacenado; /* El usuario almacenado en la BDD. */
    private Cerveza cervezaAlmacenada; /* La cerveza almacenada en la BDD. */
    private String idCerveza; /* La cadena identificadora de la cerveza en la BDD. */
    private TextView tvNombreCervezaDetalle, tvGradacionCervezaDetalle, tvTipoCervezaDetalle,
            tvPaisOrigenCervezaDetalle, tvDescripcionCervezaDetalle;
    private Button btnFavorito;

    /* Variables bandera para la navegación entre cervezas. */
    private boolean primeraCerveza = false;
    private boolean ultimaCerveza = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cerveza);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); /* Fuerza la posición a vertical. */

        /* Recoge la cerveza almacenada correspondiente al Token (el nombre identificador) en la BDD. */

        /* DEV ONLY: fuerza los campos a los de cualquiera de la lista de cervezas de la BDD. Para favoritos, lo guarda igual. */
        tvNombreCervezaDetalle = (TextView) findViewById(R.id.tvNombreCervezaDetalle);
        tvGradacionCervezaDetalle = (TextView) findViewById(R.id.tvGradacionCervezaDetalle);
        tvTipoCervezaDetalle = (TextView) findViewById(R.id.tvTipoCervezaDetalle);
        tvPaisOrigenCervezaDetalle = (TextView) findViewById(R.id.tvPaisOrigenCervezaDetalle);
        tvDescripcionCervezaDetalle = (TextView) findViewById(R.id.tvDescripcionCervezaDetalle);
        btnFavorito = (Button) findViewById(R.id.btnFavorito);

        btnFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarFavorita();
            }
        });

        /* Recoger una cerveza de la BD, volcar datos en la vista y luego agregar a favoritos. */
        bddFirebase = FirebaseDatabase.getInstance();
        referenciaBdd = bddFirebase.getReference(ReferenciasFirebase.REFERENCIA_CERVEZAS).child("Alhambra");

        autenticacionFirebase = FirebaseAuth.getInstance();
        usuarioActual = autenticacionFirebase.getCurrentUser();


    }

    @Override
    protected void onResume() {

        super.onResume();
        /* Se actualizan los datos de los campos de la vista cada vez que HomeActivity se reanuda (creación incluida). */
        /* Ejecución dinámica de recogida de datos por cambio de los mismos. */
        referenciaBdd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /* El parámetro que recibe el método getValue() es la clase del tipo de objeto que se desea recoger,
                * y necesita un constructor vacío. */
                nuevaCerveza = dataSnapshot.getValue(Cerveza.class);
                modificarCampos(nuevaCerveza);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error de BD", databaseError.getMessage()+ "");
            }
        });

    }

    private void modificarCampos(Cerveza nuevaCerveza) {

        tvNombreCervezaDetalle.setText("Nombre: " + nuevaCerveza.getNombre());
        tvGradacionCervezaDetalle.setText("Grados: " + Float.toString(nuevaCerveza.getGrados()) + "%");
        tvTipoCervezaDetalle.setText("Tipo: " + nuevaCerveza.getTipo());
        tvPaisOrigenCervezaDetalle.setText("País de origen: " + nuevaCerveza.getPaisOrigen());

    }

    private void guardarFavorita() {

        boolean esFavorita = false; /* Variable bandera. Si ya es favorita, la borra. Si no lo es, la añade. */

        Toast.makeText(this, "Pos mu bien, campeón.", Toast.LENGTH_SHORT).show();
        /* Crea un objeto Usuario para almacenarlo en la BDD. */
        usuarioActual = autenticacionFirebase.getCurrentUser();
        referenciaBdd = bddFirebase.getReference(ReferenciasFirebase.REFERENCIA_USUARIOS);
        /* Con child se puede seleccionar la clave que tendrá el nodo, y en este caso se desea que
        sea el UID (User ID) que Firebase proporciona automáticamente a cada usuario autenticado. */
        referenciaBdd.child(usuarioActual.getUid()).child("favoritas").child(nuevaCerveza.getNombre()).setValue(nuevaCerveza);

    }
}
