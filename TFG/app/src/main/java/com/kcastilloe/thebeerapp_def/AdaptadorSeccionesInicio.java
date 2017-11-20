package com.kcastilloe.thebeerapp_def;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase modelo del adaptador personalizado para el TabLayout de la ventana de inicio (Login y Registro).
 *
 * @author Kevin Castillo Escudero
 */

public class AdaptadorSeccionesInicio extends FragmentPagerAdapter {

    private final List<Fragment> lListaFragment = new ArrayList<>(); /* Lista de los Fragments. */
    private final List<String> lListaTitulosFragment = new ArrayList<>(); /* Lista de títulos de los Fragments. */

    /**
     * Añade los Fragment a la lista de Fragment.
     *
     * @param fragment El objeto Fragment a añadir.
     * @param titulo El título del Fragment (se mostrará en su sección en el TabLayout).
     */
    public void agregarFragment(Fragment fragment, String titulo) {
        lListaFragment.add(fragment);
        lListaTitulosFragment.add(titulo);
    }

    /* Constructor heredado de FragmentPagerAdapter. */
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

    /* Devuelve el número de Fragments en total. */
    @Override
    public int getCount() {
        return lListaFragment.size();
    }
}
