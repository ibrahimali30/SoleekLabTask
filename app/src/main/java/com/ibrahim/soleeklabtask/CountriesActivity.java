package com.ibrahim.soleeklabtask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CountriesActivity extends AppCompatActivity {
    private static final String TAG = "CountriesActivity";

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mFirebaseAuth;

    private ArrayList<Country> arrayList;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);
        Log.d(TAG, "onCreate: started");

        //start listen to user state
        setUpFireBaseListener();

        //initialize widgets
        mRecyclerView = findViewById(R.id.recyclerView);
        arrayList = new ArrayList<>();

        mRecyclerView.setHasFixedSize(true);

//        // use a linear layout manager
        LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        mAdapter = new RecyclerViewAdapter(arrayList  , this);
        mRecyclerView.setAdapter(mAdapter);






// Access the RequestQueue through your singleton class.
//        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    private void setUpFireBaseListener(){
        Log.d(TAG, "setUpFireBaseListener: setting up the auth state listener");

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                
                if (user != null){
                    Log.d(TAG, "onAuthStateChanged: signed in "+user.getUid());
                    
                }else {
                    Log.d(TAG, "onAuthStateChanged:signed out ");
                    Intent intent = new Intent(getApplicationContext() , MainActivity.class);
                    //reset activity life cycle
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: started");
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: activity attemping  stopped");

        //remove AuthState Listener
        if (mAuthStateListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String queryResult = sharedPreferences.getString("searchItem","");

        if (queryResult.equals("all")){
            getData("");
        }else {
            getData(queryResult);
        }

        Log.d(TAG, "onResume: "+queryResult);
        Log.d(TAG, "onCreate: search item - "+queryResult);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id==R.id.search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }
        if (id==R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getData(String search){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url;
        if (search.length()>0){
           url = " https://restcountries.eu/rest/v2/name/"+search;
        }else {
            url = "https://restcountries.eu/rest/v2/all";
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i =0 ; i<response.length() ; i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Country country = new Country(
                                jsonObject.getString("name"),
                                jsonObject.getString("flag"),
                                jsonObject.getString("capital"),
                                jsonObject.getString("subregion"));

                        arrayList.add(country);
                        mAdapter.notifyDataSetChanged();

//                        Log.d(TAG, "onResponse: country : "+country);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.d(TAG, "onResponse: --------"+response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: -------"+error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);

    }
}
