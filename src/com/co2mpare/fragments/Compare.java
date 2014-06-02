package com.co2mpare.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import weather.ConnectionDetection;
import weather.Weather;
import weather.WeatherFacade;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import code.Controller;
import code.ListAdapter;
import code.LoggedRoute;
import code.Route;
import code.SettingsObject;

import com.co2mpare.MainActivity;
import com.co2mpare.R;

public class Compare extends Fragment {
	private static final String ARG_SECTION_NUMBER = "section_number2";

	SettingsObject settings=SettingsObject.getInstance();
	Controller controller=Controller.getInstance();
	View rootView;
	int selectedPos=-1;
	ListAdapter la;
	ListView lv;
	ArrayList<Route> fromserver;
	TextView weathertw;
	ImageView weatherimage;
		
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
		weatherimage=(ImageView) rootView.findViewById(R.id.weatherimage);
		weathertw=(TextView) rootView.findViewById(R.id.weathertw);
		weathertw.setTextSize(8 * getResources().getDisplayMetrics().density);
		weathertw.setText(checkWeather());
		
		//Initial Displaying//////////////////////////////////////////////////////////////
		String text[]={"Geyrstrasse","Suedring"};
		
		
		Route test1=new Route(0, 0, "Car", text, 0, 0, getCurrentDate());
		Route test2=new Route(0, 0, "Public", text, 0, 0, getCurrentDate());
		Route test3=new Route(0, 0, "Bike", text, 0, 0.0, getCurrentDate());
		/////////////////////////////////////////////////////////////////////////////
		
		fromserver = new ArrayList<Route>();
		
		
		///FOR TESTING ONLY////////////////////////////
		if(!settings.isCardisabled()){
			fromserver.add(test1);
		}
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
		         if(fromserver.size()==3){
		        	 switch(position){
			         case 0: a.getChildAt(1).setBackgroundColor(Color.TRANSPARENT);
			         		 a.getChildAt(2).setBackgroundColor(Color.TRANSPARENT);selectedPos=0;break;
			         case 1: a.getChildAt(0).setBackgroundColor(Color.TRANSPARENT);
	        		 		 a.getChildAt(2).setBackgroundColor(Color.TRANSPARENT);selectedPos=1;break;
			         case 2: a.getChildAt(0).setBackgroundColor(Color.TRANSPARENT);
	        		  		 a.getChildAt(1).setBackgroundColor(Color.TRANSPARENT);selectedPos=2;break;	
			         } 
		         }else if(fromserver.size()==2){
		        	 switch(position){
			         case 0: a.getChildAt(1).setBackgroundColor(Color.TRANSPARENT);selectedPos=0;break;
			         case 1: a.getChildAt(0).setBackgroundColor(Color.TRANSPARENT);selectedPos=1;break;
		        	 }
			        
		         }else if(fromserver.size()==2){
		        	 switch(position){
			         case 0: selectedPos=0;break;
			      
		        	 }
		         }
		        
            }
        });
      
        confirmbtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				LoggedRoute tmp;
				Log.e("LV Position:",selectedPos+"");
				switch(selectedPos){
				case 0: 
					if(settings.isLoggedIn()){
						tmp=new LoggedRoute(settings.getUserName(),fromserver.get(0).getDuration(),fromserver.get(0).getLength(),fromserver.get(0).getType(),
						fromserver.get(0).getCO2(),fromserver.get(0).getCosts(),fromserver.get(0).getDate(),fromserver.get(0).getLength(),fromserver.get(0).getCO2(),
						fromserver.get(0).getCosts());
						controller.sendLoggedRouteToServer(tmp);
						Toast toast=new Toast(v.getContext());
						toast.makeText(v.getContext(),"Route successfully logged.",Toast.LENGTH_LONG).show();break;
					}else{
						Toast toast=new Toast(v.getContext());
						toast.makeText(v.getContext(),"Please log in first.",Toast.LENGTH_LONG).show();break;
					}
						
				case 1: 
					if(settings.isLoggedIn()){
						tmp=new LoggedRoute(settings.getUserName(),fromserver.get(1).getDuration(),fromserver.get(1).getLength(),fromserver.get(1).getType(),
						fromserver.get(1).getCO2(),fromserver.get(1).getCosts(),fromserver.get(1).getDate(),fromserver.get(0).getLength(),fromserver.get(0).getCO2(),
						fromserver.get(0).getCosts());
						controller.sendLoggedRouteToServer(tmp);
						Toast toast=new Toast(v.getContext());
						toast.makeText(v.getContext(),"Route successfully logged.",Toast.LENGTH_LONG).show();break;
					}else{
						Toast toast=new Toast(v.getContext());
						toast.makeText(v.getContext(),"Please log in first.",Toast.LENGTH_LONG).show();break;
					}
				case 2: 
					if(settings.isLoggedIn()){
						tmp=new LoggedRoute(settings.getUserName(),fromserver.get(2).getDuration(),fromserver.get(2).getLength(),fromserver.get(2).getType(),
						fromserver.get(2).getCO2(),fromserver.get(2).getCosts(),fromserver.get(2).getDate(),fromserver.get(0).getLength(),fromserver.get(0).getCO2(),
						fromserver.get(0).getCosts());
						controller.sendLoggedRouteToServer(tmp);
						Toast toast=new Toast(v.getContext());
						toast.makeText(v.getContext(),"Route successfully logged.",Toast.LENGTH_LONG).show();break;
					}else{
						Toast toast=new Toast(v.getContext());
						toast.makeText(v.getContext(),"Please log in first.",Toast.LENGTH_LONG).show();break;
					}
				default:
					Toast toast=new Toast(v.getContext());
					toast.makeText(v.getContext(),"Warning: Please select your desired vehicle to log this route.",Toast.LENGTH_LONG).show()
					;break;
				}
			}
        	
        });
        
        savebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Route tmp=fromserver.get(0);
				String[] startAndEndPoint=new String[2];
				startAndEndPoint[0]=MainActivity.fromto[0];
				startAndEndPoint[1]=MainActivity.fromto[1];
				tmp.setText(startAndEndPoint);
				Log.e("StartPoint & Endpoint:",tmp.getText()[0]+" und "+tmp.getText()[1]);
				settings.addFavouriteRoute(fromserver.get(0),getActivity());				
				Toast toast=new Toast(v.getContext());
				toast.makeText(v.getContext(),"Saved as favourite.",Toast.LENGTH_LONG).show();
			}
		});
		
		return rootView;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
	}
	
	public String checkWeather(){
		//WeatherHttpClient client = new WeatherHttpClient();
		
		String weatherText = "";
		//ArrayList<Weather> weatherArr = null;
		Weather weatherDay = null;
		
		if(ConnectionDetection.isNetworkAvailable(getActivity())){
			weatherDay = WeatherFacade.getInstance().getTodayWeather("Innsbruck");
		}
		
		if(weatherDay.getId()<=232&&weatherDay.getId()>=200){
			//thunderstorm
			weatherText="Thunderstorm - better take\na public vehicle!";
			weatherimage.setImageResource(R.drawable.thunderstorm);
		}else if(weatherDay.getId()<=321&&weatherDay.getId()>=300){
			//drizzle
			weatherText="Rain - better take\na public vehicle!";
			weatherimage.setImageResource(R.drawable.rain);
		}else if(weatherDay.getId()<=531&&weatherDay.getId()>=500){
			//rain
			weatherText="Rain - better take\na public vehicle!";
			weatherimage.setImageResource(R.drawable.rain);
		}else if(weatherDay.getId()<=622&&weatherDay.getId()>=600){
			//snow
			weatherText="Snow - better take\na public vehicle!";
			weatherimage.setImageResource(R.drawable.snow);
		}else if(weatherDay.getId()<=771&&weatherDay.getId()>=700){
			//atmosphere
			
			weatherText="Foggy today.\n Take the bike!";
			weatherimage.setImageResource(R.drawable.atmosphere);
		}else if(weatherDay.getId()<=804&&weatherDay.getId()>=800){
			//cloudy
			switch(weatherDay.getId()){
			case 800:weatherText="Nice sunny day.\nTake the bike!";
				weatherimage.setImageResource(R.drawable.sunny);break;
			case 801:weatherText="Nice cloudy day.\nTake the bike!";
				weatherimage.setImageResource(R.drawable.cloud);break;
			case 802:weatherText="Scattered clouds.\nTake the bike!";
				weatherimage.setImageResource(R.drawable.cloud);break;
			case 803:weatherText="Broken clouds.\nTake the bike!";
				weatherimage.setImageResource(R.drawable.cloud);break;
			case 804:weatherText="Overcast clouds.\nTake the bike!";
				weatherimage.setImageResource(R.drawable.cloud);break;
			}
			
		}else if(weatherDay.getId()<=906&&weatherDay.getId()>=900){
			//extreme
			weatherText="Weather warning: better stay at home!";
		}
		
		return weatherText;
	}
	
	//Berechnung onSlide
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	  super.setUserVisibleHint(isVisibleToUser);
	  if (isVisibleToUser) {
		  if(MainActivity.fromto[0]==null){
			  Toast.makeText(getActivity(), "Please enter a starting point.", Toast.LENGTH_SHORT).show();
		  }else if(MainActivity.fromto[1]==null){
			  Toast.makeText(getActivity(), "Please enter a destination.", Toast.LENGTH_SHORT).show();
		  }else{
			  if(MainActivity.fromto[0].equals("Your current position")){
					controller.createURL(MainActivity.loc,MainActivity.fromto[1]);
				}else{
					controller.createURL(MainActivity.fromto[0],MainActivity.fromto[1]);
				}
			  	
				fromserver=controller.getRoutes();
				la.refill(fromserver);
		  }
	  }
	}
	
	
	
	public Date getCurrentDate(){
		Calendar cal=new GregorianCalendar();
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
		sdf.setCalendar(cal);
		Log.e("Date: ",sdf.getCalendar().getTime().toString());
		return sdf.getCalendar().getTime();
	}
}


