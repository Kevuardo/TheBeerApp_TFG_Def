package com.kcastilloe.thebeerapp_def.fragments_inicio;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kcastilloe.thebeerapp_def.MainActivity;
import com.kcastilloe.thebeerapp_def.R;

import java.util.regex.Pattern;

/**
 * Clase Java que contiene el modelo del Fragment de inicio de sesión de usuario y sus métodos de
 * validación de campos.
 * @author Kevin Castillo Escudero
 */

public class FragmentVentanaLogin extends Fragment {

    private static final String TAG = "FragmentVentanaLogin";

    /* Los campos del formulario. */
    private EditText etNickLogin, etPasswordLogin;

    /* Las validaciones de los campos de los formularios. */
    private static final Pattern PATRON_NICK = Pattern.compile("^[a-zA-Z0-9_]{5,15}$");
    private static final Pattern PATRON_PASSWORD = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$");

    private ColorStateList bordeRojo;
    private ColorStateList bordeVerde;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_ventana_login, container, false); /* Selecciona el layout. */

        /* Los colores para los bordes de los campos. */
        bordeRojo = ColorStateList.valueOf(ContextCompat.getColor(view.getContext(), R.color.red));
        bordeVerde = ColorStateList.valueOf(ContextCompat.getColor(view.getContext(), R.color.green));

        /* Recogida de elementos de la vista. */
        etNickLogin = (EditText) view.findViewById(R.id.etNickLogin);
        etPasswordLogin = (EditText) view.findViewById(R.id.etPasswordLogin);

        /* Añade TextChangedListener a los EditText para validarlos dinámicamente según cambie su contenido. */
        etNickLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                /* Una vez que el texto ha cambiado, pasa a validarlo.
                 * Si es válido, pone el borde en verde; si no, lo pone en rojo. */
                if (!validarNick(editable.toString())) {
                    etNickLogin.setBackgroundTintList(bordeRojo);
                } else {
                    etNickLogin.setBackgroundTintList(bordeVerde);
                }
            }
        });
        etPasswordLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                /* Una vez que el texto ha cambiado, pasa a validarlo.
                 * Si es válido, pone el borde en verde; si no, lo pone en rojo. */
                if (!validarPassword(editable.toString())) {
                    etPasswordLogin.setBackgroundTintList(bordeRojo);
                } else {
                    etPasswordLogin.setBackgroundTintList(bordeVerde);
                }
            }
        });

        /* Recoge el botón y le añade un OnClickListener que se ejecutará cada vez que se pulse el botón. */
        final Button btnIniciarSesion = view.findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /* Recoge los valores de los campos. */
                String nick = "";
                String password = "";

                /* Variables bandera para analizar la validación de campos. */
                boolean nickValido = false;
                boolean passwordValida = false;

                /* Evalúa si el campo está vacío. */
                if (etNickLogin.getText().toString().trim().compareToIgnoreCase("") == 0) {
                    Toast.makeText(view.getContext(), "Introduzca un nick de usuario, por favor.", Toast.LENGTH_LONG).show();
                    etNickLogin.setBackgroundTintList(bordeRojo);
                } else {
                    /* Recoge el valor del campo, y evalúa si cumple con los estándares definidos por la expresión regular. */
                    /* EXTRA: EVALUAR SI EL NICK DE USUARIO YA ESTÁ SIENDO UTILIZADO POR OTRO USUARIO. */
                    nick = etNickLogin.getText().toString().trim();
                    if (!validarNick(nick)) {
                        Toast.makeText(view.getContext(), "Nick de usuario inválido; pruebe con otro", Toast.LENGTH_SHORT).show();
                        etNickLogin.setBackgroundTintList(bordeRojo);
                    } else {
                        etNickLogin.setBackgroundTintList(bordeVerde);
                        nickValido = true;
                    }
                }

                /* Evalúa si el campo está vacío. */
                if (etPasswordLogin.getText().toString().trim().compareToIgnoreCase("") == 0) {
                    Toast.makeText(view.getContext(), "Introduzca una contraseña, por favor", Toast.LENGTH_SHORT).show();
                    etPasswordLogin.setBackgroundTintList(bordeRojo);
                } else {
                    /* Recoge el valor del campo, y evalúa si cumple con los estándares definidos por la expresión regular. */
                    password = etPasswordLogin.getText().toString().trim();
                    if (!validarPassword(password)) {
                        Toast.makeText(view.getContext(), "La contraseña ha de tener mínimo 8 caracteres, incluyendo mínimo 1 número, 1 mayúscula y 1 minúscula.", Toast.LENGTH_SHORT).show();
                        etPasswordLogin.setBackgroundTintList(bordeRojo);
                    } else {
                        etPasswordLogin.setBackgroundTintList(bordeVerde);
                        passwordValida = true;
                    }
                }

                /* Si todas las variables bandera están igualadas a true, significa que todos los campos son válidos,
                * y por tanto puede llamar a la función de inicio de sesión de usuarios de la MainActivity. */
                if (nickValido && passwordValida) {
                    /* De esta manera se llama al método propio de la MainActivity desde un Fragment. */
                    ((MainActivity) getActivity()).iniciarSesionUsuario(nick, password);
                }
            }
        });

        return view;
    }

    /**
     * Evalúa si el nick introducido es válido o no; devuelve un booleano.
     *
     * @param nickUsuario El nick de usuario a evaluar.
     * @return Un booleano que será true si cumple con los estándares o false si no.
     */
    private boolean validarNick(String nickUsuario) {
        return PATRON_NICK.matcher(nickUsuario).matches();
    }

    /**
     * Evalúa si el nick introducido es válido o no; devuelve un booleano.
     *
     * @param passwordUsuario La contraseña de usuario a evaluar.
     * @return Un booleano que será true si cumple con los estándares o false si no.
     */
    private boolean validarPassword(String passwordUsuario) {
        return PATRON_PASSWORD.matcher(passwordUsuario).matches();
    }

}
