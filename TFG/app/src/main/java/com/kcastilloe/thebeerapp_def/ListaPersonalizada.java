package com.kcastilloe.thebeerapp_def;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kcastilloe.thebeerapp_def.modelo.Cerveza;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Clase usada para aplicar un modelo sobre la lista en la que se ha de hacer el volcado de datos de las cervezas favoritas.
 *
 * @author Kevin Castillo Escudero
 */

public class ListaPersonalizada extends ArrayAdapter<Cerveza> {

    private ArrayList<Cerveza> alCervezas = new ArrayList();
    private TextView tvNombreItem, tvGradosItem, tvTipoItem, tvPaisOrigenItem;
    private ImageView ivImagenItem;
    private int idVistaElemento = 0;

    /**
     * Constructor de ListaPersonalizada.
     *
     * @param context El contexto actual.
     * @param idVistaElemento El id de la lista sobre el que se aplicará el modelo.
     * @param alCervezas La colección de datos que se usará para rellenar los items de la lista.
     */
    public ListaPersonalizada(@NonNull Context context, int idVistaElemento, @NonNull ArrayList<Cerveza> alCervezas) {
        super(context, idVistaElemento, alCervezas);
        this.idVistaElemento = idVistaElemento;
        this.alCervezas = alCervezas;
    }

    /* Recoge la vista en la que se ha de hacer el volcado de datos sobre la lista. */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vistaFila = convertView;
        if (vistaFila == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vistaFila = inflater.inflate(idVistaElemento, parent, false);
        }

        /* Carga del nombre de la cerveza. */
        tvNombreItem = (TextView) vistaFila.findViewById(R.id.tvNombreItem);
        tvNombreItem.setText(alCervezas.get(position).getNombre());

        /* Carga de los grados de la cerveza. */
        tvGradosItem = (TextView) vistaFila.findViewById(R.id.tvGradosItem);
        tvGradosItem.setText("Grados: " + Float.toString(alCervezas.get(position).getGrados()) + "%");

        /* Carga del tipo de la cerveza. */
        tvTipoItem = (TextView) vistaFila.findViewById(R.id.tvTipoItem);
        tvTipoItem.setText("Tipo: " + alCervezas.get(position).getTipo());

        /* Carga del país de origen de la cerveza. */
        tvPaisOrigenItem = (TextView) vistaFila.findViewById(R.id.tvPaisOrigenItem);
        tvPaisOrigenItem.setText("País de origen: " + alCervezas.get(position).getPaisOrigen());

        /* Carga de la imagen de la cerveza. */
        /*ivImagenItem = (ImageView) vistaFila.findViewById(R.id.ivImagenItem);
        byte[] bytesFoto = alCervezas.get(position).getFoto();
        ByteArrayInputStream bytesLectura = new ByteArrayInputStream(bytesFoto);
        Bitmap imagenContacto = BitmapFactory.decodeStream(bytesLectura);
        ivImagenItem.setImageBitmap(imagenContacto);*/

        return vistaFila; /* Devuelve la vista personalizada de la fila. */
    }
}
