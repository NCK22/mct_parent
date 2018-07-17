package com.nyasa.mctparent.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.nyasa.mctparent.Activity.MapsMarkerActivity;

import com.nyasa.mctparent.R;
import com.nyasa.mctparent.Storage.SPProfile;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Dell on 15-01-2018.
 */


public class TabChildTrack extends Fragment {



    JSONArray eduArray;
    ArrayList<String> list_religion=new ArrayList<String>();
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    ProgressDialog progressDialog;
    ArrayAdapter aaCaste;
    Button  btnFind;
    EditText etAgeFrom,etAgeTo,etSearchId;
    RadioButton rbGroom,rbBride;
    String strGender="";
    FrameLayout Container;
    LinearLayout ll_search_info;
    boolean flagAllValid=false;
    SPProfile spCustProfile;
    Button btnTrack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.tab_child_track,container,false);

        btnTrack=(Button)rootView.findViewById(R.id.btn_track);
        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MapsMarkerActivity.class));
            }
        });
        /*Log.e("TabHome","onCreateView");

        spCustProfile=new SPProfile(getActivity());
       // Container=(FrameLayout)rootView.findViewById(R.id.Container);



        etAgeFrom=(EditText)rootView.findViewById(R.id.et_age_from);
        etAgeTo=(EditText)rootView.findViewById(R.id.et_age_to);
        etSearchId=(EditText)rootView.findViewById(R.id.et_search_id);
        btnFind=(Button)rootView.findViewById(R.id.btn_home_find);


        rbGroom=(RadioButton)rootView.findViewById(R.id.rb_groom);
        rbBride=(RadioButton)rootView.findViewById(R.id.rb_bride);
        rbGroom.setOnCheckedChangeListener(this);
        rbBride.setOnCheckedChangeListener(this);
        ll_search_info=(LinearLayout)rootView.findViewById(R.id.ll_search_info);

        etSearchId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(etSearchId.getText().toString().length()>0) {
                   rbBride.setEnabled(false);
                   rbGroom.setEnabled(false);
                   etAgeFrom.setEnabled(false);
                   etAgeTo.setEnabled(false);
                   spReligion.setEnabled(false);
                   spCaste.setEnabled(false);
                }
                else if(etSearchId.getText().toString().length()==0) {
                    rbBride.setEnabled(true);
                    rbGroom.setEnabled(true);
                    etAgeFrom.setEnabled(true);
                    etAgeTo.setEnabled(true);
                    spReligion.setEnabled(true);
                    spCaste.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnFind.setOnClickListener(this);
        spReligion.setOnItemSelectedListener(this);
        spCaste.setOnItemSelectedListener(this);

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait home");

        getReligionList();
       // displayData();
        getHighlightedProf();
        getMatches();
*/


        return rootView;

    }
   /* private void displayData() {
        Log.e("displayData","called");
        Log.e("List_highlight size",""+list_highlights.size());
        adapterHighLights = new HomeProfilesAdapter(getActivity(), list_highlights);
        rv_profile_highlight.setAdapter(adapterHighLights);
        adapterMatches = new HomeProfilesAdapter(getActivity(), list_matches);
        rv_profile_matches.setAdapter(adapterMatches);

        if (adapterMatches.getItemCount() == 0) {
  //          lyt_not_found.setVisibility(View.VISIBLE);
        } else {
//            lyt_not_found.setVisibility(View.GONE);
        }
    }

    public void getReligionList(){

        progressDialog.show();
        if(list_religion!=null)
            list_religion.clear();
        if(list_pojo_religion!=null)
            list_pojo_religion.clear();

        getReligionInterface getResponse = APIClient.getClient().create(getReligionInterface.class);
        Call<ParentPojoReligion> call = getResponse.doGetListResources();
        call.enqueue(new Callback<ParentPojoReligion>() {
            @Override
            public void onResponse(Call<ParentPojoReligion> call, Response<ParentPojoReligion> response) {

                Log.e("Inside","onResponse");
               *//* Log.e("response body",response.body().getStatus());
                Log.e("response body",response.body().getMsg());*//*
                ParentPojoReligion parentPojoReligion=response.body();
                if(parentPojoReligion!=null){
                    if(parentPojoReligion.getStatus().equalsIgnoreCase("1")){
                        Log.e("Response","Success");
                        Log.e("objsize", ""+parentPojoReligion.getObjReligion().size());

                        LinkedHashMap<String, ChildPojoReligion> resultMap =parentPojoReligion.getObjReligion();

                        Iterator<String> keys=resultMap.keySet().iterator();
                        while (keys.hasNext()){
                            String key=keys.next();

                                list_pojo_religion.add(resultMap.get(key));
                                list_religion.add(resultMap.get(key).getReligion_name());

                        }
                        Log.e("List Size",""+list_religion.size());
                        ArrayAdapter aaReligion = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,list_religion);
                        aaReligion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spReligion.setAdapter(aaReligion);
                        progressDialog.dismiss();
                    }
                }

            }

            @Override
            public void onFailure(Call<ParentPojoReligion> call, Throwable t) {

                progressDialog.dismiss();
            }
        });

    }


    public void getHighlightedProf()
    {

        progressDialog.show();
        if(list_highlights!=null)
            list_highlights.clear();

        getHighlightedInterface getResponse = APIClient.getClient().create(getHighlightedInterface.class);
        Call<ParentPojoProfile> call = getResponse.doGetListResources();
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
                        list_highlights= parentPojoProfile.getObjProfile();
                        if(list_highlights.size()!=0)

                            displayData();

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

    }

    public void getMatches()
    {

        progressDialog.show();
        if(list_matches!=null)
            list_matches.clear();

        getMatchesInterface getResponse = APIClient.getClient().create(getMatchesInterface.class);
        Call<ParentPojoProfile> call = getResponse.doGetListResources("7180214");
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
                        list_matches= parentPojoProfile.getObjProfile();
                        if(list_matches.size()!=0)

                            displayData();

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

    }

    public void basicSearch(String searchFlag)
    {

        progressDialog.show();
        if(list_search!=null)
            list_search.clear();
Log.e("searchFlag",searchFlag);
        searchBasicInterface getResponse = APIClient.getClient().create(searchBasicInterface.class);
        Call<ParentPojoProfile> call = null;
        if(searchFlag.equalsIgnoreCase("byid"))
            call=getResponse.searchById("7180214",etSearchId.getText().toString());
        else if(searchFlag.equalsIgnoreCase("basic")) 
            call = getResponse.searchBasic(spCustProfile.getMatrimonyId(),etAgeFrom.getText().toString(),etAgeTo.getText().toString(),
                list_pojo_religion.get(list_religion.indexOf(spReligion.getSelectedItem())).getReligion_id(),
                list_pojo_caste.get(list_caste.indexOf(spCaste.getSelectedItem())).getCaste_id(),strGender);
        
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
                        list_matches= parentPojoProfile.getObjProfile();
                        if(list_matches.size()!=0)

                            displayData();

                      // Container.setVisibility(View.VISIBLE);
                      *//*  Fragment someFragment = new TabSearchResult();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.Container, someFragment ); // give your fragment container id in first parameter
                        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                        transaction.commit();*//*
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

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        ((TextView)view).setTextColor(Color.BLACK);
        ((TextView)view).setTextSize(16);
        switch(adapterView.getId())
        {
            case R.id.sp_home_religion:
                Log.e("onIemSelectedListener ","religion called");
                Log.e("position clicked",""+spReligion.getSelectedItemPosition());
                if(spReligion.getSelectedItemPosition() >0)
                {
                   *//* Log.e("selected index",""+list_country.indexOf(spCountry.getSelectedItem()));
                    Log.e("pojo element name",list_pojo_country.get(list_country.indexOf(spCountry.getSelectedItem())).getCountry_name());
                    Log.e("pojo element id",list_pojo_country.get(list_country.indexOf(spCountry.getSelectedItem())).getCountry_id());
                    Log.e("pojo element sortname",list_pojo_country.get(list_country.indexOf(spCountry.getSelectedItem())).getCountry_currency());
                   // Log.e("pojo element phone",list_pojo_country.get(list_country.indexOf(spCountry.getSelectedItem())).getCountry_extension());*//*
                    getCasteList(list_pojo_religion.get(list_religion.indexOf(spReligion.getSelectedItem())).getReligion_id());
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void getCasteList(String religion_id){

        progressDialog.show();
        if(list_caste!=null)
            list_caste.clear();

        Log.e("Religion",religion_id);
        getCasteInterface getResponse = APIClient.getClient().create(getCasteInterface.class);
        Call<ParentPojoCaste> call = getResponse.doGetListResources(religion_id);
        call.enqueue(new Callback<ParentPojoCaste>() {
            @Override
            public void onResponse(Call<ParentPojoCaste> call, Response<ParentPojoCaste> response) {

                Log.e("Inside","onResponse");
               *//* Log.e("response body",response.body().getStatus());
                Log.e("response body",response.body().getMsg());*//*
                progressDialog.dismiss();
                ParentPojoCaste parentPojoCaste=response.body();
                if(parentPojoCaste!=null){
                    if(parentPojoCaste.getStatus().equalsIgnoreCase("1")){
                        Log.e("Response","Success");
                        Log.e("objsize", ""+parentPojoCaste.getObjCaste().size());

                        LinkedHashMap<String, ChildPojoCaste> resultMap =parentPojoCaste.getObjCaste();

                        Iterator<String> keys=resultMap.keySet().iterator();
                        while (keys.hasNext()){
                            String key=keys.next();

                            list_pojo_caste.add(resultMap.get(key));
                            list_caste.add(resultMap.get(key).getCaste_name());
                        }
                        Log.e("List Size",""+list_caste.size());
                        aaCaste = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,list_caste);
                        aaCaste.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spCaste.setAdapter(aaCaste);

                    }
                }

            }

            @Override
            public void onFailure(Call<ParentPojoCaste> call, Throwable t) {

                Log.e("throwable home",""+t);
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {

        checkValidity();
        if(flagAllValid==true) {
            if (etSearchId.getText().toString().length() > 0)
                basicSearch("byid");
            else
                basicSearch("basic");
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch(buttonView.getId()){

            case R.id.rb_bride:
                if(rbBride.isChecked())
                    strGender="female";
                    break;

            case R.id.rb_groom:
                if(rbGroom.isChecked())
                    strGender="male";
        }
    }

    public void checkValidity(){
        if(etSearchId.getText().toString().length()==0) {
            if (!rbGroom.isChecked() && !rbBride.isChecked())
                showToast("Please check Groom / Bride");
        }

    }

*/    public void showToast(String msg)
    {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }
}
