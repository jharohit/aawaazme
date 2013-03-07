package com.example.aawaazme;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class TwitterLiveStreamingFragment extends Fragment {
	
	private List<HashMap<String,String>> mTweets = new ArrayList<HashMap<String,String>>();
	private SimpleAdapter mAdapter;
	private boolean mKeepRunning = true;
	private String mSearchTerm = "#india";
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

		super.onCreateView(inflater,container,savedInstanceState);
        View rootView = inflater.inflate(R.layout.twitter_streaming_api, container, false);
        
        mAdapter = new SimpleAdapter(this.getActivity(), mTweets, android.R.layout.simple_list_item_2, new String[] {"Tweet", "From"}, new int[] {android.R.id.text1, android.R.id.text2});
	    
        ListView listView = (ListView)rootView.findViewById(R.id.Tweets); 
	    listView.setAdapter(mAdapter);
	    
	    if(mSearchTerm.length() > 0){
	    	new StreamTask().execute();
	    	mKeepRunning=true;
	    }else{
	    	mKeepRunning=false;
	    }
	    
	    return rootView;
	}
	public class StreamTask extends AsyncTask<Integer, Integer, Integer> {
	    	
	    	private String mUrl = "https://stream.twitter.com/1/statuses/filter.json?track=";

	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			Credentials creds = new UsernamePasswordCredentials("aawaazme", "emzaawaa");
			client.getCredentialsProvider().setCredentials( new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), creds);
			HttpGet request = new HttpGet();
			request.setURI(new URI("https://stream.twitter.com/1/statuses/filter.json?track=" + mSearchTerm));
			HttpResponse response = client.execute(request);
			InputStream in = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader( new InputStreamReader(in) );
			
			parseTweets(reader);
			
			in.close();  
			
			} 
		catch (Exception e) {
			Log.e("Twitter", "doInBackground_" + e.toString());
			}
		return new Integer(1);
		}
			
			
	private void parseTweets( BufferedReader reader ) {
		try {
			String line = "";
			do {
				line = reader.readLine();
				Log.d("Twitter", "Keep Running: " + mKeepRunning
						+ " Line: " + line);
				JSONObject tweet = new JSONObject(line);
				HashMap<String, String> tweetMap = new HashMap<String, String>();
				if (tweet.has("text")) {
					tweetMap.put("Tweet", tweet.getString("text"));
					tweetMap.put("From", tweet.getJSONObject("user")
							.getString("screen_name"));
					mTweets.add(0, tweetMap);
					if (mTweets.size() > 10) {
						mTweets.remove(mTweets.size() - 1);
						}
					//mAdapter.notifyDataSetChanged();
					publishProgress(1);
					}
				} while (mKeepRunning && line.length() > 0);
			} 
		catch (Exception e) {
			// TODO: handle exception
			}
		}
			
			
			
			protected void onProgressUpdate(Integer... progress) {
				mAdapter.notifyDataSetChanged();
				}
			
			@Override
			protected void onPostExecute(Integer i) {
				
				}
	    	
     	} 
        
     
       
}


