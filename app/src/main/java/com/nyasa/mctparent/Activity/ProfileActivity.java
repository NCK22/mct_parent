package com.nyasa.mctparent.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nyasa.mctparent.APIClient;
import com.nyasa.mctparent.Interface.getProfileInterface;
import com.nyasa.mctparent.Pojo.ParentPojoLogin;
import com.nyasa.mctparent.R;
import com.nyasa.mctparent.Storage.SPProfile;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView tvParentId,tvName,tvMacId,tvEmail,tvAddress,tvPhone,tvRelation,tvUName,tvPwd,tvStatus;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    private NavigationView navigationView;
    SPProfile spProfile;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        spProfile=new SPProfile(this);
        progressDialog=new ProgressDialog(this);

        tvParentId=(TextView)findViewById(R.id.tv_parent_id);
        tvName=(TextView)findViewById(R.id.tv_name);
        tvMacId=(TextView)findViewById(R.id.tv_mac_id);
        tvEmail=(TextView)findViewById(R.id.tv_email);
        tvAddress=(TextView)findViewById(R.id.tv_address);
        tvPhone=(TextView)findViewById(R.id.tv_phone);
        tvRelation=(TextView)findViewById(R.id.tv_relation);
        tvUName=(TextView)findViewById(R.id.tv_uname);
        tvPwd=(TextView)findViewById(R.id.tv_pwd);
        tvStatus=(TextView)findViewById(R.id.tv_status);


        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.getMenu().clear(); //clear old inflated items.
        navigationView.inflateMenu(R.menu.menu_drawer);
        navigationView.setCheckedItem(R.id.menu_go_profile);
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


        Intent intent=getIntent();
        try {
            /*if(intent!=null) {
                JSONObject jsonObject = new JSONObject(intent.getStringExtra("response"));
                Log.e("json", String.valueOf(jsonObject));
                Log.e("parent_id", jsonObject.getString("parent_id"));
                tvParentId.setText(jsonObject.getString("parent_id"));
                tvName.setText(jsonObject.getString("name"));
                tvMacId.setText(jsonObject.getString("mac_id"));
                tvEmail.setText(jsonObject.getString("email"));
                tvAddress.setText(jsonObject.getString("address"));
                tvPhone.setText(jsonObject.getString("phone"));
                tvRelation.setText(jsonObject.getString("relation"));
                tvUName.setText(jsonObject.getString("username"));
                tvPwd.setText(jsonObject.getString("password"));
                tvStatus.setText(jsonObject.getString("status"));
            }
*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        getProfile();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.menu_go_home:
                //   toolbar.setTitle(getString(R.string.menu_home));
                startActivity(new Intent(ProfileActivity.this, TabViewHomeActivity.class));
                return true;

            case R.id.menu_go_scan:
                startActivity(new Intent(getApplicationContext(),ScanActivity.class).putExtra("tabFlag","profile"));
                finish();
                return true;

            case R.id.menu_go_profile:
//                toolbar.setTitle(getString(R.string.menu_matches));
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class).putExtra("tabFlag","profile"));
                finish();
                return true;

            case R.id.menu_go_logout:
                logout();
                return true;

            case R.id.menu_go_stud_prof:
                startActivity(new Intent(getApplicationContext(),StudentListActivity.class).putExtra("tabFlag","profile"));
                finish();
                return true;

        }
        return false;
    }

    private void logout() {

        new AlertDialog.Builder(ProfileActivity.this)
                .setTitle(getString(R.string.menu_logout))
                .setMessage(getString(R.string.logout_msg))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //MyApp.saveIsLogin(false);
                        spProfile.setIsLogin("false");
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

    public void getProfile(){


        //checkValidity();

        //if(flagAllValid==true) {
            progressDialog.show();
            getProfileInterface getResponse = APIClient.getClient().create(getProfileInterface.class);
            Call<ParentPojoLogin> call = getResponse.doGetListResources(spProfile.getParent_id());
            call.enqueue(new Callback<ParentPojoLogin>() {
                @Override
                public void onResponse(Call<ParentPojoLogin> call, Response<ParentPojoLogin> response) {

                    Log.e("Inside", "onResponse");
               /* Log.e("response body",response.body().getStatus());
                Log.e("response body",response.body().getMsg());*/
                    ParentPojoLogin parentPojoLogin = response.body();
                    if (parentPojoLogin != null) {
                        if (parentPojoLogin.getStatus().equalsIgnoreCase("true")) {
                            Log.e("Response", parentPojoLogin.getStatus());
                            try {
                                Log.e("json response 1",""+parentPojoLogin.getObjProfile().get(0).get("parent_id"));

                                tvParentId.setText(parentPojoLogin.getObjProfile().get(0).get("parent_id"));
                                tvName.setText(parentPojoLogin.getObjProfile().get(0).get("name"));
                                tvMacId.setText(parentPojoLogin.getObjProfile().get(0).get("mac_id"));
                                tvEmail.setText(parentPojoLogin.getObjProfile().get(0).get("email"));
                                tvAddress.setText(parentPojoLogin.getObjProfile().get(0).get("address"));
                                tvPhone.setText(parentPojoLogin.getObjProfile().get(0).get("phone"));
                                tvRelation.setText(parentPojoLogin.getObjProfile().get(0).get("relation"));
                                tvUName.setText(parentPojoLogin.getObjProfile().get(0).get("username"));
                                tvPwd.setText(parentPojoLogin.getObjProfile().get(0).get("password"));
                                tvStatus.setText(parentPojoLogin.getObjProfile().get(0).get("status"));
                            } catch (Exception e) {
                                Log.e("exception",""+e);
                                e.printStackTrace();
                            }


                        }


                    }

                    showToast("Invalid username or Password");
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<ParentPojoLogin> call, Throwable t) {

                    Log.e("Throwabe ", "" + t);
                    progressDialog.dismiss();
                }
            });
        }

    public void showToast(String msg)
    {
        Toast.makeText(ProfileActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

}
