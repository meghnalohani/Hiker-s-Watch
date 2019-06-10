package com.meghna.project.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            startListening();

        }

    }

    public void startListening()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }


    public void updateLocationInfo(Location location)
    {
        TextView lat=(TextView)findViewById(R.id.lat);
        TextView lon=(TextView)findViewById(R.id.log);
        TextView alt=(TextView)findViewById(R.id.alt);
        TextView acc=(TextView)findViewById(R.id.acc);
        TextView add=(TextView)findViewById(R.id.add);
        lat.setText("Latitude : "+location.getLatitude());
        lon.setText("Longitude : "+location.getLongitude());
        alt.setText("Altitude : "+location.getAltitude());
        acc.setText("Accuracy : "+location.getAccuracy());
        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        String address="Could not find address";
        try {
            List<Address> addlist=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addlist!=null && addlist.size()>0){
                address="";
                Log.i("Address", String.valueOf(addlist.get(0)));


                if(addlist.get(0).getCountryCode()!=null)
                {
                    address+=addlist.get(0).getCountryCode()+"\n";
                }
                if(addlist.get(0).getCountryName()!=null)
                {
                    address+=addlist.get(0).getCountryName()+"\n";
                }
                if(addlist.get(0).getLocality()!=null)
                {
                    address+=addlist.get(0).getLocality()+"\n";
                }
                if(addlist.get(0).getPostalCode()!=null)
                {
                    address+=addlist.get(0).getPostalCode()+"\n";
                }
            }
            add.setText("Address :"+address);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location info", location.toString());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (Build.VERSION.SDK_INT < 23) {
            startListening();

        }
        else
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
            { ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
            else
        { locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,locationListener);
        Location location=locationManager.getLastKnownLocation((LocationManager.GPS_PROVIDER));
        if(location!=null)
            updateLocationInfo(location);


        }
        }
    }
}


