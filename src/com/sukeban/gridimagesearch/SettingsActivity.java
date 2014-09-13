package com.sukeban.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity {
	private Settings settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		settings = (Settings)getIntent().getSerializableExtra("settings");
		// pull out the arguments from the intent
	}
	
	public void onSave(View v) {
		
		Spinner spSizeValue = (Spinner)findViewById(R.id.spImageSize);
		String sizeValue = spSizeValue.getSelectedItem().toString();

		Spinner spColorValue = (Spinner)findViewById(R.id.spColorFilter);
		String colorValue = spColorValue.getSelectedItem().toString();

		Spinner spTypeValue = (Spinner)findViewById(R.id.spImageType);
		String typeValue = spTypeValue.getSelectedItem().toString();

		EditText etValue = (EditText)findViewById(R.id.etSiteFilter);
		String siteFilter = etValue.getText().toString();
		
		Intent i = new Intent();
		settings.imageSize = sizeValue;
		settings.colorFilter = colorValue;
		settings.imageType = typeValue;
		settings.siteFilter = siteFilter;
		
		i.putExtra("settings", settings);
		setResult(RESULT_OK,i);
		finish();
	}
}
