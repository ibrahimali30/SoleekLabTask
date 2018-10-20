package com.ibrahim.soleeklabtask;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;

public class SearchActivity extends AppCompatActivity {

    private SearchView mSearchView;
    private static final String TAG = "SearchActivity";
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serarch);
        Log.d(TAG, "onCreate: starts");
        intent = new Intent(getApplicationContext(),MainActivity.class);

        Log.d(TAG, "onCreate: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        mSearchView.setSearchableInfo(searchableInfo);
        mSearchView.setQueryHint("enter your tags");
//        Log.d(TAG, "onCreateOptionsMenu: component name "+getComponentName().toString());
//        Log.d(TAG, "onCreateOptionsMenu: hint is "+mSearchView.getQueryHint());
//        Log.d(TAG, "onCreateOptionsMenu: searchable info = "+searchableInfo.toString());

        mSearchView.setIconified(false);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(TAG, "onQueryTextSubmit: "+s);

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPreferences.edit().putString("searchItem",s).apply();
                mSearchView.clearFocus();
                finish();
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                startActivity(intent);
                return false;
            }
        });


        return true;
    }


}
