package com.example.googlecloud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    GoogleMap mMap;
    List<LatLng> lstlongitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lstlongitud=new ArrayList<>();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setZoomControlsEnabled(true);


        CameraUpdate camUpd1 =
                CameraUpdateFactory
                        .newLatLngZoom(new LatLng(40.68931426278643, -74.04459172111524), 17);
        mMap.moveCamera(camUpd1);
        mMap.setOnMapClickListener(this);


    }
    private float calcularDistancia(LatLng p1, LatLng p2) {
        float[] sum = new float[1];
        Location.distanceBetween(p1.latitude, p1.longitude, p2.latitude, p2.longitude, sum);
        return sum[0];
    }
    PolylineOptions lineas = new PolylineOptions();
    @Override
    public void onMapClick(LatLng latLng) {
        MarkerOptions marcador = new MarkerOptions();
        marcador.position(latLng);
        marcador.title("Punto" + " " + (lineas.getPoints().size()+1));
        mMap.addMarker(marcador);
        lineas.add(latLng);

        if (lineas.getPoints().size() == 6) {
            lineas.add(lineas.getPoints().get(0));
            lineas.width(8);
            lineas.color(Color.RED);
            mMap.addPolyline(lineas);

            float distanciaAcumulada = 0;
            for (int i = 0; i < lineas.getPoints().size() - 1; i++) {
                LatLng puntoActual = lineas.getPoints().get(i);
                LatLng puntoSiguiente = lineas.getPoints().get(i + 1);
                distanciaAcumulada += calcularDistancia(puntoActual, puntoSiguiente);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("DISTANCIA CALCULADA");
            builder.setMessage("Distancia acumulada entre los puntos es: " + distanciaAcumulada + " metros");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            lineas.getPoints().clear();
        }
    }
}