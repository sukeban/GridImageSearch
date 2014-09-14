package com.sukeban.gridimagesearch;

import com.sukeban.gridimagesearch.activities.SearchActivity;

import android.util.Log;

public class EndlessGridViewScrollListener extends EndlessScrollListener {
	
	private SearchActivity searchActivity;
	
	public void setSearchActivity(SearchActivity activity) {
		this.searchActivity = activity;
	}

	@Override
	public void onLoadMore(int page, int totalItemsCount) {
		this.searchActivity.getMore();		
		Log.d("DEBUG","here");

	}

}
