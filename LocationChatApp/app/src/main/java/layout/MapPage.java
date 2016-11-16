package layout;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.widget.Toast;

import com.app.seniorproject.mainseniorprojectpart.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MapPage extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mMap;

    protected GoogleApiClient mGoogleApiClient;

    public String userMarkTextInput = "User Input will go here";

    protected Location mLastLocation;
    private LocationRequest mLocationRequest;
    double longitudeValue;
    double latitudeValue;
    int markerCount = 1;


    ArrayList<MarkerOptions> markers = new ArrayList<>();

    public MapPage() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mapView = inflater.inflate(R.layout.fragment_main, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }


        buildGoogleApiClient();

        return mapView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setFloatingActionButton();
        setMaps();
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);

//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.8977216, -97.4878854), 14.0f));

        mMap = googleMap;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
//                Snackbar.make(view, "premission not granted", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        } else {
            mMap.setMyLocationEnabled(true);
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void setFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Going to add map functions here", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                try {

//                    location = mMap.getMyLocation();
                    // this method will work but gonna test with a non deprecated method

                    longitudeValue = mLastLocation.getLongitude();
                    System.out.println("Longitude: " + longitudeValue);
                    latitudeValue = mLastLocation.getLatitude();
                    System.out.println("Latitude: " + latitudeValue);


                    if(longitudeValue == 0.0 || latitudeValue == 0.0){

                        System.out.println("Can't get Location");

                    } else {
                        LatLng lats = new LatLng(latitudeValue, longitudeValue);

                        MarkerOptions marker = new MarkerOptions().position(lats).title(userMarkTextInput + " " + Integer.toString(markerCount));
                        markers.add(marker);
                        markerCount++;
                        mMap.addMarker(marker);
                    }

//                    LatLng lat = new LatLng(location.getLatitude(), location.getLongitude());
//                    System.out.println("Latitude " + location.getLatitude());
//                    MarkerOptions marker = new MarkerOptions().position(lat).title("Marker" + Integer.toString(markerCount));

//                    markerCount++;
//                    mMap.addMarker(marker);
                } catch (Exception e) {
                    System.out.println("Floating Action Button messed up");
                }

            }
        });
    }

    public void setMaps() {
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentByTag("map");

        if (fragment == null) {
            fragment = new SupportMapFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.mapFragmentContainer, fragment, "map");
            ft.commit();
            fm.executePendingTransactions();
        }
        fragment.getMapAsync(this);

    }

    // this will return true if the user allows the app to use user location
    // if the user denies it then it wont be allowed to hone in on the user
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


}
