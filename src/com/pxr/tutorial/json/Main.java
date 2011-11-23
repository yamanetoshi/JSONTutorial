package com.pxr.tutorial.json;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.pxr.tutorial.xmltest.R;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Main extends ListActivity {
	private JSONObject mJson;
	private ArrayList<HashMap<String, String>> mylist;
	private Handler mHandler = new Handler();
	private ProgressDialog mProgressDialog;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listplaceholder);
        
        mylist = new ArrayList<HashMap<String, String>>();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Please wait ...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();

        new Thread(new Runnable() {
			@Override
			public void run() {
		        //mJson = JSONfunctions.getJSONfromURL("http://api.geonames.org/earthquakesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&username=demo");
		        mJson = JSONfunctions.getJSONfromURL("http://webservice.recruit.co.jp/hotpepper/gourmet/v1/?key=40788ee7b80b9835&lat=34.67&lng=135.52&range=5&order=4&format=json");
		        mHandler.post(new ListSetting());
			}      	
        }).start();          
    }

    private class ListSetting implements Runnable {
    	@Override
    	public void run() {
    		try{
	        	
    			JSONObject result = mJson.getJSONObject("results");
    			JSONArray  earthquakes = result.getJSONArray("shop");
	        	
    			for(int i=0;i<earthquakes.length();i++){						
    				HashMap<String, String> map = new HashMap<String, String>();	
    				JSONObject e = earthquakes.getJSONObject(i);
					
    				map.put("id",  String.valueOf(i));
    				map.put("iid", e.getString("id"));
    				map.put("name", e.getString("name"));
    				map.put("lat", e.getString("lat"));
    				map.put("lng", e.getString("lng"));
    				mylist.add(map);			
    			}		
    		}catch(JSONException e)        {
    			Log.e("log_tag", "Error parsing data "+e.toString());
    		}
	        
    		ListAdapter adapter = new SimpleAdapter(Main.this, mylist , R.layout.main, 
    				new String[] { "name", "iid" }, 
    				new int[] { R.id.item_title, R.id.item_subtitle });
	        
    		setListAdapter(adapter);
	        
    		final ListView lv = getListView();
    		lv.setTextFilterEnabled(true);	
    		lv.setOnItemClickListener(new OnItemClickListener() {
    			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {        		
    				@SuppressWarnings("unchecked")
    				HashMap<String, String> o = (HashMap<String, String>) lv.getItemAtPosition(position);	        		
    				Toast.makeText(Main.this, "ID '" + o.get("name") + "' was clicked.", Toast.LENGTH_SHORT).show(); 

    			}
    		});		
    		mProgressDialog.dismiss();
    	}
    }
}