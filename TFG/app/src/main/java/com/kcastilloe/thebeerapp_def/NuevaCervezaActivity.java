package com.kcastilloe.thebeerapp_def;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kcastilloe.thebeerapp_def.modelo.Cerveza;
import com.kcastilloe.thebeerapp_def.modelo.ReferenciasFirebase;

import java.util.ArrayList;

public class NuevaCervezaActivity extends AppCompatActivity {

    private static final String TAG = "NuevaCervezaActivity";

    private FirebaseDatabase bddFirebase;
    private DatabaseReference referenciaBdd;
    private Cerveza nuevaCerveza;
    private ArrayList<Cerveza> alCervezasInsercion = new ArrayList();
    private Intent intentCambio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cerveza);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); /* Fuerza la posición a vertical. */

        /* A continuación, crea una nueva instancia de BDD en Firebase. */
        bddFirebase = FirebaseDatabase.getInstance();
        referenciaBdd = bddFirebase.getReference(ReferenciasFirebase.REFERENCIA_CERVEZAS);
        /* Si la referencia al nodo en la BDD no existe en la misma, creará dicho nodo con la referencia. */

        almacenarCervezas();

        /* Abre la HomeActivity. */
        Intent intentCambio = new Intent(this, HomeActivity.class);
        startActivity(intentCambio);
        finish();
    }

    /* Sólo testeo - genera varias cervezxas predefinidas. */
    private void generarCervezas() {

        /* 1ª cerveza. */
        nuevaCerveza = new Cerveza("Mahou", 5.5f, "Dorada con lúpulo", "España");
        alCervezasInsercion.add(nuevaCerveza);

        /* 2ª cerveza. */
        nuevaCerveza = new Cerveza("San Miguel", 4.5f, "Dorada", "España");
        alCervezasInsercion.add(nuevaCerveza);

        /* 3ª cerveza. */
        nuevaCerveza = new Cerveza("Alhambra", 5.0f, "Dorada", "España");
        alCervezasInsercion.add(nuevaCerveza);

        /* 4ª cerveza. */
        nuevaCerveza = new Cerveza("Gulden Draak", 12.5f, "Tostada con lúpulo", "Holanda");
        alCervezasInsercion.add(nuevaCerveza);

    }

    /* Sólo testeo - agrega varias cervezas pre-generadas por código a la base de datos. */
    private void almacenarCervezas() {

        generarCervezas();

        for (int i = 0; i < alCervezasInsercion.size(); i++) {

            /* Se fuerza una key aleatoria sin llegar a insertar un valor. Después se inserta la
            cerveza como child() usando dicha key aleatoria, y finalmente se cambia el valor del id
            al de la key para poder referenciarlo fácilmente para la DetalleCervezaActivity. */
            String keyAleatoriaFirebase = referenciaBdd.push().getKey();
            referenciaBdd.child(keyAleatoriaFirebase).setValue(alCervezasInsercion.get(i));
            referenciaBdd.child(keyAleatoriaFirebase).child("id").setValue(keyAleatoriaFirebase);

        }

        Toast.makeText(this, "Todo creado con éxito", Toast.LENGTH_SHORT).show();

    }

}
