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
import android.widget.TextView;
import com.cheapassapps.tourbuddy.Models.Round;
import com.cheapassapps.tourbuddy.R;
import com.cheapassapps.tourbuddy.ViewModels.CurrentRoundViewModel;
import com.cheapassapps.tourbuddy.ViewModels.RoundGuiViewModel;
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
    //private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static int LOCATION_REQ_CODE = 123;
    private Round round = new Round();
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
        mapView = (MapView) view.findViewById(R.id.map);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        startMap();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(RoundGuiViewModel.class);

        mRoundViewModel = ViewModelProviders.of(this).get(CurrentRoundViewModel.class);
        mRoundViewModel.getCurrentRound().observe(this, new Observer<Round>(){
            @Override
            public void onChanged(Round currentRound) {
                if(currentRound.getHoleCount() > 0 && myPosition != null){
                    round = currentRound;

                    float[] distanceFront = new float[1];
                    float[] distanceCenter = new float[1];
                    float[] distanceBack = new float[1];
                    Location.distanceBetween(myPosition.latitude, myPosition.longitude,currentRound.getHole(0).getFrontLat(), currentRound.getHole(0).getFrontLong(), distanceFront);
                    Location.distanceBetween(myPosition.latitude, myPosition.longitude,currentRound.getHole(0).getMiddleLat(), currentRound.getHole(0).getMiddleLong(), distanceCenter);
                    Location.distanceBetween(myPosition.latitude, myPosition.longitude,currentRound.getHole(0).getBackLat(), currentRound.getHole(0).getBackLong(), distanceBack);
                    txtFront.setText(Integer.toString((int) (distanceFront[0] * YARDS_TO_METERS)));
                    txtCenter.setText(Integer.toString((int) (distanceCenter[0] * YARDS_TO_METERS)));
                    txtBack.setText(Integer.toString((int) (distanceBack[0] * YARDS_TO_METERS)));

                    if(googleMap != null){
                        LatLngBounds bounds = new LatLngBounds(myPosition, new LatLng(currentRound.getHole(0).getBackLat(), currentRound.getHole(0).getBackLong()));
                        googleMap.setLatLngBoundsForCameraTarget(bounds);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                    }
                }
             }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        //googleMap = map;
        //googleMap.setMyLocationEnabled(true);
        //googleMap.setMinZoomPreference(17);
        //googleMap.addMarker(new MarkerOptions().position(ny).title("NY"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        //googleMap.setLatLngBoundsForCameraTarget(bounds);
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap = map;
    }

    private void startMap(){
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

            float[] distanceFront = new float[1];
            float[] distanceCenter = new float[1];
            float[] distanceBack = new float[1];

            if(round.getHoleCount() > 0){
                Location.distanceBetween(latitude, longitude,round.getHole(0).getFrontLat(), round.getHole(0).getFrontLong(), distanceFront);
                Location.distanceBetween(latitude, longitude,round.getHole(0).getMiddleLat(), round.getHole(0).getMiddleLong(), distanceCenter);
                Location.distanceBetween(latitude, longitude,round.getHole(0).getBackLat(), round.getHole(0).getBackLong(), distanceBack);
                txtFront.setText(Integer.toString((int) (distanceFront[0] * YARDS_TO_METERS)));
                txtCenter.setText(Integer.toString((int) (distanceCenter[0] * YARDS_TO_METERS)));
                txtBack.setText(Integer.toString((int) (distanceBack[0] * YARDS_TO_METERS)));
            }

//            LatLngBounds bounds = new LatLngBounds(new LatLng(33.920536, -78.618403), new LatLng(33.922781, -78.617193));
//            googleMap.setLatLngBoundsForCameraTarget(bounds);
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            //Log.i("Location:", myPosition.toString() + Arrays.toString(distance));
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

//    @Override
//    public void onMapClick(LatLng latLng) {
//        googleMap.clear();
//        googleMap.addMarker(new MarkerOptions()
//                            .position(latLng)
//                            .title("Point A")
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_menu_send)));
//
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
//
//        myPosition = new LatLng(latitude, longitude);
//
//        float[] distanceFront = new float[1];
//        float[] distanceCenter = new float[1];
//        float[] distanceBack = new float[1];
////        Location.distanceBetween(latLng.latitude, latLng.longitude,33.922557, -78.617585, distanceFront);
////        Location.distanceBetween(latLng.latitude, latLng.longitude, 33.922647, -78.617465, distanceCenter);
////        Location.distanceBetween(latLng.latitude, latLng.longitude,33.922742, -78.617330, distanceBack);
//        Location.distanceBetween(33.919346, -78.618182,round.getHole(0).getFrontLat(), round.getHole(0).getFrontLong(), distanceFront);
//        Location.distanceBetween(33.919346, -78.618182,round.getHole(0).getMiddleLat(), round.getHole(0).getMiddleLong(), distanceCenter);
//        Location.distanceBetween(33.919346, -78.618182,round.getHole(0).getBackLat(), round.getHole(0).getBackLong(), distanceBack);
//        txtFront.setText(Integer.toString((int) (distanceFront[0] * YARDS_TO_METERS)));
//        txtCenter.setText(Integer.toString((int) (distanceCenter[0] * YARDS_TO_METERS)));
//        txtBack.setText(Integer.toString((int) (distanceBack[0] * YARDS_TO_METERS)));
//    }
}
