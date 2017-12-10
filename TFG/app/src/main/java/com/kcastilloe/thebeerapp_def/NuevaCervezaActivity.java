package com.kcastilloe.thebeerapp_def;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kcastilloe.thebeerapp_def.modelo.Cerveza;
import com.kcastilloe.thebeerapp_def.modelo.ReferenciasFirebase;

import java.util.ArrayList;

/**
 * La actividad usada para añadir cervezas a la BDD de la aplicación.
 *
 * @author Kevin Castillo Escudero
 */

public class NuevaCervezaActivity extends AppCompatActivity {

    private static final String TAG = "NuevaCervezaActivity";

    /* Elementos de vista. */
    private EditText etNombreNuevaCerveza, etGradosNuevaCerveza, etTipoNuevaCerveza, etPaisOrigenNuevaCerveza;
    private Button btnAgregarNuevaCerveza;

    /* Elementos propios de Android Studio.*/
    private FirebaseDatabase bddFirebase;
    private DatabaseReference referenciaBdd;
    private Cerveza nuevaCerveza;
    private ArrayList<Cerveza> alCervezasInsercion = new ArrayList();
    private Intent intentCambio;

    /* Colores indicativos para los bordes de las cajas de texto. */
    private ColorStateList bordeRojo;
    private ColorStateList bordeVerde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cerveza);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); /* Fuerza la posición a vertical. */

        bordeRojo = ColorStateList.valueOf(ContextCompat.getColor(NuevaCervezaActivity.this, R.color.red));
        bordeVerde = ColorStateList.valueOf(ContextCompat.getColor(NuevaCervezaActivity.this, R.color.green));

        /* A continuación, crea una nueva instancia de BDD en Firebase. */
        bddFirebase = FirebaseDatabase.getInstance();
        referenciaBdd = bddFirebase.getReference(ReferenciasFirebase.REFERENCIA_CERVEZAS);
        /* Si la referencia al nodo en la BDD no existe en la misma, creará dicho nodo con la referencia. */

        /* Recoge los elementos de la vista. */
        etNombreNuevaCerveza = findViewById(R.id.etNombreNuevaCerveza);
        etGradosNuevaCerveza = findViewById(R.id.etGradosNuevaCerveza);
        etTipoNuevaCerveza = findViewById(R.id.etTipoNuevaCerveza);
        etPaisOrigenNuevaCerveza = findViewById(R.id.etPaisOrigenNuevaCerveza);
        btnAgregarNuevaCerveza = findViewById(R.id.btnAgregarNuevaCerveza);

        btnAgregarNuevaCerveza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* Recoge los valores de los campos. */
                String nombreCerveza = null;
                String gradosCervezaString = null;
                float gradosCervezaNumericos = 0f;
                String tipoCerveza = null;
                String paisOrigenCerveza = null;

                 /* Variables bandera para analizar la validación de campos. */
                boolean nombreValido = false;
                boolean tipoValido = false;
                boolean gradacionValida = false;
                boolean paisOrigenValido = false;

                /* Evalúa si el nombre está vacío. */
                if (etNombreNuevaCerveza.getText().toString().trim().compareToIgnoreCase("") == 0) {
                    Toast.makeText(NuevaCervezaActivity.this, "Introduzca un nombre de cerveza, por favor.", Toast.LENGTH_SHORT).show();
                    etNombreNuevaCerveza.setBackgroundTintList(bordeRojo);
                    nombreValido = false;
                } else {
                    /* Recoge el valor del campo. */
                    nombreCerveza = etNombreNuevaCerveza.getText().toString().trim();
                    etNombreNuevaCerveza.setBackgroundTintList(bordeVerde);
                    nombreValido = true;
                }

                /* Evalúa si la gradación está vacía. */
                if (etGradosNuevaCerveza.getText().toString().trim().compareToIgnoreCase("") == 0) {
                    Toast.makeText(NuevaCervezaActivity.this, "Introduzca una gradación, por favor.", Toast.LENGTH_SHORT).show();
                    etGradosNuevaCerveza.setBackgroundTintList(bordeRojo);
                    gradacionValida = false;
                } else {
                    /* Recoge el valor del campo. */
                    gradosCervezaString = etGradosNuevaCerveza.getText().toString().trim();
                    gradosCervezaNumericos = Float.parseFloat(gradosCervezaString);
                    etGradosNuevaCerveza.setBackgroundTintList(bordeVerde);
                    gradacionValida = true;
                }

                /* Evalúa si el tipo está vacío. */
                if (etTipoNuevaCerveza.getText().toString().trim().compareToIgnoreCase("") == 0) {
                    Toast.makeText(NuevaCervezaActivity.this, "Introduzca un tipo de cerveza, por favor.", Toast.LENGTH_SHORT).show();
                    etTipoNuevaCerveza.setBackgroundTintList(bordeRojo);
                    tipoValido = false;
                } else {
                    /* Recoge el valor del campo. */
                    tipoCerveza = etTipoNuevaCerveza.getText().toString().trim();
                    etTipoNuevaCerveza.setBackgroundTintList(bordeVerde);
                    tipoValido = true;
                }

                /* Evalúa si el país de origen está vacío. */
                if (etPaisOrigenNuevaCerveza.getText().toString().trim().compareToIgnoreCase("") == 0) {
                    Toast.makeText(NuevaCervezaActivity.this, "Introduzca un país de origen, por favor.", Toast.LENGTH_SHORT).show();
                    etPaisOrigenNuevaCerveza.setBackgroundTintList(bordeRojo);
                    paisOrigenValido = false;
                } else {
                    /* Recoge el valor del campo. */
                    paisOrigenCerveza = etPaisOrigenNuevaCerveza.getText().toString().trim();
                    etPaisOrigenNuevaCerveza.setBackgroundTintList(bordeVerde);
                    paisOrigenValido = true;
                }

                if (nombreValido && gradacionValida && tipoValido && paisOrigenValido) {
                    nuevaCerveza = new Cerveza(nombreCerveza, gradosCervezaNumericos, tipoCerveza, paisOrigenCerveza);
                    almacenarCerveza(nuevaCerveza);
                }

            }
        });

    }

    /* Sólo testeo - genera varias cervezas predefinidas. */

    /**
     * Genera varias cervezas predefinidas por código y las añade a un ArrayList para su posterior
     * manejo dinámico.
     */
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

    /**
     * Agrega la cerveza generada a la BDD para que sea accesible por los usuarios de la app.
     *
     * @param nuevaCerveza La cerveza a almacenar en la BDD.
     */
    private void almacenarCerveza(Cerveza nuevaCerveza) {

//        generarCervezas();
//
//        for (int i = 0; i < alCervezasInsercion.size(); i++) {
//
//            /* Se fuerza una key aleatoria sin llegar a insertar un valor. Después se inserta la
//            cerveza como child() usando dicha key aleatoria, y finalmente se cambia el valor del id
//            al de la key para poder referenciarlo fácilmente para la DetalleCervezaActivity. */
//            String keyAleatoriaFirebase = referenciaBdd.push().getKey();
//            referenciaBdd.child(keyAleatoriaFirebase).setValue(alCervezasInsercion.get(i));
//            referenciaBdd.child(keyAleatoriaFirebase).child("id").setValue(keyAleatoriaFirebase);
//
//        }

        /* Se fuerza una key aleatoria sin llegar a insertar un valor. Después se inserta la
        cerveza como child() usando dicha key aleatoria, y finalmente se cambia el valor del id
        al de la key para poder referenciarlo fácilmente para la DetalleCervezaActivity. */
        String keyAleatoriaFirebase = referenciaBdd.push().getKey();
        referenciaBdd.child(keyAleatoriaFirebase).setValue(nuevaCerveza);
        referenciaBdd.child(keyAleatoriaFirebase).child("id").setValue(keyAleatoriaFirebase);

        Toast.makeText(this, "Cerveza añadida con éxito", Toast.LENGTH_SHORT).show();

        /* Abre la HomeActivity. */
        intentCambio = new Intent(this, HomeActivity.class);
        startActivity(intentCambio);
        finish();

    }

}
