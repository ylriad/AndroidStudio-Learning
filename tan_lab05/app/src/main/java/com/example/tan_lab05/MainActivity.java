package com.example.tan_lab05;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<LocationData> locations = new ArrayList<>();
    private LocationData currentLocation;

    // Data Class
    static class LocationData {
        String name;
        double lat;
        double lng;
        String description;
        String youtubeId;
        String websiteLink;

        public LocationData(String name, double lat, double lng, String desc, String link) {
            this.name = name;
            this.lat = lat;
            this.lng = lng;
            this.description = desc;
            this.websiteLink = link;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Initialize Data (EDIT COORDINATES TO YOUR DISTRICT)
        locations.add(new LocationData("Toraighyrov University", 52.266895, 76.966336, "Pavlodar State University", "https://tou.edu.kz/ru/"));
        locations.add(new LocationData("Small", 52.263084144603475, 76.97114137105537, "Supermarket", "https://small.kz/ru/pavlodar"));
        locations.add(new LocationData("KFC", 52.26830379811451, 76.968725640207, "Fast-food",  "https://www.kfc.kz/"));
        locations.add(new LocationData("Madlena", 52.2675117035311, 76.96550841903192, "A bakery",  "https://www.instagram.com/madlenapavlodar/"));
        locations.add(new LocationData("Stadium", 52.27159905884728, 76.96456394910165, "Sports territory", "https://"));
        locations.add(new LocationData("Tagam", 52.26601610843722, 76.95642241823955, "Family cafe",  "https://tagam-halal.kz/pavlodar"));

        // Get Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupUI();
    }

    private void setupUI() {
        // 5. Map Type Spinner
        Spinner mapTypeSpinner = findViewById(R.id.spinnerMapType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.map_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapTypeSpinner.setAdapter(adapter);

        mapTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mMap != null) {
                    if (position == 0) mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    if (position == 1) mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    if (position == 2) mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 3. Location Spinner
        Spinner locationSpinner = findViewById(R.id.spinnerLocations);
        List<String> names = new ArrayList<>();
        for (LocationData loc : locations) names.add(loc.name);

        ArrayAdapter<String> locAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        locAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locAdapter);

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentLocation = locations.get(position);
                moveToLocation(currentLocation);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 2. Table Buttons
        TableLayout tableLayout = findViewById(R.id.tableLayoutButtons);
        TableRow row = new TableRow(this);

        for (int i = 0; i < locations.size(); i++) {
            Button btn = new Button(this);
            btn.setText(locations.get(i).name);
            btn.setTextSize(10);
            btn.setPadding(10, 10, 10, 10);

            final int index = i;
            btn.setOnClickListener(v -> {
                currentLocation = locations.get(index);
                locationSpinner.setSelection(index);
                moveToLocation(currentLocation);
            });
            row.addView(btn);
        }
        tableLayout.addView(row);

        // 4. Details Button
        findViewById(R.id.btnDetails).setOnClickListener(v -> {
            if (currentLocation != null) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("name", currentLocation.name);
                intent.putExtra("desc", currentLocation.description);
                intent.putExtra("yt", currentLocation.youtubeId);
                intent.putExtra("link", currentLocation.websiteLink);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Select a location first", Toast.LENGTH_SHORT).show();
            }
        });

        if (!locations.isEmpty()) {
            currentLocation = locations.get(0);
            locationSpinner.setSelection(0);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (LocationData loc : locations) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(loc.lat, loc.lng))
                    .title(loc.name)
                    .snippet(loc.description));
        }
        if (!locations.isEmpty()) {
            moveToLocation(locations.get(0));
        }
    }

    private void moveToLocation(LocationData loc) {
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.lat, loc.lng), 15));
        }
    }
}