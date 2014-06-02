package com.co2mpare.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.co2mpare.MainActivity;
import com.co2mpare.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends Fragment {

	private static final String ARG_SECTION_NUMBER = "section_number4";

	 
	  private GoogleMap map;
	  static final LatLng Innsbruck=new LatLng(47.261408,11.427569);
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static Map newInstance(int sectionNumber) {
		Map fragment = new Map();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public Map() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_map, container,
				false);
				
		map = ((SupportMapFragment) MainActivity.getActivityInstance().getSupportFragmentManager().findFragmentById(R.id.gmap))
		        .getMap();
		    
		    if (map!=null){
		      Marker frommarker = map.addMarker(new MarkerOptions().position(Innsbruck)
		          .title(MainActivity.fromto[0]));
		      Marker tomarker = map.addMarker(new MarkerOptions()
		          .position(Innsbruck)
		          .title(MainActivity.fromto[1]));
		    }

		return rootView;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
	}
	
	
	
	@Override
	public void onDestroyView(){
		android.support.v4.app.FragmentManager fm = MainActivity.getActivityInstance().getSupportFragmentManager();

	    Fragment xmlFragment = fm.findFragmentById(R.id.gmap);
	    if (xmlFragment != null) {
	        fm.beginTransaction().remove(xmlFragment).commit();
	    }

	    super.onDestroyView();
	}
	
	
	public void setUpMaps(LatLng from, LatLng to){
		    if (map!=null){
		    	map.clear();
		    	
		      Marker frommarker = map.addMarker(new MarkerOptions().position(from)
		          .title("Start: "+MainActivity.fromto[0]));
		      Marker tomarker = map.addMarker(new MarkerOptions()
		          .position(to)
		          .title("Destination: "+MainActivity.fromto[1]));
		      
		      frommarker.showInfoWindow();
		      tomarker.showInfoWindow();
		      map.moveCamera(CameraUpdateFactory.newLatLngZoom(to, 15));
		      map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null); 
		    }
	}
	
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	  super.setUserVisibleHint(isVisibleToUser);
	  if (isVisibleToUser) {
		  LatLng llfrom=null;
		  String from="Innsbruck";
		  String to="Innsbruck";
		  if(!(MainActivity.fromto[0]==null)){
			  if(!(MainActivity.fromto[0].equals("Your current position"))){
				  from=MainActivity.fromto[0];
				  try {
						llfrom = new RetrieveData().execute(from).get();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			  }else{
				  llfrom=new LatLng(MainActivity.loc.getLat(),MainActivity.loc.getLng());
			  }
			  
		  }
		  if(!(MainActivity.fromto[1]==null)){
			  to=MainActivity.fromto[1];
		  }

		  LatLng llto=null;
		try {
			llto = new RetrieveData().execute(to).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  setUpMaps(llfrom,llto);
	  }
	  }
	
}

class RetrieveData extends AsyncTask<String, Void, LatLng> {
	
	@Override
    protected LatLng doInBackground(String... address) {
		String uri = "http://maps.google.com/maps/api/geocode/json?" +
	             "sensor=false&address=";
		
		try {
			uri += URLEncoder.encode(address[0], "UTF-8");
		} catch (UnsupportedEncodingException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		Log.e("URL:",uri);
	    URL url=null;
		try {
			url = new URL(uri);
		} catch (MalformedURLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		InputStream is=null;
		
		String jsonText;
		Scanner scan=null;
		try {
			scan = new Scanner(url.openStream());
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		jsonText = new String();
		   while (scan.hasNext())
		       jsonText += scan.nextLine();
		scan.close();
		JSONObject geodata = null;
		try {
			geodata = new JSONObject(jsonText);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    LatLng loc=null;
	   
	    try {
	    	
	        JSONObject res = geodata.getJSONArray("results").getJSONObject(0);

	        JSONObject jsonloc=res.getJSONObject("geometry").getJSONObject("location");
	        
	        double lat=jsonloc.getDouble("lat");
	        double lng=jsonloc.getDouble("lng");
	        
	        loc=new LatLng(lat,lng);
	    } catch (JSONException e) {
	        e.printStackTrace();
	    }
	    return loc;
		
        
	 }
	
	 
}

