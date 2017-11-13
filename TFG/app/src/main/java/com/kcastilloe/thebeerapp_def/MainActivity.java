package com.kcastilloe.thebeerapp_def;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.kcastilloe.thebeerapp_def.fragments_inicio.FragmentVentanaLogin;
import com.kcastilloe.thebeerapp_def.fragments_inicio.FragmentVentanaRegistro;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private AdaptadorSeccionesInicio adaptadorSecciones; /* El adaptador de las secciones del TabLayout. */
    private ViewPager vpContenedor; /* EL formato de las secciones. */
    private TabLayout tlVentanas; /* El contenedor de las secciones. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: Iniciando.");

        adaptadorSecciones = new AdaptadorSeccionesInicio(getSupportFragmentManager());

        /* Configura el ViewPager con el AdaptadorSecciones. */
        vpContenedor = (ViewPager) findViewById(R.id.vpContenedor);
        configurarViewPager(vpContenedor);

        /* Después, añade el ViewPager al contenedor de ventanas del Layout (el TabLayout). */
        tlVentanas = (TabLayout) findViewById(R.id.tlVentanas);
        tlVentanas.setupWithViewPager(vpContenedor);
    }

    /* Crea un AdaptadorSeccionesInicio y le añade los Fragment.*/
    private void configurarViewPager(ViewPager viewPager) {
        AdaptadorSeccionesInicio adaptador = new AdaptadorSeccionesInicio(getSupportFragmentManager());
        adaptador.agregarFragment(new FragmentVentanaLogin(), "Login");
        adaptador.agregarFragment(new FragmentVentanaRegistro(), "Registro");
        viewPager.setAdapter(adaptador);
    }
}
