package com.charitymaps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class Head extends Activity {
	
	public static final int SLEEP_TIME = 3; //wait for x seconds
	private static String TAG = Head.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes the title bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Removes notifications bar
		
		setContentView(R.layout.activity_main);
		
		//start the timer and lauch main activity
		IntentLauncher launcher = new IntentLauncher();
		launcher.start();
		}

	private class IntentLauncher extends Thread {
		
		@Override
		//Sleep for some time, then start a new activity
		public void run() {
			try {
				//sleeping
				Thread.sleep(SLEEP_TIME*1000);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
			
			//start main activity
			Intent intent = new Intent(Head.this, IntroActivity.class);
			Head.this.startActivity(intent);
			Head.this.finish();
		}
	}
	
}
