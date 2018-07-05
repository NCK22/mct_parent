package com.nyasa.mctparent.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.nyasa.mctparent.Fragment.TabChildTrack;
import com.nyasa.mctparent.R;
import com.nyasa.mctparent.Storage.SPProfile;

import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabViewHomeActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

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


    Intent intent;
    Bundle bundle;
    String tabFlag="home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_view_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        progressDialog=new ProgressDialog(this);
        spCustProfile=new SPProfile(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        tabLayout=(TabLayout)findViewById(R.id.tl_parent);
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setTabTextColors(Color.BLACK,Color.WHITE);


        intent=getIntent();

        bundle = new Bundle();
        tabFlag=intent.getStringExtra("tabFlag");


        //   Tabs Activity

        mSectionsPagerAdapter = new TabViewHomeActivity.SectionsPagerAdapter(getSupportFragmentManager(),4);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.vp_parent);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_parent);

        mViewPager.setOffscreenPageLimit(0);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        for (int i = 0; i < 4; i++) {
            tabLayout.addTab(tabLayout.newTab().setText("Child " + String.valueOf(i + 1)));
        }


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
        actionBarDrawerToggle.syncState();


    Log.e("SPProfilephoto",spCustProfile.getProfilePhotoPath());
       //if(spCustProfile.getProfilePhotoPath().equalsIgnoreCase(""))
           //getProfile();

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {


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

        }
            return false;
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
            //Log.e("positi", "" + tabLayout.getSelectedTabPosition());

            for (int i = 0; i < mNumOfTabs ; i++) {
                if (i == position) {
                    fragment = new TabChildTrack();
                    break;
                }
            }
            return fragment;



        }



        @Override
        public int getItemPosition(@NonNull Object object) {
            Log.e("positionItem",""+object);
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.

            return 4;

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

 /*   public void getProfile(){

        progressDialog.show();


        getProfileInterface getResponse = APIClient.getClient().create(getProfileInterface.class);
        Call<ParentPojoProfile> call = getResponse.doGetListResources(spCustProfile.getMatrimonyId());
        call.enqueue(new Callback<ParentPojoProfile>() {
            @Override
            public void onResponse(Call<ParentPojoProfile> call, Response<ParentPojoProfile> response) {

                Log.e("Inside","onResponse");
               *//* Log.e("response body",response.body().getStatus());
                Log.e("response body",response.body().getMsg());*//*
                ParentPojoProfile parentPojoProfile =response.body();
                if(parentPojoProfile !=null){
                    if(parentPojoProfile.getStatus().equalsIgnoreCase("1")){
                        Log.e("Response","Success");
                        Log.e("objsize", ""+ parentPojoProfile.getObjProfile().size());
                        if(parentPojoProfile.getObjProfile().get(0).getProfile_photo()!=null) {
                            Log.e("profile_photo res", parentPojoProfile.getObjProfile().get(0).getProfile_photo());
                            spCustProfile.setProfilePhotoPath(parentPojoProfile.getObjProfile().get(0).getProfile_photo());
                        }
                       setHeader();

                    }
                }
                else
                    Log.e("parentpojotabwhome","null");
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ParentPojoProfile> call, Throwable t) {

                Log.e("throwable",""+t);
                progressDialog.dismiss();
            }
        });

    }*/

   /* private void setHeader() {
        if (spCustProfile.getIsLogin().equalsIgnoreCase("true")) {
            View header = navigationView.getHeaderView(0);
            TextView txtHeaderName = (TextView) header.findViewById(R.id.header_name);
            TextView txtHeaderEmail = (TextView) header.findViewById(R.id.header_email);
            final ShapedImageView imageUser = (ShapedImageView) header.findViewById(R.id.header_image);
            // txtHeaderName.setText(spCustProfile.get());
            txtHeaderEmail.setText(spCustProfile.getEmail());

            if(spCustProfile.getProfilePhotoPath()!=null) {
                Log.e("profile_photo","http://applex360.in/Deshpande-family/Matrimony-web/" + spCustProfile.getProfilePhotoPath());
                Picasso.with(this).load("http://applex360.in/Deshpande-family/Matrimony-web/" + spCustProfile.getProfilePhotoPath())
                        //.placeholder(R.drawable.placeholder)
                        .into(imageUser);
            }
            header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    *//*Intent profile = new Intent(TabParentProfileActivity.this, TabParentProfileActivity.class);
                    profile.putExtra("id", MyApp.getUserId());
                    if (MyApp.getIsJobProvider())
                        profile.putExtra("p_type", "jp");
                    else
                        profile.putExtra("p_type", "js");
                    profile.putExtra("editFlag", "true");
                    profile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(profile);*//*
                }
            });
        }
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

