package com.example.parte3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    GoogleMap mapa;
    LatLng ubicacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// Obtenemos el mapa de forma asíncrona (notificará cuando esté listo)
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Toast.makeText(this, "GPS available", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "GPS not available", Toast.LENGTH_LONG).show();
        }
        LocationListener locationListener = new LocationListener()
        {
            public void onLocationChanged(Location location)
            {
                Toast.makeText(getApplicationContext(), "Se cambio de posicion", Toast.LENGTH_SHORT).show();
                Double latitude=location.getLatitude();
                Double longitude=location.getLatitude();
                Toast.makeText(getApplicationContext(), "latitud: "+ latitude.toString()+ " longitud: "+ longitude.toString(), Toast.LENGTH_SHORT).show();
                if(checkseguir==1)
                    if (mapa.getMyLocation() != null)
                        mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mapa.getMyLocation().getLatitude(),
                                        mapa.getMyLocation().getLongitude()), 15));
            }
            public void onStatusChanged(String provider, int status, Bundle extras)
            {
            }
            public void onProviderEnabled(String provider)
            {
            }
            public void onProviderDisabled(String provider)
            {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        ubicacion = new LatLng(-18.013766, -70.255331); //Nos ubicamos en la UNJBG
        mapa.addMarker(new MarkerOptions().position(ubicacion).title("Marcador Tacna"));
        mapa.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
        mapa.setOnMapClickListener(this);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {//si tengo el permiso localizacion
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setZoomControlsEnabled(false);//activa ubicacion
            mapa.getUiSettings().setCompassEnabled(true);
        } else {
            Button btnMiPos=(Button) findViewById(R.id.btnmiubi);//desabilita el bootn que llama a la funcion ubicacion
            btnMiPos.setEnabled(false);
        }
    }
    public void moveCamera(View view) {
        mapa.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
    }
    public void addMarker(View view) {
        mapa.addMarker(new MarkerOptions().position(
                mapa.getCameraPosition().target));
    }
    @Override public void onMapClick(LatLng puntoPulsado) {
        Log.i("mimarcador",puntoPulsado.toString());
        Log.i("mimarcador",String.valueOf(puntoPulsado.latitude));
        Log.i("mimarcador",String.valueOf(puntoPulsado.longitude));
        mapa.addMarker(new MarkerOptions().position(puntoPulsado)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    }

    public void ubicacion(View view) {
        if (mapa.getMyLocation() != null)
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(//hace la animacion
                    new LatLng(mapa.getMyLocation().getLatitude(),
                            mapa.getMyLocation().getLongitude()), 15));//zoom 1 al 20
    }

    int checkseguir=0;
    public void Seguir(View view) {
        Button btnseguir=findViewById(R.id.btnseguir);
        if (checkseguir==0) {
            checkseguir = 1;
            btnseguir.setText("OFF seguir");
            Toast.makeText(this, "Se Activo el seguimiento", Toast.LENGTH_SHORT).show();
        }
        else{
            checkseguir = 0;
            btnseguir.setText("ON seguir");
            Toast.makeText(this, "Se desactivo el seguimiento", Toast.LENGTH_SHORT).show();
        }
    }

    public void mifusedlocation(View view) {
        startActivity(new Intent(this, MiFusedLocation.class));
    }

    public void migoogleapiclient(View view) {
        startActivity(new Intent(this, MiFusedLocationClient.class));
    }
}
