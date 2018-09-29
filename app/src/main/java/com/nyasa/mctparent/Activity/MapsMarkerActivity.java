package com.nyasa.mctparent.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.nyasa.mctparent.APIClient;
import com.nyasa.mctparent.Interface.getChildListInterface;
import com.nyasa.mctparent.Interface.getDriverLocInterface;
import com.nyasa.mctparent.Pojo.ChildPojoLocation;
import com.nyasa.mctparent.Pojo.ParentPojoLocation;
import com.nyasa.mctparent.R;
import com.nyasa.mctparent.Storage.SPProfile;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsMarkerActivity extends AppCompatActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 60 * 1; // 1 minute
    Button btnFusedLocation;
    TextView tvLocation;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    GoogleMap googleMap;
    MapView mapView;
    ProgressDialog progressDialog;
    SPProfile spCustProfile;
    ArrayList<ChildPojoLocation> mListItem=new ArrayList<ChildPojoLocation>();
    Intent intent;
    Toolbar toolbar;
    TextView toolbar_textView;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_marker);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_textView=(TextView)findViewById(R.id.toolbar_title);
        toolbar.setTitle("");
        toolbar_textView.setText("Tracking");
       // toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        findViewById(R.id.icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        spCustProfile=new SPProfile(this);

       intent=getIntent();

        Log.d(TAG, "onCreate ...............................");
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
                this.mGoogleApiClient.connect();
        Log.e("After googleapiclient", String.valueOf(mGoogleApiClient.isConnected()));

       /* SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
//        googleMap = fm.getMapAsync((OnMapReadyCallback);this);
        googleMap=fm.getMapAsync();*/

        mapView = (MapView)findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        if(googleMap!=null)
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
      //  mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
      //  addMarker();
       // startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
            Log.d(TAG, "Location update started ..............: ");
            return;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        //addMarker();
    }

    private void addMarker(String lat,String lng) {

        MarkerOptions options = new MarkerOptions();
        // following four lines requires 'Google Maps Android API Utility Library'
        // https://developers.google.com/maps/documentation/android/utility/
        // I have used this to display the time as title for location markers
        // you can safely comment the following four lines but for this info
        IconGenerator iconFactory = new IconGenerator(this);
        iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
        options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mLastUpdateTime)));
        //options.icon(BitmapDescriptorFactory.fromResource(R.drawable.hand));
        options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        //LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        LatLng currentLatLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        options.position(currentLatLng);
        Marker mapMarker = googleMap.addMarker(options);
       /* long atTime = mCurrentLocation.getTime();
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));*/
        mapMarker.setTitle(String.valueOf(currentLatLng));
        Log.d(TAG, "Marker added.............................");
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                15));
        Log.d(TAG, "Zoom done.............................");
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        if(mGoogleApiClient!=null) {
            if(mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                Log.d(TAG, "Location update stopped .......................");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
           // startLocationUpdates();
           // addMarker();
            Log.d(TAG, "Location update resumed .....................");

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        getLocation(intent.getStringExtra("driver_id"));
    }

    public void getLocation(String driver_id){

        progressDialog.show();
Log.e("inside","getLocation");

        getDriverLocInterface getResponse = APIClient.getClient().create(getDriverLocInterface.class);
        Call<ParentPojoLocation> call = getResponse.doGetListResources(driver_id);
        call.enqueue(new Callback<ParentPojoLocation>() {
            @Override
            public void onResponse(Call<ParentPojoLocation> call, Response<ParentPojoLocation> response) {

                Log.e("Inside","onResponseGetLocation");
                // Log.e("response body",response.body().getStatus());
                //Log.e("response body",response.body().getMsg());
                ParentPojoLocation parentPojoLocation =response.body();
                if(parentPojoLocation !=null){
                    if(parentPojoLocation.getStatus().equalsIgnoreCase("true")){
                        mListItem=parentPojoLocation.getObjProfile();
                        //  noOfTabs=list_child.size();
                        Log.e("Response","Success");

                        addMarker(mListItem.get(mListItem.size()-1).getLatitude(),mListItem.get(mListItem.size()-1).getLongitude());

                        //addMarker(mListItem.get(2210).getLatitude(),mListItem.get(2210).getLongitude());

                        /*if(mListItem.size()>0){
                            for(int i=0;i<mListItem.size();i++)
                            addMarker(mListItem.get(0).getLatitude(),mListItem.get(0).getLongitude());
                        }*/

                        //      Log.e("objsize", ""+ parentPojoProfile.getObjProfile().size());

                        //setHeader();

                    }
                }
                else
                    Log.e("parentpojotabwhome","null");
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ParentPojoLocation> call, Throwable t) {

                Log.e("throwable",""+t);
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}