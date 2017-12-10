package com.kcastilloe.thebeerapp_def;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kcastilloe.thebeerapp_def.modelo.Cerveza;
import com.kcastilloe.thebeerapp_def.modelo.ReferenciasFirebase;
import com.kcastilloe.thebeerapp_def.modelo.UbicacionCerveza;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    /* Elementos con modelo hecho por mí. */
    private UbicacionCerveza ubicacionAlmacenada;
    private ArrayList<UbicacionCerveza> alUbicacionesAlmacenadas = new ArrayList();

    /* Elementos propios de Android Studio. */
    private GoogleMap mMap;
    private FirebaseDatabase bddFirebase;
    private DatabaseReference referenciaBdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        /* Recoge el SupportMapFragment (derivado de Fragment para Maps) del mapa. */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /* Cuando el mapa ha cargado, carga las localicaciones de la cerveza. */
        /* Recoge las localicaciones de la cerveza para cargarlas posteriormente. */
        bddFirebase = FirebaseDatabase.getInstance();
        referenciaBdd = bddFirebase.getReference(ReferenciasFirebase.REFERENCIA_UBICACIONES);
        referenciaBdd.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /* Recoge las ubicaciones de las cervezas. */
                alUbicacionesAlmacenadas.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ubicacionAlmacenada = postSnapshot.getValue(UbicacionCerveza.class);
                    alUbicacionesAlmacenadas.add(ubicacionAlmacenada);
                }
                /* Por cada ubicación añade un marcador. */
                for (int i = 0; i < alUbicacionesAlmacenadas.size(); i++) {
                    LatLng latitudLongitud = new LatLng(alUbicacionesAlmacenadas.get(i).getLatitud(), alUbicacionesAlmacenadas.get(i).getLongitud());
                    mMap.addMarker(new MarkerOptions().position(latitudLongitud).title(alUbicacionesAlmacenadas.get(i).getTitulo()));
                }
                /* Define el rango entre las ubicaciones para situarlas todas, de manera centrada,
                * en la pantalla del usuario. */
                LatLngBounds rangoUbicacionesCerveza = new LatLngBounds(
                        new LatLng(alUbicacionesAlmacenadas.get(0).getLatitud(), alUbicacionesAlmacenadas.get(0).getLongitud()),
                        new LatLng(alUbicacionesAlmacenadas.get(alUbicacionesAlmacenadas.size() - 1).getLatitud(), alUbicacionesAlmacenadas.get(alUbicacionesAlmacenadas.size() - 1).getLongitud()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rangoUbicacionesCerveza.getCenter(), 1));

                /* Anima el mapa, añadiendo un zoom dinámico que va desde la vista global (zoom 1)
                hasta la vista de las calles (zoom 15). */
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error de BDD", databaseError.getMessage()+ "");
            }

        });

    }
}
