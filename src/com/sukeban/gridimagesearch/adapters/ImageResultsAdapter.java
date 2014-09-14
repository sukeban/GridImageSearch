package com.sukeban.gridimagesearch.adapters;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sukeban.gridimagesearch.R;
import com.sukeban.gridimagesearch.models.ImageResult;

//import android.R;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageResultsAdapter extends
		ArrayAdapter<ImageResult> {
	private ImageLoader imageLoader;

	public ImageResultsAdapter(Context context, List<ImageResult> images) {
		super(context,R.layout.item_image_result,images);
		imageLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)  
		.threadPriority(Thread.NORM_PRIORITY - 2)  
		.denyCacheImageMultipleSizesInMemory()  
		//.discCacheFileNameGenerator(new Md5FileNameGenerator())  
		.tasksProcessingOrder(QueueProcessingType.LIFO)  
		//.enableLogging()   
		.build();  
		ImageLoader.getInstance().init(config); 
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ImageResult imageInfo = getItem(position);
		if (convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result,parent,false);
		}
		ImageView ivImage = (ImageView)convertView.findViewById(R.id.ivImage);
		TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
		
		ivImage.setImageResource(0);
		imageLoader.displayImage(imageInfo.thumbUrl, ivImage);
		
		tvTitle.setText(Html.fromHtml(imageInfo.title));
		
		return convertView;
	}

}
