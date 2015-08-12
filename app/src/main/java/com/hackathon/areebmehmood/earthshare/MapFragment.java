package com.hackathon.areebmehmood.earthshare;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    private Boolean refuge = true, food = true, home = true, medical = true;
    private Button refugeButton;
    private Button foodButton;
    private Button homeButton;
    private Button medicalButton;

    private ArrayList<Marker> markers = new ArrayList<Marker>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        refugeButton = (Button) rootView.findViewById(R.id.refuge_button);
        foodButton = (Button) rootView.findViewById(R.id.food_button);
        homeButton = (Button) rootView.findViewById(R.id.home_button);
        medicalButton = (Button) rootView.findViewById(R.id.medical_button);


        refugeButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        refuge = !refuge;
                    }
                });
        foodButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        food = !food;
                    }
                });
        homeButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        home = !home;
                    }
                });
        medicalButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        medical = !medical;
                    }
                });
        if (!refuge) {
            for (Marker m : markers) {
                if (m.getTitle().contains("refuge")) {
                    m.setVisible(false);
                }
            }
        }
        if (!medical) {
            for (Marker m : markers) {
                if (m.getTitle().contains("Medical")) {
                    m.setVisible(false);
                }
            }
        }
        if (!food) {
            for (Marker m : markers) {
                if (m.getTitle().contains("Food")) {
                    m.setVisible(false);
                }
            }
        }
        if (!home) {
            for (Marker m : markers) {
                if (m.getTitle().contains("Home")) {
                    m.setVisible(false);
                }
            }
        }


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        googleMap.setMyLocationEnabled(true);
        View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 175);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        try {
            makeQuakes(downloadContent("http://earthquake.usgs.gov/fdsnws/event/1/query?format=text&limit=500"));
        } catch (IOException e) {

        }

        return rootView;
    }

    public void makeQuakes(DataStruct[] eartQuake) {
        for (DataStruct d : eartQuake) {
            if (d != null) {
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(Double.parseDouble(d.latitude), Double.parseDouble(d.longitude))).title(d.name + " | Magnitude: " + d.magnitude);
                googleMap.addMarker(marker);
            }
        }
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return p1;
    }

    public void addMarker(String address, String name, String details) {
        LatLng latLong = getLocationFromAddress(address);
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latLong.latitude, latLong.longitude)).title(name + " | " + details);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        Marker m = googleMap.addMarker(marker);
        markers.add(m);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLong).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    private DataStruct[] downloadContent(String myurl) throws IOException {
        InputStream is = null;
        int length = 5000;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = convertInputStreamToString(is, length);
            String[] arrString = contentAsString.split("\n");
            int entryNumber = -1;
            int DATA_COUNT = 30;
            DataStruct[] eartQuake = new DataStruct[50];

            for (int i = 1; i < arrString.length; i++) {
                String[] data = arrString[i].split("\\|");
                if (entryNumber >= DATA_COUNT - 1) break;
                entryNumber++;
                if (data.length > 12) {
                    eartQuake[entryNumber] = new DataStruct(data[1], data[2], data[3], data[10], data[12]);
                }
            }

            Toast.makeText(getActivity(), "" + arrString.length, Toast.LENGTH_LONG).show();
            return eartQuake;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    class DataStruct {

        String longitude, latitude, magnitude, time, name;

        public DataStruct(String time, String latitude, String longitude, String magnitude, String name) {
            this.longitude = longitude;
            this.latitude = latitude;
            this.magnitude = magnitude;
            this.time = time;
            this.name = name;
        }

    }

    public String convertInputStreamToString(InputStream stream, int length) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[length];
        reader.read(buffer);
        return new String(buffer);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
