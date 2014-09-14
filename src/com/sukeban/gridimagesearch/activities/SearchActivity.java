package com.sukeban.gridimagesearch.activities;

import java.util.ArrayList;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sukeban.gridimagesearch.EndlessGridViewScrollListener;
import com.sukeban.gridimagesearch.R;
import com.sukeban.gridimagesearch.SettingsActivity;
import com.sukeban.gridimagesearch.adapters.ImageResultsAdapter;
import com.sukeban.gridimagesearch.models.ImageResult;
import com.sukeban.gridimagesearch.models.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;


public class SearchActivity extends Activity {
	
	private Settings settings;
	private EditText etSearch;
	private GridView gvResults;
	private EndlessGridViewScrollListener listener;
	private ArrayList<ImageResult> imageResults;
	private ImageResultsAdapter aImageResults;
	private Context toastContext;
	private short pageCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        
        setupViews();
        
        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        gvResults.setAdapter(aImageResults);
        listener = new EndlessGridViewScrollListener();
        listener.setSearchActivity(this);
        gvResults.setOnScrollListener(listener);

        settings = new Settings();
        settings.imageSize = "";
        settings.colorFilter = "";
        settings.imageType = "";
        settings.siteFilter = "";
        
        toastContext = this;
        
        pageCounter = 0;
    }
    
    private void setupViews() {
    	etSearch = (EditText)findViewById(R.id.etSearch);
    	gvResults = (GridView)findViewById(R.id.gvResults);
    	gvResults.setOnItemClickListener(new OnItemClickListener(){
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    			Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
    			ImageResult result = imageResults.get(position);
    			i.putExtra("result", result);
    			startActivity(i);
    		}
    	});
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
     MenuInflater inflater = getMenuInflater();
     inflater.inflate(R.menu.searchmenu, menu);
     return true;
    } 
    
    public void onAddItem(MenuItem m) {
    	Intent i = new Intent(this, SettingsActivity.class);    	
    	i.putExtra("settings", settings);
    	startActivityForResult(i,5);// make it a constant
    }
    
    public void performSearch(final boolean clear) {

    	if (clear == true){
    		pageCounter = 0;
    	}
    	
    	String query = etSearch.getText().toString();
    	if (query.isEmpty()){
    		return;
    	}
    	   	
        String imageSize = settings.imageSize;
        String colorFiler = settings.colorFilter;
        String imageType = settings.imageType;
        String siteFilter = settings.siteFilter;
        
        short maxQuerySize = 8;
        short maxQueryPages = 9;

        String searchUrl = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=" + maxQuerySize;
        if (!imageSize.isEmpty()){
        	String imageSizeAddition = "&imgsz=" + imageSize;
        	searchUrl += imageSizeAddition;
        }
        if (!colorFiler.isEmpty()){
        	String colorFilterAddition = "&imgcolor=" + colorFiler;
        	searchUrl += colorFilterAddition;
        }
        if (!imageType.isEmpty()){
        	String imageTypeAddition = "&imgtype=" + imageType;
        	searchUrl += imageTypeAddition;
        }
        if (!siteFilter.isEmpty()){
        	String siteFilterAddition = "&as_sitesearch="+ siteFilter;
        	searchUrl += siteFilterAddition;
        }
       
        if (pageCounter > maxQuerySize*maxQueryPages){ // TODO: or no more results returned from API
			Toast.makeText(toastContext, "No more results", Toast.LENGTH_SHORT).show();
        	return;
        }
        String pageAddition = "&start=" + pageCounter;
        searchUrl += pageAddition;
                
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(searchUrl, new JsonHttpResponseHandler(){
        	@Override
        	public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response){
        		//Log.d("DEBUG",response.toString());
        		JSONArray imageResultsJson = null;
        		try {
        			
        			JSONObject responseData = response.getJSONObject("responseData");
        			if (responseData == null){
						Toast.makeText(toastContext, response.toString(), Toast.LENGTH_SHORT).show();
        				return;
        			}
        			
        			imageResultsJson = responseData.getJSONArray("results");
        			if (clear == true){
        			 	imageResults.clear();
        			}
        			aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
        			pageCounter+=imageResultsJson.length();
        		 
        		} catch (JSONException e){
        			e.printStackTrace();
        		}
        		//Log.i("INFO", imageResults.toString());
        	}  
        	
        	@Override
        	 public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
    	    	Toast.makeText(toastContext, errorResponse.toString(), Toast.LENGTH_SHORT).show();
        		//Log.d("DEBUG",errorResponse.toString());
        	}
        	
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == 5){
    		if (resultCode == RESULT_OK){
    			Settings settings = (Settings) data.getSerializableExtra("settings");
    			this.settings = settings;
    	    	Toast.makeText(toastContext, "Saved", Toast.LENGTH_SHORT).show();
    			this.performSearch(true);
    		}
    	}
    }
    
    public void getMore() {
    	this.performSearch(false);
    }
    
    public void onImageSearch(View v) {
    	this.performSearch(true);
    	// TODO: hitting return from the TextView crashes app
    }
}
