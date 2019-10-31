package com.example.krzkam.local;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import io.realm.Realm;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private GoogleMap mMap;
    private int PROXIMITY_RADIUS = 10000;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;
    private double latitude = 0;
    private double longitude = 0;
    private double endLat = 0;
    private double endLng = 0;
    private double dLat = 0;
    private double dLng = 0;
    private String destName = "";
    private String reference = "";
    private String delRef = "";
    private String type = "";
    private int first = 0;
    private boolean addClicked = false;


    BaseApplication base = new BaseApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        }
        else {
            Log.d("onCreate","Google Play Services available.");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        base.realm = Realm.getDefaultInstance();


    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //Disable Map Toolbar:
        mMap.getUiSettings().setMapToolbarEnabled(false);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        final Spinner mySpinner = (Spinner) findViewById(R.id.B_spinner);
        final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MapsActivity.this,
                R.layout.spinner_layout, getResources().getStringArray(R.array.spin));
        final Button btnMore = (Button) findViewById(R.id.B_more);
        btnMore.setVisibility(View.INVISIBLE);
        final Button btnGo = (Button) findViewById(R.id.B_go);
        btnGo.setVisibility(View.INVISIBLE);
        final Button btnDel = (Button) findViewById(R.id.B_delete);
        btnDel.setVisibility(View.INVISIBLE);
        final Button btnInsert = (Button) findViewById(R.id.B_insert);
        btnInsert.setVisibility(View.INVISIBLE);
        final Button btnAdd = (Button) findViewById(R.id.B_add);
        myAdapter.setDropDownViewResource(R.layout.spinner_layout);
        mySpinner.setAdapter(myAdapter);
        Button clear = (Button) findViewById(R.id.B_clear);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i==1)
                {
                    type = "hotel";
                    Log.d("onClick", "Button is Clicked");
                    choosedType(type, btnAdd, btnInsert);

                    //Toast.makeText(MapsActivity.this,"Nearby Hotels", Toast.LENGTH_SHORT).show();
                }
                if (i==2)
                {
                    type = "cafe";
                    Log.d("onClick", "Button is Clicked");
                    choosedType(type, btnAdd, btnInsert);
                    Toast.makeText(MapsActivity.this,"Nearby Cafe", Toast.LENGTH_SHORT).show();
                }
                if (i==3)
                {
                    type = "restaurant";
                    Log.d("onClick", "Button is Clicked");
                    choosedType(type, btnAdd, btnInsert);
                    Toast.makeText(MapsActivity.this,"Nearby Restaurants", Toast.LENGTH_SHORT).show();
                }
                if (i==4)
                {
                    type = "bar";
                    Log.d("onClick", "Button is Clicked");
                    choosedType(type, btnAdd, btnInsert);
                    Toast.makeText(MapsActivity.this,"Nearby Bars", Toast.LENGTH_SHORT).show();
                }
                if (i==5)
                {
                    type = "supermarket";
                    Log.d("onClick", "Button is Clicked");
                    choosedType(type, btnAdd, btnInsert);
                    Toast.makeText(MapsActivity.this,"Nearby Supermarkets", Toast.LENGTH_SHORT).show();
                }
                if (i==6)
                {
                    type = "gas_station";
                    Log.d("onClick", "Button is Clicked");
                    choosedType(type, btnAdd, btnInsert);
                    Toast.makeText(MapsActivity.this,"Nearby Gas Stations", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (endLat != 0 && endLng != 0) {
                    mMap.clear();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(endLat, endLng));
                    markerOptions.title(destName);
                    markerOptions.snippet(reference);
                    reference = markerOptions.getSnippet();
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.go);
                    markerOptions.icon(icon);
                    mMap.addMarker(markerOptions);
                    Object[] DataTransfer = new Object[3];
                    String url = getDirectionsUrl();
                    GetDirectionsData getDirectionsData = new GetDirectionsData();
                    DataTransfer[0] = mMap;
                    DataTransfer[1] = url;
                    DataTransfer[2] = new LatLng(endLat, endLng);
                    getDirectionsData.execute(DataTransfer);
                }
                else
                {
                    Toast.makeText(MapsActivity.this,"You didnt choose place", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endLat != 0 && endLng != 0) {
                    //String aa = String.valueOf(reference.length());

                    Intent more = new Intent(MapsActivity.this, More_Info.class);
                    more.putExtra("reference", reference);
                    startActivity(more);


                }
                else
                {
                    Toast.makeText(MapsActivity.this,"You didnt choose place", Toast.LENGTH_SHORT).show();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                btnGo.setVisibility(View.INVISIBLE);
                btnMore.setVisibility(View.INVISIBLE);
                mySpinner.setVisibility(View.VISIBLE);
                mySpinner.setAdapter(myAdapter);
                btnAdd.setVisibility(View.VISIBLE);
                btnInsert.setVisibility(View.INVISIBLE);
                btnDel.setVisibility(View.INVISIBLE);
                first=0;
                addClicked = false;
                onLocationChanged(mLastLocation);



            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClicked = true;
                Toast.makeText(MapsActivity.this,"Drag and drop to location which you'd like to add", Toast.LENGTH_SHORT).show();
                btnInsert.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.INVISIBLE);
                mySpinner.setVisibility(View.INVISIBLE);
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                base.deletePlace(delRef);
                choosedType(type,btnAdd,btnInsert);
                btnDel.setVisibility(View.INVISIBLE);
                Toast.makeText(MapsActivity.this,"Place has been deleted", Toast.LENGTH_SHORT).show();
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inLat="";
                String inLng="";

                if (mCurrLocationMarker != null) {
                    inLat = String.valueOf(mCurrLocationMarker.getPosition().latitude);
                    inLng = String.valueOf(mCurrLocationMarker.getPosition().longitude);
                    //Toast.makeText(MapsActivity.this,inLat+" "+inLng, Toast.LENGTH_SHORT).show();
                    if (inLat != "" && inLng != "" )
                    {
                        Intent insert = new Intent(MapsActivity.this, InsertActivity.class);
                        insert.putExtra("inLat", inLat.toString());
                        insert.putExtra("inLng", inLng.toString());
                        startActivity(insert);
                    }
                    else
                        Toast.makeText(MapsActivity.this,"Error", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MapsActivity.this,"Error", Toast.LENGTH_SHORT).show();


                mMap.clear();
                btnAdd.setVisibility(View.VISIBLE);
                btnInsert.setVisibility(View.INVISIBLE);
                addClicked = false;
                mySpinner.setVisibility(View.VISIBLE);
                onLocationChanged(mLastLocation);
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                dLat = mCurrLocationMarker.getPosition().latitude;
                dLng = mCurrLocationMarker.getPosition().longitude;
            }
        });

        if (mMap != null)
        {
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    View v = getLayoutInflater().inflate(R.layout.info_window, null);
                    TextView tvLocality = (TextView)v.findViewById(R.id.tv_locality);
                    tvLocality.setText(marker.getTitle());

                    reference = marker.getSnippet();
                    endLat = marker.getPosition().latitude;
                    endLng = marker.getPosition().longitude;
                    destName = marker.getTitle();
                    if (endLng != longitude && endLat != latitude)
                    {
                        btnDel.setVisibility(View.INVISIBLE);
                        delRef = marker.getSnippet();
                        if (delRef.length() == 20){
                            btnDel.setVisibility(View.VISIBLE);

                        }
                        btnGo.setVisibility(View.VISIBLE);
                        btnMore.setVisibility(View.VISIBLE);

                    }
                    return v;
                }
            });
        }
    }

    private String getDirectionsUrl()
    {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin=" + latitude + "," + longitude);
        googleDirectionsUrl.append("&destination="+endLat+","+endLng);
        googleDirectionsUrl.append("&key="+"AIzaSyDrLcCxy1sIPZjduDJQWnNf1D97no6aMV8");
        return googleDirectionsUrl.toString();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyAuQEQltaikY4W0MWQwjD2-yB0VPMh-Eds");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        if (addClicked == true) return;

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.draggable(true);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.placeholder_25);
        markerOptions.icon(icon);
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        if (first == 0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude, longitude));
            first = 1;
        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void choosedType(String type, Button btnAdd, Button btnInsert){
        mMap.clear();
        String url = getUrl(latitude, longitude, type);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        Log.d("onClick", url);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        btnAdd.setVisibility(View.INVISIBLE);
        btnInsert.setVisibility(View.INVISIBLE);
        getNearbyPlacesData.execute(DataTransfer);

        if (latitude != 0 && longitude != 0){
            base.refresh_views(mMap,type,latitude,longitude,PROXIMITY_RADIUS);
        }
    }




}