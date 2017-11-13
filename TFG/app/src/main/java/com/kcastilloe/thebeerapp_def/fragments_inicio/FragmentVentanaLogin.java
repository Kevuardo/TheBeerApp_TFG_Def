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

public class FragmentVentanaLogin extends Fragment {

    private static final String TAG = "FragmentVentanaLogin";
    private Button btnTest1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ventana_login, container, false);
        btnTest1 = (Button) view.findViewById(R.id.btnTest1);

        btnTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "CLICK 1", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
