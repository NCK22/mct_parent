package com.nyasa.mctparent.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nyasa.mctparent.APIClient;
import com.nyasa.mctparent.Adapter.StudentAdapter;
import com.nyasa.mctparent.Interface.getChildListInterface;
import com.nyasa.mctparent.Pojo.ChildPojoStudProf;
import com.nyasa.mctparent.Pojo.ParentPojoStudProf;
import com.nyasa.mctparent.R;
import com.nyasa.mctparent.Storage.SPProfile;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;*/

public class StudentListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<ChildPojoStudProf> mListItem;
    public RecyclerView recyclerView;
    StudentAdapter adapter;
    private ProgressBar progressBar;
    EditText skill, loc, quali;
    Button search;
    private LinearLayout lyt_not_found, lyt_search;
    String Id;
    SPProfile spCustProfile;

    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    Toolbar toolbar;
    FloatingActionButton fabSearch;
    ProgressDialog progressDialog;
    // AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        spCustProfile=new SPProfile(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mListItem = new ArrayList<ChildPojoStudProf>();

        recyclerView = (RecyclerView) findViewById(R.id.vertical_courses_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        fragmentManager = getSupportFragmentManager();

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


        getStudentList();


}


    private void logout() {

        new AlertDialog.Builder(StudentListActivity.this)
                .setTitle(getString(R.string.menu_logout))
                .setMessage(getString(R.string.logout_msg))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //MyApp.saveIsLogin(false);
                        //   spProfile.setIsLogin("false");
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


    private void displayData() {
        adapter = new StudentAdapter(StudentListActivity.this, mListItem);

        recyclerView.setAdapter(adapter);

        /*if (adapter.getItemCount() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }*/
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
                      //  noOfTabs=list_child.size();
                        Log.e("Response","Success");

                    if(mListItem.size()>0){
                        displayData();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.menu_go_home:
                //   toolbar.setTitle(getString(R.string.menu_home));
                startActivity(new Intent(StudentListActivity.this, TabViewHomeActivity.class));
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
}




/*
    public void getSavedSeekerList(){


        String url="";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        if(mListItem!=null)
            mListItem.clear();
        url  = "http://applex360.in/Deshpande-family/Job-web/app/app_get_savedapplications.php";

        Log.e("URL", url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(MainScreen.this, response , Toast.LENGTH_SHORT).show();
                        Log.e("Response",response);

                        try {
                            JSONObject jsonObj = new JSONObject(response.toString());

                            if(jsonObj.getString("RESPONSE_STATUS").equals("1")) {

                                pDialog.dismiss();
                                JSONObject objOuter=jsonObj.getJSONObject("Saved_Applications");
                                Log.e("jsonobj response",""+jsonObj);

                                Iterator x = objOuter.keys();
                                //  Log.e("obj outer",""+);
                                JSONArray arrayJson=objOuter.getJSONArray("1");
                                for(int i=0;i<arrayJson.length();i++){
                                JSONObject objJson;

                                    objJson = arrayJson.getJSONObject(i);
                                    Log.e("objinner",""+objJson);
                                    ItemUser objItem = new ItemUser();
                                    objItem.setUserId(objJson.getString("id"));
                                    objItem.setUserName(objJson.getString("name"));
                                    objItem.setUserEmail(objJson.getString(Constant.USER_EMAIL));
                                    objItem.setUserPhone(objJson.getString("mobile"));
                                    objItem.setUserCity(objJson.getString("location"));
                                    objItem.setUserImage("abc");
                                    objItem.setUserResume("abc");
                                    objItem.setUserSkills(objJson.getString("skills"));
                                    objItem.setUserType(objJson.getString("type"));
                                    mListItem.add(objItem);
                                }

                                Log.e("list size",""+mListItem.size());
                                displayData();

                            }

                            else if(jsonObj.getString("RESPONSE_STATUS").equals("3")||
                                    jsonObj.getString("RESPONSE_STATUS").equals("2")||
                                    jsonObj.getString("RESPONSE_STATUS").equals("0")){

                                pDialog.dismiss();
                                // showToast(jsonObj.getString("RESPONSE_MSG"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        pDialog.dismiss();
                        error.printStackTrace();
                        //Toast.makeText(MainScreen.this,""+error , Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                    params.put("userid",MyApp.getUserId());

                *//*Log.e("userid",MyApp.getUserId());
                Log.e("password",newPwd.getText().toString());
                Log.e("cpassword",cnfrmPwd.getText().toString());
                Log.e("opassword",oldPwd.getText().toString());
*//*


                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }*/








