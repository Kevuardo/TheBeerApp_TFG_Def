package com.kcastilloe.thebeerapp_def.fragments_inicio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kcastilloe.thebeerapp_def.R;

/**
 * Created by kevin_000 on 13/11/2017.
 */

public class FragmentVentanaRegistro extends Fragment {

    private static final String TAG = "FragmentVentanaRegistro";
    private Button btnTest2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ventana_registro, container, false);
        btnTest2 = (Button) view.findViewById(R.id.btnTest2);

        btnTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "CLICK 2", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
