package com.nyasa.mctparent.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.nyasa.mctparent.APIClient;
import com.nyasa.mctparent.Fragment.TabChildTrack;
import com.nyasa.mctparent.Interface.getChildListInterface;
import com.nyasa.mctparent.Pojo.ChildPojoStudProf;
import com.nyasa.mctparent.Pojo.ParentPojoStudProf;
import com.nyasa.mctparent.R;
import com.nyasa.mctparent.Storage.SPProfile;

import java.util.ArrayList;

import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nyasa.mctparent.R.id.btn_track;

public class TabViewHomeActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private TabViewHomeActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    String quotes[];
    TextView bottomText;
    Toolbar toolbar;
    TextView toolbar_textView;
    DrawerLayout drawerLayout;
    private NavigationView navigationView;
    BottomNavigationView bottmNavView;
    TextView testtv;
    TabLayout tabLayout;
    SPProfile spCustProfile;
    ProgressDialog progressDialog;
    public static ArrayList<ChildPojoStudProf> list_child=new ArrayList<ChildPojoStudProf>();


    Intent intent;
    Bundle bundle;
    String tabFlag="home";
    int noOfTabs=1;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_view_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_textView=(TextView)findViewById(R.id.toolbar_title);
        toolbar.setTitle("");
        toolbar_textView.setText("Home");
     //   toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //toolbar.setNavigationIcon(null);
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

        progressDialog=new ProgressDialog(this);
        spCustProfile=new SPProfile(this);
        tabLayout=(TabLayout)findViewById(R.id.tl_parent);
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setTabTextColors(Color.WHITE,Color.WHITE);


        intent=getIntent();

        bundle = new Bundle();
        tabFlag=intent.getStringExtra("tabFlag");
        getStudentList();

        //   Tabs Activity

       /* mSectionsPagerAdapter = new TabViewHomeActivity.SectionsPagerAdapter(getSupportFragmentManager(),noOfTabs);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.vp_parent);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        for (int i = 0; i < noOfTabs; i++) {
            Log.e("tab",""+i);
            tabLayout.addTab(tabLayout.newTab().setText("Child " + String.valueOf(i + 1)));
        }

*/
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.getMenu().clear(); //clear old inflated items.
        navigationView.inflateMenu(R.menu.menu_drawer);
        navigationView.setCheckedItem(R.id.menu_go_home);
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
       //if(spCustProfile.getProfilePhotoPath().equalsIgnoreCase(""))
           //getProfile();


        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This app needs location access");
            builder.setMessage("Please grant location access so this app can detect peripherals.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ActivityCompat.requestPermissions(TabViewHomeActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            });
            builder.show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("coarse location permission granted");


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
        }
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {


     /*   Log.e("title", String.valueOf(tab.getText()));
        if(list_child.get(tab.getPosition()).getStatus().equalsIgnoreCase("0"))
        btn=tab.getCustomView().findViewById(btn_track);
        btn.setEnabled(false);*/

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        item.setChecked(true);
        drawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.menu_go_home:
             //   toolbar.setTitle(getString(R.string.menu_home));
                startActivity(new Intent(TabViewHomeActivity.this, TabViewHomeActivity.class));
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
                finish();
                return true;


            case R.id.menu_go_stud_prof:
                startActivity(new Intent(getApplicationContext(),StudentListActivity.class).putExtra("tabFlag","profile"));
                finish();
                return true;

        }
            return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.e("called","onPageScrolled");
        if(position>noOfTabs)
            return;
    }

    @Override
    public void onPageSelected(int position) {
        Log.e("called","onPageSelected");
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.e("called","onPageScrollStateChanged");
    }


    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        int mNumOfTabs;
        Fragment fragment = null;

        public SectionsPagerAdapter(FragmentManager fm,int mNumOfTabs) {
            super(fm);
            this.mNumOfTabs=mNumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            Log.e("position", String.valueOf(position));
            Log.e("mNumOfTab", "" + mNumOfTabs);

            for (int i = 0; i <= mNumOfTabs ; i++) {
                if (i == position) {
                    fragment = new TabChildTrack();
                    Bundle bundle=new Bundle();
                /*    if(list_child.get(i).getStatus().equalsIgnoreCase("0"))
                        bundle.putString("status","inactive");
                    else
                        bundle.putString("status","active");*/
                    bundle.putString("position", String.valueOf(position));
                    if(position<list_child.size())
                    bundle.putString("child_mac_id",list_child.get(position).getMac_id());
                    else
                        bundle.putString("child_mac_id","sample_mac_id");
                    fragment.setArguments(bundle);
                    return fragment;
                }
            }

            return null;


        }



        @Override
        public int getItemPosition(@NonNull Object object) {
            Log.e("positionItem",""+object);
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.

            return mNumOfTabs;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                startActivity(new Intent(getApplicationContext(),TabViewHomeActivity.class).putExtra("tabFlag","home"));
                break;


            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void logout() {

        new AlertDialog.Builder(TabViewHomeActivity.this)
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

//                Log.e("Inside","onResponse");
               // Log.e("response body",response.body().getStatus());
                //Log.e("response body",response.body().getMsg());
                ParentPojoStudProf parentPojoStudProf =response.body();
                if(parentPojoStudProf !=null){
                    if(parentPojoStudProf.getStatus().equalsIgnoreCase("true")){
                        list_child=parentPojoStudProf.getObjProfile();
                        noOfTabs=list_child.size();
                        Log.e("noOfTabs",""+noOfTabs);
                        Log.e("Response","Success");

                        mSectionsPagerAdapter = new TabViewHomeActivity.SectionsPagerAdapter(getSupportFragmentManager(),noOfTabs);
                        // Set up the ViewPager with the sections adapter.
                        mViewPager = (ViewPager) findViewById(R.id.vp_parent);
                        mViewPager.setAdapter(mSectionsPagerAdapter);
                        mViewPager.setOffscreenPageLimit(noOfTabs);
                        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                 //       mViewPager.addOnPageChangeListener(TabViewHomeActivity.this);
                        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


                        for (int i = 0; i < noOfTabs; i++) {
                            Log.e("tab",""+i);
//                                tabLayout.addTab(tabLayout.newTab().setText("Child " + String.valueOf(i + 1)));
                            tabLayout.addTab(tabLayout.newTab().setText(list_child.get(i).getName().substring(0,list_child.get(i).getName().indexOf(" "))));

                        }


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

   /* private void setHeader() {

            View header = navigationView.getHeaderView(0);
            TextView txtHeaderName = (TextView) header.findViewById(R.id.header_name);
            TextView txtHeaderEmail = (TextView) header.findViewById(R.id.header_email);
            final ShapedImageView imageUser = (ShapedImageView) header.findViewById(R.id.header_image);
            // txtHeaderName.setText(spCustProfile.get());
            txtHeaderEmail.setText("");
            imageUser.setImageResource(R.drawable.logo);


     *//*       header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent profile = new Intent(TabParentProfileActivity.this, TabParentProfileActivity.class);
                    profile.putExtra("id", MyApp.getUserId());
                    if (MyApp.getIsJobProvider())
                        profile.putExtra("p_type", "jp");
                    else
                        profile.putExtra("p_type", "js");
                    profile.putExtra("editFlag", "true");
                    profile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(profile);
                }
            });*//*

    }*/

    private void exitApp() {
        new AlertDialog.Builder(TabViewHomeActivity.this)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.exit_msg))
                //.setIcon(R.mipmap.ic_launcher_app)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        exitApp();
    }

}

