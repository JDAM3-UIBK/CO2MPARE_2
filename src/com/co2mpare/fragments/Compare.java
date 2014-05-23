package com.co2mpare.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import code.Controller;
import code.ListAdapter;
import code.Route;
import code.SettingsObject;

import com.co2mpare.MainActivity;
import com.co2mpare.R;

public class Compare extends Fragment {
	private static final String ARG_SECTION_NUMBER = "section_number";

	SettingsObject settings=SettingsObject.getInstance();
	Controller controller=Controller.getInstance();
	View rootView;
	int selectedPos=-1;
	ListAdapter la;
	ListView lv;
	ArrayList<Route> fromserver;
		
	public static Compare newInstance(int sectionNumber) {
		Compare fragment = new Compare();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public Compare() {
		
	}

	public void setRoutesFromServer(ArrayList<Route> routes){
		this.fromserver=routes;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_compare, container,
				false);
		
		Button confirmbtn=(Button) rootView.findViewById(R.id.cfbtn);
		Button savebtn=(Button) rootView.findViewById(R.id.savefav);
	
		
		
		
		
		//FOR TESTING ONLY//////////////////////////////////////////////////////////////
		String text[]={"Geyrstrasse","Suedring"};
		Route test1=new Route(0, 0, "Car", text, 10.0, 1.27, getCurrentDate());
		Route test2=new Route(0, 0, "Public", text, 4.7, 1.27, getCurrentDate());
		Route test3=new Route(0, 0, "Bike", text, 0.0, 0.0, getCurrentDate());
		/////////////////////////////////////////////////////////////////////////////
		
		fromserver = new ArrayList<Route>();
		
		
		///FOR TESTING ONLY////////////////////////////
		fromserver.add(test1);
		fromserver.add(test2);
		fromserver.add(test3);
		/////////////////////////////////////////////
		
		
		la=new ListAdapter(getActivity(),fromserver);
		
        lv = (ListView) rootView.findViewById(R.id.listView1);
        lv.setAdapter(la);
  
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
              
                final View item=a.getChildAt(position);
           
		         item.setBackgroundColor(Color.LTGRAY);
		         switch(position){
		         case 0: a.getChildAt(1).setBackgroundColor(Color.TRANSPARENT);
		         		 a.getChildAt(2).setBackgroundColor(Color.TRANSPARENT);selectedPos=0;break;
		         case 1: a.getChildAt(0).setBackgroundColor(Color.TRANSPARENT);
        		 		 a.getChildAt(2).setBackgroundColor(Color.TRANSPARENT);selectedPos=1;break;
		         case 2: a.getChildAt(0).setBackgroundColor(Color.TRANSPARENT);
        		  		 a.getChildAt(1).setBackgroundColor(Color.TRANSPARENT);selectedPos=2;break;	
		         }
            }
        });
        
        confirmbtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(MainActivity.fromto[0].equals("")||MainActivity.fromto[0].equals(null)){
					controller.createURL(MainActivity.loc,MainActivity.fromto[1]);
				}else{
					controller.createURL(MainActivity.fromto[0],MainActivity.fromto[1]);
				}
				
				fromserver=controller.getRoutes();
				la.refill(fromserver);
				
				Log.e("OK:",controller.getRoutes().get(0).getType());
				/*Log.e("Position:",""+selectedPos);
				switch(selectedPos){
				case 0: ;break;
				case 1: ;break;
				case 2: ;break;
				default:
					Toast toast=new Toast(v.getContext());
					toast.makeText(v.getContext(),"Warning: Please select your desired vehicle to log this route.",Toast.LENGTH_LONG).show()
					;break;
				}*/
			}
        	
        });
        
        savebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				switch(selectedPos){
				case 0: settings.addFavouriteRoute(fromserver.get(0),getActivity());break;
				case 1: settings.addFavouriteRoute(fromserver.get(1),getActivity());break;
				case 2: settings.addFavouriteRoute(fromserver.get(2),getActivity());break;
				default:
					Toast toast=new Toast(v.getContext());
					toast.makeText(v.getContext(),"Warning: Please select the vehicle to save this as favourite.",Toast.LENGTH_LONG).show()
					;break;
				}
				
			}
		});
		
		return rootView;
	}
	
	public Fragment getVisibleFragment(){
	    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
	    List<Fragment> fragments = fragmentManager.getFragments();
	    for(Fragment fragment : fragments){
	        if(fragment != null && fragment.isVisible())
	            return fragment;
	    }
	    return null;
	}
	
	public Date getCurrentDate(){
		Calendar cal=new GregorianCalendar();
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
		sdf.setCalendar(cal);
		Log.e("Date: ",sdf.getCalendar().getTime().toString());
		return sdf.getCalendar().getTime();
	}
}


