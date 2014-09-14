package com.sukeban.gridimagesearch.activities;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sukeban.gridimagesearch.R;
import com.sukeban.gridimagesearch.models.ImageResult;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class ImageDisplayActivity extends Activity {

	private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		getActionBar().hide();
		
		ImageResult result = (ImageResult)getIntent().getSerializableExtra("result");
		String url = result.fullUrl;
		ImageView imImageResult = (ImageView)findViewById(R.id.ivImageResult);
		
		imageLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getBaseContext())  
		.threadPriority(Thread.NORM_PRIORITY - 2)  
		.denyCacheImageMultipleSizesInMemory()  
		//.discCacheFileNameGenerator(new Md5FileNameGenerator())  
		.tasksProcessingOrder(QueueProcessingType.LIFO)  
		//.enableLogging()   
		.build();  
		ImageLoader.getInstance().init(config);
		
		imageLoader.displayImage(url, imImageResult);
		
		// TODO: display something while it loads not the launcher icon
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_display, menu);
		return true;
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
}
