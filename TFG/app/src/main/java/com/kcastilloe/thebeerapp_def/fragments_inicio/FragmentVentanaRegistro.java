package com.kcastilloe.thebeerapp_def.fragments_inicio;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
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
 * Clase Java que contiene el modelo del Fragment de registro de usuario y sus métodos de
 * validación de campos.
 *
 * @author Kevin Castillo Escudero
 */

public class FragmentVentanaRegistro extends Fragment {

    private static final String TAG = "FragmentVentanaRegistro";
    private static final int EDAD_MINIMA = 18; /* Constante para almacenar el valor de edad mínimo para permitir al usuario usar la app. */
    private static final int EDAD_MAXIMA = 130; /* Constante para almacenar el valor de edad máximo para permitir al usuario usar la app. */

    /* Los campos del formulario. */
    private EditText etNickRegistro, etEdadRegistro, etEmailRegistro, etPasswordRegistro, etPasswordVerificacionRegistro;

    /* Las validaciones de los campos de los formularios. */
    /* De 5 a 15 caracteres, sólo alfanuméricos y '_'.  */
    private static final Pattern PATRON_NICK = Pattern.compile("^[a-zA-Z0-9_]{5,15}$");
    /* 8 caracteres o más; mínimo 1 minúscula, 1 mayúscula y 1 número. Sólo alfanuméricos. */
    private static final Pattern PATRON_PASSWORD = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$");

    /* Colores indicativos para los bordes de las cajas de texto. */
    private ColorStateList bordeRojo;
    private ColorStateList bordeVerde;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_ventana_registro, container, false); /* Selecciona el layout. */

        bordeRojo = ColorStateList.valueOf(ContextCompat.getColor(view.getContext(), R.color.red));
        bordeVerde = ColorStateList.valueOf(ContextCompat.getColor(view.getContext(), R.color.green));

        /* Recogida de elementos de la vista. */
        etNickRegistro = (EditText) view.findViewById(R.id.etNickRegistro);
        etEdadRegistro = (EditText) view.findViewById(R.id.etEdadRegistro);
        etEmailRegistro = (EditText) view.findViewById(R.id.etEmailRegistro);
        etPasswordRegistro = (EditText) view.findViewById(R.id.etPasswordRegistro);
        etPasswordVerificacionRegistro = (EditText) view.findViewById(R.id.etPasswordVerificacionRegistro);

        /* Añade TextChangedListener a los EditText para validarlos dinámicamente según cambie su contenido. */
        etNickRegistro.addTextChangedListener(new TextWatcher() {
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
                    etNickRegistro.setBackgroundTintList(bordeRojo);
                } else {
                    etNickRegistro.setBackgroundTintList(bordeVerde);
                }
            }
        });
        etEmailRegistro.addTextChangedListener(new TextWatcher() {
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
                if (!validarEmail(editable.toString())) {
                    etEmailRegistro.setBackgroundTintList(bordeRojo);
                } else {
                    etEmailRegistro.setBackgroundTintList(bordeVerde);
                }
            }
        });
        etEdadRegistro.addTextChangedListener(new TextWatcher() {
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
                /* Evalúa que no esté vacío para evitar errores de ejecución. */
                if (editable.toString().compareToIgnoreCase("") == 0) {
                    etEdadRegistro.setBackgroundTintList(bordeRojo);
                } else {
                    /* Si no está vacío, evalúa si es mayor de edad. */
                    int edadValidable = Integer.parseInt(editable.toString());
                    if (!validarEdad(edadValidable)) {
                        etEdadRegistro.setBackgroundTintList(bordeRojo);
                    } else {
                        etEdadRegistro.setBackgroundTintList(bordeVerde);
                    }
                }
            }
        });
        etPasswordRegistro.addTextChangedListener(new TextWatcher() {
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
                    etPasswordRegistro.setBackgroundTintList(bordeRojo);
                    /* Deshabilita la verificación de contraseña. */
                    etPasswordVerificacionRegistro.setEnabled(false);
                } else {
                    etPasswordRegistro.setBackgroundTintList(bordeVerde);
                    /* Habilita la verificación de contraseña. */
                    etPasswordVerificacionRegistro.setEnabled(true);
                }
            }
        });
        etPasswordVerificacionRegistro.addTextChangedListener(new TextWatcher() {
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
                    etPasswordVerificacionRegistro.setBackgroundTintList(bordeRojo);
                } else {
                    if (editable.toString().compareToIgnoreCase(etPasswordRegistro.getText().toString()) != 0) {
                        etPasswordVerificacionRegistro.setBackgroundTintList(bordeRojo);
                    } else {
                        etPasswordVerificacionRegistro.setBackgroundTintList(bordeVerde);
                    }
                }
            }
        });

        /* Recoge el botón y le añade un OnClickListener que se ejecutará cada vez que se pulse el botón. */
        final Button btnRegistrarUsuario = view.findViewById(R.id.btnRegistrarUsuario);
        btnRegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* Recoge los valores de los campos. */
                String nick = null;
                int edad = 0;
                String email = null;
                String password = null;
                String passwordVerificacion = null;

                /* Variables bandera para analizar la validación de campos. */
                boolean nickValido = false;
                boolean edadValida = false;
                boolean emailValido = false;
                boolean passwordValida = false;
                boolean passwordVerificacionValida = false;
                boolean passwordsCoinciden = false;

                /* Evalúa si el campo está vacío. */
                if (etNickRegistro.getText().toString().trim().compareToIgnoreCase("") == 0) {
                    Toast.makeText(view.getContext(), "Introduzca un nick de usuario, por favor.", Toast.LENGTH_SHORT).show();
                    etNickRegistro.setBackgroundTintList(bordeRojo);
                    nickValido = false;
                } else {
                    /* Recoge el valor del campo, y evalúa si cumple con los estándares definidos por la expresión regular. */
                    /* EXTRA: EVALUAR SI EL NICK DE USUARIO YA ESTÁ SIENDO UTILIZADO POR OTRO USUARIO. */
                    nick = etNickRegistro.getText().toString().trim();
                    if (!validarNick(nick)) {
                        Toast.makeText(view.getContext(), "Nick de usuario inválido; pruebe con otro", Toast.LENGTH_SHORT).show();
                        etNickRegistro.setBackgroundTintList(bordeRojo);
                        nickValido = false;
                    } else {
                        etNickRegistro.setBackgroundTintList(bordeVerde);
                        nickValido = true;
                    }
                }

                /* Evalúa si el campo está vacío. */
                if (etEdadRegistro.getText().toString().trim().compareToIgnoreCase("") == 0) {
                    Toast.makeText(view.getContext(), "Introduzca una edad, por favor.", Toast.LENGTH_SHORT).show();
                    etEdadRegistro.setBackgroundTintList(bordeRojo);
                    edadValida = false;
                } else {
                    /* Recoge el valor del campo, y evalúa si cumple con los estándares definidos por la expresión regular. */
                    edad = Integer.parseInt(etEdadRegistro.getText().toString().trim());
                    if (!validarEdad(edad)) {
                        Toast.makeText(view.getContext(), "Debe ser mayor de edad para utilizar la app", Toast.LENGTH_SHORT).show();
                        etEdadRegistro.setBackgroundTintList(bordeRojo);
                        edadValida = false;
                    } else {
                        etEdadRegistro.setBackgroundTintList(bordeVerde);
                        edadValida = true;
                    }
                }

                /* Evalúa si el campo está vacío. */
                if (etEmailRegistro.getText().toString().trim().compareToIgnoreCase("") == 0) {
                    Toast.makeText(view.getContext(), "Introduzca un e-mail, por favor.", Toast.LENGTH_SHORT).show();
                    etEmailRegistro.setBackgroundTintList(bordeRojo);
                    emailValido = false;
                } else {
                    /* Recoge el valor del campo, y evalúa si cumple con los estándares definidos por la expresión regular. */
                    email = etEmailRegistro.getText().toString().trim();
                    if (!validarEmail(email)) {
                        Toast.makeText(view.getContext(), "El e-mail debe tener un formato correcto", Toast.LENGTH_SHORT).show();
                        etEmailRegistro.setBackgroundTintList(bordeRojo);
                        emailValido = false;
                    } else {
                        etEmailRegistro.setBackgroundTintList(bordeVerde);
                        emailValido = true;
                    }
                }

                /* A continuación comprueba que las contraseñas coincidan y tengan el mismo formato. */
                /* Evalúa si el campo está vacío. */
                if (etPasswordRegistro.getText().toString().trim().compareToIgnoreCase("") == 0) {
                    Toast.makeText(view.getContext(), "Introduzca una contraseña, por favor", Toast.LENGTH_SHORT).show();
                    etPasswordRegistro.setBackgroundTintList(bordeRojo);
                    passwordValida = false;
                } else {
                    /* Recoge el valor del campo, y evalúa si cumple con los estándares definidos por la expresión regular. */
                    password = etPasswordRegistro.getText().toString().trim();
                    if (!validarPassword(password)) {
                        Toast.makeText(view.getContext(), "La contraseña ha de tener mínimo 8 caracteres, incluyendo mínimo 1 número, 1 mayúscula y 1 minúscula.", Toast.LENGTH_SHORT).show();
                        etPasswordRegistro.setBackgroundTintList(bordeRojo);
                        passwordValida = false;
                    } else {
                        etPasswordRegistro.setBackgroundTintList(bordeVerde);
                        passwordValida = true;
                    }
                }

                /* Evalúa si el campo está vacío. */
                if (etPasswordVerificacionRegistro.getText().toString().trim().compareToIgnoreCase("") == 0) {
                    Toast.makeText(view.getContext(), "Introduzca una verificación de contraseña, por favor", Toast.LENGTH_SHORT).show();
                    etPasswordVerificacionRegistro.setBackgroundTintList(bordeRojo);
                    passwordVerificacionValida = false;
                } else {
                    /* Recoge el valor del campo, y evalúa si cumple con los estándares definidos por la expresión regular. */
                    passwordVerificacion = etPasswordVerificacionRegistro.getText().toString().trim();
                    if (!validarPassword(passwordVerificacion)) {
                        Toast.makeText(view.getContext(), "La contraseña de verificación ha de tener mínimo 8 caracteres, incluyendo mínimo 1 número, 1 mayúscula y 1 minúscula.", Toast.LENGTH_SHORT).show();
                        etPasswordVerificacionRegistro.setBackgroundTintList(bordeRojo);
                        passwordVerificacionValida = false;
                    } else {
                        passwordVerificacionValida = true;
                        /* Compara las contraseñas para comprobar que coinciden. */
                        if (passwordVerificacion.compareToIgnoreCase(password) != 0) {
                            Toast.makeText(view.getContext(), "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
                            passwordsCoinciden = false;
                        } else {
                            etPasswordVerificacionRegistro.setBackgroundTintList(bordeVerde);
                            passwordsCoinciden = true;
                        }
                    }
                }

                /* Si todas las variables bandera están igualadas a true, significa que todos los campos son válidos,
                * y por tanto puede llamar a la función de registro de usuarios de la MainActivity. */
                if (nickValido && edadValida && emailValido && passwordValida && passwordVerificacionValida && passwordsCoinciden) {
                    /* De esta manera se llama al método propio de la MainActivity desde un Fragment. */
                    ((MainActivity) getActivity()).registrarUsuario(nick, email, edad, password);
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
    public boolean validarNick(String nickUsuario) {
        return PATRON_NICK.matcher(nickUsuario).matches();
    }

//    /* Futura versión. */
//    private boolean evaluarDisponibilidadNick (String nickUsuario) {
//        return false;
//    }

    /**
     * Evalúa si la edad introducida es válida o no; devuelve un booleano.
     * @param edadUsuario La edad del usuario a validar.
     * @return Un booleano que será true si cumple con los estándares o false si no.
     */
    public boolean validarEdad(int edadUsuario) {
        if (edadUsuario < EDAD_MINIMA || edadUsuario > EDAD_MAXIMA) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Evalúa si el e-mail introducido es válido o no; devuelve un booleano.
     *
     * @param emailUsuario El e-mail del usuario a validar.
     * @return Un booleano que será true si cumple con los estándares o false si no.
     */
    public boolean validarEmail(String emailUsuario) {
        return Patterns.EMAIL_ADDRESS.matcher(emailUsuario).matches();
    }

    /**
     * Evalúa si el nick introducido es válido o no; devuelve un booleano.
     *
     * @param passwordUsuario La contraseña de usuario a evaluar.
     * @return Un booleano que será true si cumple con los estándares o false si no.
     */
    public boolean validarPassword(String passwordUsuario) {
        return PATRON_PASSWORD.matcher(passwordUsuario).matches();
    }
}
