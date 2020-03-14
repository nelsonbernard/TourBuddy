package com.cheapassapps.tourbuddy.Views;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cheapassapps.tourbuddy.Models.Round;
import com.cheapassapps.tourbuddy.R;
import com.cheapassapps.tourbuddy.ViewModels.CurrentRoundViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class RoundGUI extends Fragment implements OnMapReadyCallback {

    //private RoundGuiViewModel mViewModel;
    private CurrentRoundViewModel mRoundViewModel;
    private MapView mapView;
    private GoogleMap googleMap;
    private TextView txtFront;
    private TextView txtCenter;
    private TextView txtBack;
    private TextView txtHoleNum;
    private TextView txtParNum;
    private TextView txtYardsNum;
    private LinearLayout linearLayout;
    private Integer currentHole;
    private static final int LOCATION_REQ_CODE = 123;
    private Round round;
    private LocationManager locationManager;
    private LatLng myPosition;
    private static final double YARDS_TO_METERS = 1.0936132983;

    public static RoundGUI newInstance() {
        return new RoundGUI();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.round_gui_fragment, container, false);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        txtFront = (TextView) view.findViewById(R.id.txtFront);
        txtCenter = (TextView) view.findViewById(R.id.txtCenter);
        txtBack = (TextView) view.findViewById(R.id.txtBack);
        txtHoleNum = (TextView) view.findViewById(R.id.txtHoleNum);
        txtParNum = (TextView) view.findViewById(R.id.txtParNum);
        txtYardsNum = (TextView) view.findViewById(R.id.txtYardsNum);
        mapView = (MapView) view.findViewById(R.id.map);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        for(int i = 0; i < linearLayout.getChildCount(); i++){
            final Button button = (Button) linearLayout.getChildAt(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(myPosition != null){
                        currentHole = Integer.parseInt(button.getText().toString()) - 1;
                        txtHoleNum.setText(Integer.toString(currentHole + 1));
                        txtParNum.setText(Integer.toString(mRoundViewModel.getCurrentRound().getValue().getHole(currentHole).getPar()));

                        updateUI();
                    }
                    else{
                        Toast.makeText(getActivity(), "Loading hole data...", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRoundViewModel = ViewModelProviders.of(this).get(CurrentRoundViewModel.class);
        mRoundViewModel.getCurrentRound().observe(this, new Observer<Round>(){
            @Override
            public void onChanged(Round currentRound) {
                if(currentRound.getCourse() != null){
                    round = currentRound;
                    if(currentHole == null)
                        currentHole = 1;

                    updateUI();
                }
             }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMyLocationEnabled(true);
        googleMap.setMinZoomPreference(17);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        getMapPermission();
    }

    private void getMapPermission(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQ_CODE);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListenerGPS);
        }
    }

    private LocationListener locationListenerGPS=new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
             double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            myPosition = new LatLng(latitude, longitude);

            updateUI();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            String message = "";

            switch (status){
                case 1:
                    message = "GPS location found";
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    break;
            }

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case LOCATION_REQ_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    googleMap.setMyLocationEnabled(true);
                }
            }
        }
    }

    private void updateUI(){
        float[] distanceFront = new float[1];
        float[] distanceCenter = new float[1];
        float[] distanceBack = new float[1];

        if(round != null && currentHole != null){
            LatLng position = new LatLng(round.getHole(currentHole).getMiddleLat(), round.getHole(currentHole).getMiddleLong());

            Location.distanceBetween(myPosition.latitude, myPosition.longitude,round.getHole(currentHole).getFrontLat(), round.getHole(currentHole).getFrontLong(), distanceFront);
            Location.distanceBetween(myPosition.latitude, myPosition.longitude,round.getHole(currentHole).getMiddleLat(), round.getHole(currentHole).getMiddleLong(), distanceCenter);
            Location.distanceBetween(myPosition.latitude, myPosition.longitude,round.getHole(currentHole).getBackLat(), round.getHole(currentHole).getBackLong(), distanceBack);
            txtFront.setText(Integer.toString((int) (distanceFront[0] * YARDS_TO_METERS)));
            txtCenter.setText(Integer.toString((int) (distanceCenter[0] * YARDS_TO_METERS)));
            txtBack.setText(Integer.toString((int) (distanceBack[0] * YARDS_TO_METERS)));

            if(googleMap != null && myPosition != null){
                LatLngBounds bounds = new LatLngBounds(myPosition, new LatLng(round.getHole(currentHole).getBackLat(), round.getHole(currentHole).getBackLong()));
                googleMap.setLatLngBoundsForCameraTarget(bounds);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        }
    }
}
