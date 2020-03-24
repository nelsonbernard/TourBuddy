package com.cheapassapps.tourbuddy.Views;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Looper;
import android.provider.Settings;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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
    private Integer currentHole;
    private Round round;
    private LatLng myPosition;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final double YARDS_TO_METERS = 1.0936132983;
    private static final int LOCATION_REQ_CODE = 123;

    public static RoundGUI newInstance() {
        return new RoundGUI();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.round_gui_fragment, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        txtFront = (TextView) view.findViewById(R.id.txtFront);
        txtCenter = (TextView) view.findViewById(R.id.txtCenter);
        txtBack = (TextView) view.findViewById(R.id.txtBack);
        txtHoleNum = (TextView) view.findViewById(R.id.txtHoleNum);
        txtParNum = (TextView) view.findViewById(R.id.txtParNum);
        txtYardsNum = (TextView) view.findViewById(R.id.txtYardsNum);
        mapView = (MapView) view.findViewById(R.id.map);

        for(int i = 0; i < linearLayout.getChildCount(); i++){
            final Button button = (Button) linearLayout.getChildAt(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(myPosition != null){
                        currentHole = Integer.parseInt(button.getText().toString()) - 1;

                        updateUI();
                    }
                    else{
                        Toast.makeText(getActivity(), "Grabbing GPS position, just a sec...", Toast.LENGTH_SHORT).show();
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
        mapView.getMapAsync(this);
        mapView.onResume();
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
                        currentHole = 0;

                    updateUI();
                }
             }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        if(checkPermissions()){
            if (isLocationEnabled()){
                googleMap.setMyLocationEnabled(true);
                getLastLocation();
            }
        }
        else{
            requestPermissions();
        }

        googleMap.setMinZoomPreference(17);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.clear();

                MarkerOptions markerOptions = new MarkerOptions();

                if(myPosition != null && latLng != null){
                    float[] distance = new float[1];
                    Location.distanceBetween(myPosition.latitude, myPosition.longitude,latLng.latitude, latLng.longitude, distance);
                    String total = (Integer.toString((int) (distance[0] * YARDS_TO_METERS)));
                    markerOptions.title(total);
                    markerOptions.draggable(true);

                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.add(myPosition, latLng).width(2).color(Color.rgb(0, 255, 0));
                    googleMap.addPolyline(polylineOptions);
                }

                markerOptions.position(latLng);
                markerOptions.icon(createBitmapFromView(getActivity(), R.drawable.ic_greenbullseye)).anchor(0.5f, 0.5f);

                googleMap.addMarker(markerOptions).showInfoWindow();
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
    }

    private void requestPermissions(){
//        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQ_CODE);
//        } else {
//            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                locationEnabled = true;
//
////                if(!(googleMap == null)){
////                    googleMap.setMyLocationEnabled(true);
////                }
//
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListenerGPS);
//            }
//            else{
//                new AlertDialog.Builder(getContext())
//                        .setTitle("GPS not enabled")
//                        .setMessage("Please enable location services and turn on location to enable GPS tracking.")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS ));
//                            }
//                        })
//                        .show();
//            }
//
//        }
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQ_CODE);
    }

    private boolean checkPermissions(){
        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
            else {
                new AlertDialog.Builder(getContext())
                        .setTitle("GPS/Location Services not enabled")
                        .setMessage("Please enable location services and turn on location to enable GPS course assistance.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 200);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.popBackStack();
                            }
                        })
                        .show();

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 0){
            switch(requestCode){
                case 200:
                    if(checkPermissions()){
                        if (isLocationEnabled()){
                            googleMap.setMyLocationEnabled(true);
                            getLastLocation();
                        }
                    }
                    else{
                        requestPermissions();
                    }
                    break;
            }
        }
     }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void getLastLocation(){
        if(checkPermissions()){
            if(isLocationEnabled()){
                if(googleMap != null){
                    googleMap.setMyLocationEnabled(true);
                }
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();

                        if(location == null){
                            requestNewLocationData();
                        }else{
                            myPosition = new LatLng(location.getLatitude(), location.getLongitude());
                            updateUI();
                        }
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Unable to get location - permission required", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private void requestNewLocationData(){
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(1000);
        //mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            myPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            updateUI();
        }
    };

    private void updateUI(){
        float[] distanceFront = new float[1];
        float[] distanceCenter = new float[1];
        float[] distanceBack = new float[1];

        if(round != null && currentHole != null && myPosition != null){
            Location.distanceBetween(myPosition.latitude, myPosition.longitude,round.getHole(currentHole).getFrontLat(), round.getHole(currentHole).getFrontLong(), distanceFront);
            Location.distanceBetween(myPosition.latitude, myPosition.longitude,round.getHole(currentHole).getMiddleLat(), round.getHole(currentHole).getMiddleLong(), distanceCenter);
            Location.distanceBetween(myPosition.latitude, myPosition.longitude,round.getHole(currentHole).getBackLat(), round.getHole(currentHole).getBackLong(), distanceBack);
            txtFront.setText(String.format("%s", (int) (distanceFront[0] * YARDS_TO_METERS)));
            txtCenter.setText(String.format("%s", (int) (distanceCenter[0] * YARDS_TO_METERS)));
            txtBack.setText(String.format("%s", (int) (distanceBack[0] * YARDS_TO_METERS)));

            String holeString = "";

            switch (currentHole + 1){
                case 1:
                    holeString = "" + (currentHole + 1) + "st";
                    break;
                case 2:
                    holeString = "" + (currentHole + 1) + "nd";
                    break;
                case 3:
                    holeString = "" + (currentHole + 1) + "rd";
                    break;
                default:
                    holeString = "" + (currentHole + 1) + "th";
                    break;
            }

            txtHoleNum.setText(holeString);
            txtParNum.setText(String.format("%s", round.getHole(currentHole).getPar()));
            txtYardsNum.setText(String.format("%s", round.getHole(currentHole).getYards()));

            if(googleMap != null && myPosition != null){
                LatLng boundsUpper = new LatLng(round.getHole(currentHole).getBackLat(), round.getHole(currentHole).getBackLong());
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(boundsUpper).zoom(17).bearing(0).tilt(45).build()));
            }
        }
    }

    private BitmapDescriptor createBitmapFromView(Context context, int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
