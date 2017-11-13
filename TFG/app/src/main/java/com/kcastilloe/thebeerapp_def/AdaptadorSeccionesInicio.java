package com.kcastilloe.thebeerapp_def;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin_000 on 13/11/2017.
 */

/* Clase modelo del adaptador personalizado para el TabLayout de la ventana de inicio (Login y Registro). */
public class AdaptadorSeccionesInicio extends FragmentPagerAdapter {

    private final List<Fragment> lListaFragment = new ArrayList<>(); /* Lista de los Fragments. */
    private final List<String> lListaTitulosFragment = new ArrayList<>(); /* Lista de títulos de los Fragments. */

    /* Añade los Fragment a la lista de Fragment. */
    public void agregarFragment(Fragment fragment, String titulo) {
        lListaFragment.add(fragment);
        lListaTitulosFragment.add(titulo);
    }

    /* Constructor. */
    public AdaptadorSeccionesInicio(FragmentManager fm) {
        super(fm);
    }

    /* Devuelve el título de la página. */
    @Override
    public CharSequence getPageTitle(int position) {
        return lListaTitulosFragment.get(position);
    }

    /* Devuelve el Fragment actual. */
    @Override
    public Fragment getItem(int position) {
        return lListaFragment.get(position);
    }

    /* Devuelve el número de Fragments. */
    @Override
    public int getCount() {
        return lListaFragment.size();
    }
}
