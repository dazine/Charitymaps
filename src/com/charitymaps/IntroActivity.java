package com.charitymaps;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.widget.ShareActionProvider;

public class IntroActivity extends SherlockActivity implements View.OnClickListener {

	private ShareActionProvider mShareActionProvider;

	Button button_maps;
	Button button_database;
	Button button_overzicht;
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        /** Inflating the current activity's menu with res/menu/items.xml */
	        getSupportMenuInflater().inflate(R.menu.items, menu);
	 
	        /** Getting the actionprovider associated with the menu item whose id is share */
	        mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.share).getActionProvider();
	 
	        /** Getting the target intent */
	        Intent intent = getDefaultShareIntent();
	 
	        /** Setting a share intent */
	        if(intent!=null)
	            mShareActionProvider.setShareIntent(intent);
	        return super.onCreateOptionsMenu(menu);
	 
	    }
	 
	    /** Returns a share intent */
	    private Intent getDefaultShareIntent(){
	 
	        Intent intent = new Intent(Intent.ACTION_SEND);
	        intent.setType("text/plain");
	        intent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
	        intent.putExtra(Intent.EXTRA_TEXT,"Sample Content !!!");
	        return intent;
	    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		button_maps = (Button) findViewById(R.id.button_maps);
        button_maps.setOnClickListener(this);
        
        button_database = (Button) findViewById(R.id.button_database);
        button_database.setOnClickListener(this);
        
        button_overzicht = (Button) findViewById(R.id.button_overzicht);
        button_overzicht.setOnClickListener(this);
	}

	@Override
    public void onClick(View v) {
    	switch (v.getId()) {
    	case R.id.button_maps:
    		startActivity(new Intent("com.charitymaps.MainActivity"));
    		break;
    	case R.id.button_database:
    		startActivity(new Intent(this, AllProductsActivity.class));
    		break;
    	case R.id.button_overzicht:
    		startActivity(new Intent(this, Overzicht.class));
    		break;
    	}
    	
    }
	
}