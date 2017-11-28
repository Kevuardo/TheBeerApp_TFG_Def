package com.kcastilloe.thebeerapp_def;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kcastilloe.thebeerapp_def.modelo.Cerveza;
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
    private Usuario usuarioAlmacenado; /* El usuario almacenado en la BDD. */
    private Cerveza cervezaAlmacenada; /* La cerveza almacenada en la BDD. */
    private String idCerveza; /* La cadena identificadora de la cerveza en la BDD. */
    private TextView tvNombreCervezaDetalle, tvGradacionCervezaDetalle, tvTipoCervezaDetalle,
            tvPaisOrigenCervezaDetalle, tvDescripcionCervezaDetalle;

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

    }


}
