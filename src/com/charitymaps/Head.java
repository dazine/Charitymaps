package com.charitymaps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class Head extends Activity implements View.OnClickListener {
	Button btn_verder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btn_verder = (Button) findViewById(R.id.button_enter);
        btn_verder.setOnClickListener(this);
	}

	@Override
    public void onClick(View v) {
    	switch (v.getId()) {
    	case R.id.button_enter:
    		startActivity(new Intent("com.charitymaps.IntroActivity"));
    		break;
    	}
    }

}
