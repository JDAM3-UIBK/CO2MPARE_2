package com.co2mpare.fragments;

import code.Controller;
import code.LatLng;
import code.SettingsObject;
import com.co2mpare.R;
import com.co2mpare.MainActivity;
import com.co2mpare.MainActivity.PlaceholderFragment;
import com.co2mpare.SettingsActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;




public class Main extends Fragment implements LocationListener{
	
	private static final String ARG_SECTION_NUMBER = "section_number";
	
	ImageView gear,gps,favbtn,homebtn;
	static EditText fr,to;
	private LocationManager locationManager;
	LatLng loc;
	SettingsObject settings=SettingsObject.getInstance();
	Controller controller=Controller.getInstance();

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static Main newInstance(int sectionNumber) {
		Main fragment = new Main();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public Main() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		
		settings.retrieveFromStorage(getActivity());
		
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		
		gear=(ImageView)rootView.findViewById(R.id.imageView2);
		gps=(ImageView)rootView.findViewById(R.id.gpsbtn);
		fr=(EditText)rootView.findViewById(R.id.from);
		to=(EditText)rootView.findViewById(R.id.to);
		favbtn=(ImageView)rootView.findViewById(R.id.savefav);
		homebtn=(ImageView)rootView.findViewById(R.id.homebtn);
		
		
		fr.addTextChangedListener(new TextWatcher() {

	        @Override
	        public void afterTextChanged(Editable s) {
	            // TODO Auto-generated method stub
	        }

	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	    
	            MainActivity.fromto[0]=fr.getText().toString();
	            Log.e("Changed:",MainActivity.fromto[0]);
	        } 

	    });
		
		to.addTextChangedListener(new TextWatcher() {

	        @Override
	        public void afterTextChanged(Editable s) {
		         
	        }

	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	            
	            MainActivity.fromto[1]=to.getText().toString();
	            Log.e("Changed:",MainActivity.fromto[1]);
	        } 

	    });
			
			
		
		
		favbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(settings.getSizeOfFavRoutes()>0){
					
					//Favorisierte Routen: ALERTDIALOG:
					String[] favroutes=new String[settings.getSizeOfFavRoutes()];
					
					//Stringarray für die Dialog-Liste
					for(int i=0;i<settings.getSizeOfFavRoutes()-1;i++){
						favroutes[i]="From: "+settings.getStartPosOfFavRoutes(i)+", To: "+settings.getDestOfFavRoutes(i);
					}
					
			        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			        
			        builder.setTitle("Relog a favourite route:");
			        builder.setItems(favroutes, new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			            	fr.setText(settings.getStartPosOfFavRoutes(which));
			            	to.setText(settings.getDestOfFavRoutes(which));
			            	MainActivity.fromto[1]=to.getText().toString();
			            	MainActivity.fromto[0]=fr.getText().toString();
			                controller.createURL(MainActivity.fromto[0], MainActivity.fromto[2]);
			            }
			        });
				   builder.create();
				   builder.show();
					
				}else{
					Toast.makeText(getActivity(), "I am afraid you did not define favourite routes yet.", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		//Retrieve GPS Location
		gps.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getLocation();
				
			}
			
		});
		
		//Log Route from GPS->home
		homebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(settings.getMyHome().equals("")){
					Toast.makeText(getActivity(), "I am afraid you did not enter your home address yet. Open Settings to fetch.", Toast.LENGTH_LONG).show();
				}else{
					getLocation();
					to.setText(settings.getMyHome());
				}
				
			}
			
		});
		
		//Launch Settings
		gear.setOnClickListener(new OnClickListener(){

			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SettingsActivity.class);
			   
			    startActivity(intent);

			}
			
		});
		
		return rootView;
	}
	
	public boolean getTBEmpty(){
		if(fr.getText().toString().equals("")||to.getText().toString().equals("")){
			return true;
		}else{
			return false;
		}
	}
	
	public String[] getValues(){
		String[] values=new String[2];
		values[0]=fr.getText().toString();
		values[1]=to.getText().toString();
		return values;
	}
	
	public void getLocation(){
		Toast.makeText(getActivity(), "Retrieving GPS-Location - please wait", Toast.LENGTH_LONG).show();
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,30, 10, this);
        
        /********* After registration onLocationChanged method  ********/
        		
    }
     
    @Override
    public void onLocationChanged(Location location) {
            
        String str = location.getLatitude()+" "+location.getLongitude();
        this.fr.setText(str);
        loc=new LatLng(location.getLatitude(),location.getLongitude());
        MainActivity.loc=loc;
        locationManager.removeUpdates(this);
    }
 
    @Override
    public void onProviderDisabled(String provider) {
         
        /******** Called when User off Gps *********/
         
        Toast.makeText(getActivity(), "Gps turned off ", Toast.LENGTH_LONG).show();
    }
 
    @Override
    public void onProviderEnabled(String provider) {
         
        /******** Called when User on Gps  *********/
         
        Toast.makeText(getActivity(), "Gps turned on ", Toast.LENGTH_LONG).show();
    }

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}

