package com.example.gridimagesearch.activities;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.gridimagesearch.R;
import com.example.gridimagesearch.fragments.AdvancedSearchDialog;
import com.example.gridimagesearch.fragments.AdvancedSearchDialog.AdvancedSearchDialogListener;
import com.example.gridimagesearch.models.ImageResult;
import com.example.gridimagesearch.models.SearchCondition;
import com.example.gridimagesearch.widgets.EndlessScrollListener;
import com.example.gridimagesearch.widgets.ImageResultsAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;

public class SearchActivity extends FragmentActivity implements AdvancedSearchDialogListener {
    private EditText etQuery;
    private StaggeredGridView sgvResults;
    
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    
    private SearchCondition searchCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        
        searchCondition = new SearchCondition();
        
        handleIntent(getIntent());
        
        setupView();
        
        int margin = getResources().getDimensionPixelSize(R.dimen.margin);
        sgvResults.setItemMargin(margin); // set the GridView margin
        sgvResults.setPadding(margin, 0, margin, 0); // have the margin on the sides as well 

        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        sgvResults.setAdapter(aImageResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public void onFinishEditDialog(SearchCondition searchCondition) {
        this.searchCondition.setImageColor(searchCondition.getImageColor());
        this.searchCondition.setImageSize(searchCondition.getImageSize());
        this.searchCondition.setImageType(searchCondition.getImageType());
        this.searchCondition.setSite(searchCondition.getSite());
        imageResults.clear();
        aImageResults.clear();
        loadDataFromApi(0);
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            
            //use the query to search your data somehow
            searchCondition.setQuery(query);
            loadDataFromApi(0);
        }
    }
    
    private void loadDataFromApi(int start) {
        AsyncHttpClient client = new AsyncHttpClient();
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&start=" + start;
        if (searchCondition.getQuery() != null) 
            searchUrl += "&q=" + searchCondition.getQuery();
        
        if (searchCondition.getImageColor() != null) 
            searchUrl += "&imgcolor=" + searchCondition.getImageColor();
        
        if (searchCondition.getImageSize() != null) 
            searchUrl += "&imgsz=" + searchCondition.getImageSize();
        
        if (searchCondition.getImageType() != null) 
            searchUrl += "&imgtype=" + searchCondition.getImageType();
        
        if (searchCondition.getSite() != null) 
            searchUrl += "&as_sitesearch=" + searchCondition.getSite();
        
        Log.d("DEBUG", searchUrl);
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    //imageResults.clear(); // clear the existing images from the array (in case where its a new search)
                    //imageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                    //aImageResults.notifyDataSetChanged();
                    
                    // When you make to the adapter, it does modify the underlying data
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.i("INFO", response.toString());
                }
                //Log.i("INFO", imageResultsJson.toString());
            }
        });
    }

    private void setupView() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        sgvResults = (StaggeredGridView) findViewById(R.id.sgvResults);
        
        sgvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                if (page < 8)
                    loadDataFromApi(page * searchCondition.getSize());
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
        
        sgvResults.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(StaggeredGridView parent, View view, int position, long id) {
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                ImageResult result = imageResults.get(position);
                //i.putExtra("url", result.getFullUrl());
                i.putExtra("result", result);
                startActivity(i);
            }
        });
    }
    
    private void showAdvancedSearchDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AdvancedSearchDialog advancedSearchDialog = AdvancedSearchDialog.newInstance();
        advancedSearchDialog.show(fm, "fragment_advanced_search");
    }
    
    public void onAdvancedSearch(MenuItem mi) {
        Log.i("DEBUG", "aaa");
        showAdvancedSearchDialog();
    }
    
    public void onImageSearch(View v) {
        String query = etQuery.getText().toString();
        //Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        
        AsyncHttpClient client = new AsyncHttpClient();
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8&start=0";
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear(); // clear the existing images from the array (in case where its a new search)
                    //imageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                    //aImageResults.notifyDataSetChanged();
                    
                    // When you make to the adapter, it does modify the underlying data
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //Log.i("INFO", imageResultsJson.toString());
            }
        });
        
    }
}
