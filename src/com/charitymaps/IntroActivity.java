package com.charitymaps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class IntroActivity extends Activity implements View.OnClickListener {

	Button button_maps;
	Button button_overzicht;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		button_maps = (Button) findViewById(R.id.button_maps);
        button_maps.setOnClickListener(this);
        
        button_overzicht = (Button) findViewById(R.id.button_overzicht);
        button_overzicht.setOnClickListener(this);
	}

	@Override
    public void onClick(View v) {
    	switch (v.getId()) {
    	case R.id.button_maps:
    		startActivity(new Intent("com.charitymaps.MainActivity"));
    		break;
    	case R.id.button_overzicht:
				startActivity(new Intent("com.charitymaps.AllProductsActivity"));
				break;
    	}
    }
}