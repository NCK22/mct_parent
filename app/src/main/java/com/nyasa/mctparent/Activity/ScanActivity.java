package com.nyasa.mctparent.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nyasa.mctparent.APIClient;
import com.nyasa.mctparent.Interface.getChildListInterface;
import com.nyasa.mctparent.Interface.setScannedByInterface;
import com.nyasa.mctparent.Pojo.ChildPojoStudProf;
import com.nyasa.mctparent.Pojo.CommonParentPojo;
import com.nyasa.mctparent.Pojo.ParentPojoStudProf;
import com.nyasa.mctparent.R;
import com.nyasa.mctparent.Storage.SPProfile;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    Button startScanningButton;
    Button stopScanningButton;
    TextView peripheralTextView,toolbar_textView;
    ProgressDialog progressDialog;
    public static ArrayList<String> list_macId=new ArrayList<String>();
    public static ArrayList<ChildPojoStudProf> mListItem=new ArrayList<ChildPojoStudProf>();
    SPProfile spCustProfile;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            // auto scroll for text view
            final int scrollAmount = peripheralTextView.getLayout().getLineTop(peripheralTextView.getLineCount()) - peripheralTextView.getHeight();
            // if there is no need to scroll, scrollAmount will be <=0
            if (scrollAmount > 0)
                peripheralTextView.scrollTo(0, scrollAmount);

            HashMap<String, Integer> txPowerLookupTable = new HashMap<String, Integer>();
            txPowerLookupTable.put(result.getDevice().getAddress(), new Integer(result.getRssi()));

            String macAddress = result.getDevice().getAddress();
            Integer txPower = txPowerLookupTable.get(macAddress);

            Log.e("txPower", "" + txPower);
            Log.e("Rssi", "" + result.getRssi());


//            𝑅𝑆𝑆𝐼𝑠𝑚𝑜𝑜𝑡h = 𝛼 ∗ 𝑅𝑆𝑆𝐼𝑛 + (1 − 𝛼) ∗ 𝑅𝑆𝑆𝐼𝑛−1

            Log.e("distance", "" + getDistance(result.getRssi(), txPower));
           /* if(!result.getDevice().getAddress().equalsIgnoreCase(""))
            {*/
            Log.e("macIdsize",""+list_macId.size());
            if(list_macId.isEmpty()|| !list_macId.contains(result.getDevice().getAddress())) {
                list_macId.add(result.getDevice().getAddress());
                peripheralTextView.append("MAC ADDRESS: " + result.getDevice().getAddress() + "\nRSSI: " + result.getRssi() + "\nBondState: " + result.getDevice().getBondState() + "\nDistance: " + calculateDistance(result.getRssi()) + "\n-----------------------------------------\n");
                Log.e("mListItem size",""+mListItem.size());
                for(int i=0;i<mListItem.size();i++)
                {
                    Log.e("MACId",mListItem.get(i).getMac_id());
                    if(mListItem.get(i).getMac_id().equalsIgnoreCase(result.getDevice().getAddress()))
                        mListItem.get(i).setFound("true");
                }
            }
            // }

            //if(!peripheralTextView.getText().toString().contains(result.getDevice().getAddress()))
         //   if(!list_macId.contains(result.getDevice().getAddress()))
                //peripheralTextView.append("MAC ADDRESS: " + result.getDevice().getAddress() + "\nRSSI: " + result.getRssi() + "\nBondState: " + result.getDevice().getBondState() + "\nDistance: " + calculateDistance(result.getRssi()) + "\n-----------------------------------------\n");

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        peripheralTextView = (TextView) findViewById(R.id.PeripheralTextView);
        peripheralTextView.setMovementMethod(new ScrollingMovementMethod());

        spCustProfile=new SPProfile(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar_textView=(TextView)findViewById(R.id.toolbar_title);
        toolbar_textView.setText("Scan");
        findViewById(R.id.icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        if(list_macId!=null)
            list_macId.clear();

        startScanningButton = (Button) findViewById(R.id.StartScanButton);
        startScanningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startScanning();
            }
        });

        stopScanningButton = (Button) findViewById(R.id.StopScanButton);
        stopScanningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopScanning();
            }
        });
        stopScanningButton.setVisibility(View.INVISIBLE);

        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();


        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        // Make sure we have access coarse location enabled, if not, prompt the user to enable it
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This app needs location access");
            builder.setMessage("Please grant location access so this app can detect peripherals.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
            });
            builder.show();
        }

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.getMenu().clear(); //clear old inflated items.
        navigationView.inflateMenu(R.menu.menu_drawer);
        navigationView.setCheckedItem(R.id.menu_go_scan);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        //  setHeader();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBarDrawerToggle.syncState();


        Log.e("SPProfilephoto",spCustProfile.getProfilePhotoPath());


        getStudentList();
    }

    double getDistance(int rssi, int txPower) {
        /*
         * RSSI = TxPower - 10 * n * lg(d)
         * n = 2 (in free space)
         *
         * d = 10 ^ ((TxPower - RSSI) / (10 * n))
         */

        return Math.pow(10d, ((double) txPower - rssi) / (10 * 2));
    }


    double calculateDistance(int rssi) {

        int txPower = -59; //hard coded power value. Usually ranges between -59 to -65

        if (rssi == 0) {
            return -1.0;
        }

        double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            double distance = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            return distance;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("coarse location permission granted");
                   // startService(new Intent(MainActivity.this, TrackLocService.class));
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }

            case 2: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // {Some Code}

                    //startService(new Intent(MainActivity.this, TrackLocService.class));
                }
            }
            break;

           /* case 3:
                Log.e("test", "onActivityResult");
                if(gps_enabled)
                    finish();
                else
                    isLocationEnabled();
                // broadcastFlag=false;
                break;
*/



        }
    }

    public void startScanning() {
        if(list_macId!=null)
            list_macId.clear();
        System.out.println("start scanning");
        peripheralTextView.setText("");
        startScanningButton.setVisibility(View.INVISIBLE);
        stopScanningButton.setVisibility(View.VISIBLE);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                if(btScanner!=null)
                btScanner.startScan(leScanCallback);
                else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ScanActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            stopScanningButton.setVisibility(View.INVISIBLE);
                            startScanningButton.setVisibility(View.VISIBLE);
                        }
                    });
                }
                  //  Toast.makeText(ScanActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void stopScanning() {
        System.out.println("stopping scanning");
        peripheralTextView.append("Stopped Scanning");
        startScanningButton.setVisibility(View.VISIBLE);
        stopScanningButton.setVisibility(View.INVISIBLE);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.stopScan(leScanCallback);
            }
        });

        for(int i=0;i<mListItem.size();i++)
        {
            if(mListItem.get(i).getFound().equalsIgnoreCase("true"))
                //  setChildStatusActive(mListItem.get(i).getChild_id(),mListItem.get(i).getChildMacID());
                setScannedBy(mListItem.get(i).getMac_id());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //isLocationEnabled();
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }*/



    private void logout() {

        new android.support.v7.app.AlertDialog.Builder(ScanActivity.this)
                .setTitle(getString(R.string.menu_logout))
                .setMessage(getString(R.string.logout_msg))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //MyApp.saveIsLogin(false);
                        spCustProfile.setIsLogin("false");
                        spCustProfile.setProfilePhotoPath("");
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                //  .setIcon(R.drawable.ic_logout)
                .show();
    }

    public void getStudentList(){

        progressDialog.show();


        getChildListInterface getResponse = APIClient.getClient().create(getChildListInterface.class);
        Call<ParentPojoStudProf> call = getResponse.doGetListResources(spCustProfile.getParent_id());
        call.enqueue(new Callback<ParentPojoStudProf>() {
            @Override
            public void onResponse(Call<ParentPojoStudProf> call, Response<ParentPojoStudProf> response) {

                Log.e("Inside","onResponse");
                // Log.e("response body",response.body().getStatus());
                //Log.e("response body",response.body().getMsg());
                ParentPojoStudProf parentPojoStudProf =response.body();
                if(parentPojoStudProf !=null){
                    if(parentPojoStudProf.getStatus().equalsIgnoreCase("true")){
                        mListItem=parentPojoStudProf.getObjProfile();

                        Log.e("Response","Success");



                        //      Log.e("objsize", ""+ parentPojoProfile.getObjProfile().size());

                        //setHeader();

                    }
                }
                else
                    Log.e("parentpojotabwhome","null");
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ParentPojoStudProf> call, Throwable t) {

                Log.e("throwable",""+t);
                progressDialog.dismiss();
            }
        });

    }

    public void setScannedBy(String child_mac_id){

        progressDialog.show();


        setScannedByInterface getResponse = APIClient.getClient().create(setScannedByInterface.class);
        Call<CommonParentPojo> call = getResponse.doGetListResources(child_mac_id,spCustProfile.getParent_id(),"parent");
        call.enqueue(new Callback<CommonParentPojo>() {
            @Override
            public void onResponse(Call<CommonParentPojo> call, Response<CommonParentPojo> response) {

                Log.e("Inside","onResponse");
                // Log.e("response body",response.body().getStatus());
                //Log.e("response body",response.body().getMsg());
                CommonParentPojo CommonParentPojo =response.body();
                if(CommonParentPojo !=null){
                    if(CommonParentPojo.getStatus().equalsIgnoreCase("true")){

                        Log.e("Response","Success");



                        //      Log.e("objsize", ""+ parentPojoProfile.getObjProfile().size());

                        //setHeader();

                    }
                }
                else
                    Log.e("parentpojotabwhome","null");
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CommonParentPojo> call, Throwable t) {

                Log.e("throwable",""+t);
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.menu_go_home:
                //   toolbar.setTitle(getString(R.string.menu_home));
                startActivity(new Intent(ScanActivity.this, TabViewHomeActivity.class));
                return true;


            case R.id.menu_go_profile:
//                toolbar.setTitle(getString(R.string.menu_matches));
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class).putExtra("tabFlag","profile"));
                finish();
                return true;

            case R.id.menu_go_logout:
                logout();
                return true;


            case R.id.menu_go_scan:
                startActivity(new Intent(getApplicationContext(),ScanActivity.class).putExtra("tabFlag","profile"));
                //finish();
                return true;



            case R.id.menu_go_stud_prof:
                startActivity(new Intent(getApplicationContext(),StudentListActivity.class).putExtra("tabFlag","profile"));
                finish();
                return true;

        }
        return false;
    }
}
