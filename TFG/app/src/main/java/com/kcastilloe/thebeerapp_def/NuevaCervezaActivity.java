package com.kcastilloe.thebeerapp_def;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        nuevaCerveza = new Cerveza("Alhambra", 5.0f, "Dorada", "España");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Amstel Lager", 5.5f, "Dorada con lúpulo", "Holanda");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Asahi", 4.5f, "Dorada", "Japón");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Beck's", 5.0f, "Dorada", "EE.UU.");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Benediktiner", 5.5f, "Dorada", "Alemania");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Birra Moretti", 4.5f, "Tostada", "Italia");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Buckler", 12.5f, "Dorada", "Alemania");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Budweiser", 3.5f, "(Lluvia) Dorada", "EE.UU");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Carslberg", 5.5f, "Dorada", "Alemania");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Chimay", 8.5f, "Dorada", "Bélgica");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Chimay Red", 10.5f, "Tostada", "Bélgica");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Corona", 5.5f, "Dorada", "Argentina");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Dos Equis", 10.5f, "Dorada", "México");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Duvel", 10.5f, "Tostada con lúpulo", "República Checa");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Erdinger Weissbier", 5.5f, "Dorada", "Alemania");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Foster's", 4.5f, "Dorada", "Reino Unido");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Franziskaner", 5.5f, "Dorada de trigo", "Alemania");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Guinness", 6.5f, "Tostada con toques de café", "Irlanda");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Gulden Draak", 12.5f, "Tostada con lúpulo", "Holanda");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Heineken", 4.5f, "Dorada", "Holanda");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Hoegaarden", 8.5f, "Dorada", "Bélgica");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("King Goblin", 10.5f, "Dorada con lúpulo", "República Checa");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Kirin Ichiban", 6.5f, "Dorada", "Japón");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Köstritzer", 7.5f, "Dorada", "Alemania");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Kronenbourg 1664", 5.5f, "Dorada", "Alemania");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Mahou", 5.5f, "Dorada con lúpulo", "España");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Newcastle Brown Ale", 8.5f, "Dorada", "Reino Unido");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Paulaner", 5.5f, "Dorada de trigo", "Alemania");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Peroni Nastro Azzurro", 4.5f, "Dorada", "Italia");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Presidente", 7.5f, "Dorada", "Brasil");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("San Miguel", 4.5f, "Dorada", "España");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Sapporo Premium Lager", 3.5f, "Dorada", "Japón");
        alCervezasInsercion.add(nuevaCerveza);

        nuevaCerveza = new Cerveza("Stella Artois", 9.5f, "Dorada", "República Checa");
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
