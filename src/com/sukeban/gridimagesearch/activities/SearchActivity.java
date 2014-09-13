package com.sukeban.gridimagesearch.activities;

import java.util.ArrayList;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sukeban.gridimagesearch.R;
import com.sukeban.gridimagesearch.SettingsActivity;
import com.sukeban.gridimagesearch.R.id;
import com.sukeban.gridimagesearch.R.layout;
import com.sukeban.gridimagesearch.R.menu;
import com.sukeban.gridimagesearch.adapters.ImageResultsAdapter;
import com.sukeban.gridimagesearch.models.ImageResult;
import com.sukeban.gridimagesearch.models.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;


public class SearchActivity extends Activity {
	
	private Settings settings;
	private EditText etSearch;
	private GridView gvResults;
	private ArrayList<ImageResult> imageResults;
	private ImageResultsAdapter aImageResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        
        setupViews();
        
        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        gvResults.setAdapter(aImageResults);

        settings = new Settings();
        settings.imageSize = "Small";
        settings.colorFilter = "Red";
        settings.imageType = "Face";
        settings.siteFilter = "";
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == 5){
    		if (resultCode == RESULT_OK){
    			Settings settings = (Settings) data.getSerializableExtra("settings");
    			this.settings = settings;
    			
    			// settings view dismisssed 
    			// TODO: get the settings values and use them in search
    			
    	    	Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    		}
    	}
    }
    
    
    public void onImageSearch(View v) {
    	String query = etSearch.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
    	
        // TODO: get the settings values and use them in search    	

        String searchUrl = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8";
        client.get(searchUrl, new JsonHttpResponseHandler(){
        	@Override
        	public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response){
        		//Log.d("DEBUG",response.toString());
        		JSONArray imageResultsJson = null;
        		try {
        		 imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
        		 imageResults.clear();
        		 aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
        		} catch (JSONException e){
        			e.printStackTrace();
        		}
        		
        		//Log.i("INFO", imageResults.toString());
        	}        	
        });
		

    }
}
