package com.example.aawaazme;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class SplashScreen extends Activity{
	
	private Thread mSplashThread;
	   @Override
	   public void onCreate(Bundle savedInstanceState){
	      super.onCreate(savedInstanceState);
	      // set the content view for your splash screen you defined in an xml file
	      setContentView(R.layout.splashscreen);

	      // perform other stuff you need to do
	      final SplashScreen sPlashScreen = this;   
	        
	        // The thread to wait for splash screen events
	        mSplashThread =  new Thread(){
	            @SuppressWarnings("deprecation")
				@Override
	            public void run(){
	                try {
	                    synchronized(this){
	                        // Wait given period of time or exit on touch
	                        wait(5000);
	                    }
	                }
	                catch(InterruptedException ex){                    
	                }

	                finish();
	                
	                // Run next activity
	                Intent intent = new Intent();
	                intent.setClass(sPlashScreen, StreamingFeed.class);
	                startActivity(intent);
                      finish();
	            }
	        };
	        
	        mSplashThread.start();  
	     
	   }

	   private class AsyncLoadXMLFeed extends AsyncTask<Void, Void, Void>{
	      @Override
	      protected void onPreExecute(){
	            // show your progress dialog

	      }

	      @Override
	      protected Void doInBackground(Void... voids){
			return null;
	            // load your xml feed asynchronously
	      }

	      @Override
	      protected void onPostExecute(Void params){
	            // dismiss your dialog
	            // launch your News activity
	    	  startActivity(new Intent(SplashScreen.this,StreamingFeed.class));

	            // close this activity
	            finish();
	      }

	   }
	}