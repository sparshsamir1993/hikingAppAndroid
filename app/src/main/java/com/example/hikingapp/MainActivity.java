package com.example.hikingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    TextView latitudeText;
    TextView longText;
    TextView addressText;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1000, locationListener);
                }
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        latitudeText = findViewById(R.id.latitudeText);
        longText = findViewById(R.id.longText);
        addressText = findViewById(R.id.addressText);


        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String latitude = "Latitude :: " + location.getLatitude();
                String longitude = "Longitude :: " + location.getLongitude();

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> currentAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (currentAddress != null && currentAddress.size() > 0) {
                        String address = "Address :: \n";
                        Address cAdd = currentAddress.get(0);
                        String addressLine1 = cAdd.getAddressLine(0) != null ? cAdd.getAddressLine(0) : null;
                        String country = cAdd.getCountryName() != null ? cAdd.getCountryName() : null;
                        String area = cAdd.getAdminArea() != null ? cAdd.getAdminArea() : null;

                        if (addressLine1 != null) {
                            address += addressLine1;
                        }
                        if (country != null) {
                            address += " " + country;
                        }
                        if (area != null) {
                            address += " " + area;
                        }

                        addressText.setText(address);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                latitudeText.setText(latitude);
                longText.setText(longitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1000, locationListener);
        }


    }
}
