package com.kcastilloe.thebeerapp_def.fragments_inicio;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kcastilloe.thebeerapp_def.MainActivity;
import com.kcastilloe.thebeerapp_def.R;

/**
 * Created by kevin_000 on 13/11/2017.
 */

public class FragmentVentanaRegistro extends Fragment {

    private static final String TAG = "FragmentVentanaRegistro";

    private EditText etNombreRegistro, etNickRegistro, etEdadRegistro, etPasswordRegistro, etPasswordVerificacionRegistro;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_ventana_registro, container, false); /* Selecciona el layout. */

        /* Recogida de elementos de la vista. */
        etNombreRegistro = (EditText) view.findViewById(R.id.etNombreRegistro);
        etNickRegistro = (EditText) view.findViewById(R.id.etNickRegistro);
        etEdadRegistro = (EditText) view.findViewById(R.id.etEdadRegistro);
        etPasswordRegistro = (EditText) view.findViewById(R.id.etPasswordRegistro);
        etPasswordVerificacionRegistro = (EditText) view.findViewById(R.id.etPasswordVerificacionRegistro);

        /* Recoge el botón y le añade un OnClickListener que se ejecutará cada vez que se pulse el botón. */
        final Button btnRegistrarUsuario = view.findViewById(R.id.btnRegistrarUsuario);
        btnRegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* Recoge los valores de los campos. */
                String nombre = "";
                String nick = "";
                int edad = 0;
                String password = etPasswordRegistro.getText().toString();
                String passwordVerificacion = etPasswordRegistro.getText().toString();

                /* Verifica que los campos no estén vacíos. */
                if (etNombreRegistro.getText().toString().trim().compareToIgnoreCase("") == 0) {
                    Toast.makeText(view.getContext(), "Introduzca un nombre, por favor.", Toast.LENGTH_LONG).show();
                } else {
                    nombre = etNombreRegistro.getText().toString();
                    if (etNickRegistro.getText().toString().trim().compareToIgnoreCase("") == 0) {
                        Toast.makeText(view.getContext(), "Introduzca un nick de usuario, por favor.", Toast.LENGTH_LONG).show();
                    } else {
                        if (etEdadRegistro.getText().toString().trim().compareToIgnoreCase("") == 0) {
                            Toast.makeText(view.getContext(), "Introduzca una edad válida, por favor.", Toast.LENGTH_LONG).show();
                        } else {
                            edad = Integer.parseInt(etEdadRegistro.getText().toString());
                            /* Faltan los passwords!!!! */
                        }
                    }
                }

                /* De esta manera se llama al método propio de la MainActivity desde un Fragment. */
                ((MainActivity) getActivity()).registrarUsuario(nombre, nick, edad, password);
            }
        });

        return view;
    }
}
