package com.co2mpare;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import code.Cars;
import code.SettingsObject;

public class SettingsActivity extends Activity {
	
	
	List<Cars> listofallcars=new ArrayList<Cars>();
	Button savebtn;
	CheckBox disablecar;
	Spinner predefined;
	EditText fuel,co2,home;
	SettingsObject settings=SettingsObject.getInstance();
	TextView prefc, preco, tw2,tw3,tw4,tw5,tw6,tw7,tw8;
	
	boolean dcar=false,dbike=false,dpublic=false,owncar=false;
	String fuelconsumption,co2emission;
	Double fc,co;
	int errorcounter=0,spinnerposition=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		this.setContentView(R.layout.activity_settings);
		
		savebtn=(Button)findViewById(R.id.buttonsv);
		disablecar=(CheckBox)findViewById(R.id.cbdisablecar);
		
		fuel=(EditText)findViewById(R.id.from);
		co2=(EditText)findViewById(R.id.co2cons);
		preco=(TextView)findViewById(R.id.cotv);
		prefc=(TextView)findViewById(R.id.fctv);
		home=(EditText)findViewById(R.id.homeadd);
		tw2=(TextView)findViewById(R.id.saved);
		tw3=(TextView)findViewById(R.id.textView3);
		tw4=(TextView)findViewById(R.id.textView4);
		tw5=(TextView)findViewById(R.id.textView5);
		tw6=(TextView)findViewById(R.id.textView6);
		tw7=(TextView)findViewById(R.id.textView7);
		tw8=(TextView)findViewById(R.id.textView8);
		predefined=(Spinner)findViewById(R.id.spinner1);
		
		disablecar.setTextSize(7 * getResources().getDisplayMetrics().density);
		fuel.setTextSize(7 * getResources().getDisplayMetrics().density);
		co2.setTextSize(7 * getResources().getDisplayMetrics().density);
		preco.setTextSize(7 * getResources().getDisplayMetrics().density);
		prefc.setTextSize(7 * getResources().getDisplayMetrics().density);
		home.setTextSize(7 * getResources().getDisplayMetrics().density);
		tw2.setTextSize(9 * getResources().getDisplayMetrics().density);
		tw3.setTextSize(9 * getResources().getDisplayMetrics().density);
		tw4.setTextSize(9 * getResources().getDisplayMetrics().density);
		savebtn.setTextSize(12 * getResources().getDisplayMetrics().density);
		tw5.setTextSize(7 * getResources().getDisplayMetrics().density);
		tw6.setTextSize(7 * getResources().getDisplayMetrics().density);
		tw7.setTextSize(7 * getResources().getDisplayMetrics().density);
		tw8.setTextSize(7 * getResources().getDisplayMetrics().density);
		
		
		
		//Settings aus gespeichertem File wieder herstellen
		
		settings.retrieveFromStorage(getThisContext());
		if(settings.getOwnCar()){
			fuel.setText(settings.getVerbrauch()+"");
			co2.setText(settings.getCO2()+"");
		}
			    
		home.setText(settings.getMyHome());
			
		
		if(settings.isCardisabled()){
			disablecar.setChecked(true);
		}
		
		savebtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(disablecar.isChecked()){
					dcar=true;
					errorcounter++;
				}
				
				
				if((!fuel.getText().toString().equals("0.0"))&&!fuel.getText().toString().equals("")){
					fc=Double.valueOf(fuel.getText().toString());
					owncar=true;
				}else{
					fc=Double.valueOf(prefc.getText().toString());
					owncar=false;
					spinnerposition=predefined.getSelectedItemPosition();
				}
				if((!co2.getText().toString().equals("0.0"))&&!co2.getText().toString().equals("")){
					co=Double.valueOf(co2.getText().toString());
					owncar=true;
				}else{
					co=Double.valueOf(preco.getText().toString());
					owncar=false;
					spinnerposition=predefined.getSelectedItemPosition();
				}
				
				
				
				if(errorcounter>=3){
					Toast.makeText(v.getContext(), "There is an error; please check the values.", Toast.LENGTH_SHORT).show();
				}else{
					
					
					settings.setCardisabled(disablecar.isChecked());
					
					settings.setCO2(co);
					settings.setVerbrauch(fc);
					settings.setMyHome(home.getText().toString());
					
					Intent intent = new Intent(v.getContext(), MainActivity.class);
					
					//Einstellungen in Datei schreiben
					
					String toWrite=""+disablecar.isChecked()+";"+co+";"+fc+";"+home.getText()+";"+owncar+";"+spinnerposition+";";
					
					settings.writeToFile(getThisContext(),toWrite);
			        Log.e("ToWrite",toWrite);
				    startActivity(intent);
				}
					
			}
			
		});
		
		//Wenn Beispielautos noch nicht in der Liste sind -> aus externer TXT lesen:
		
		if(listofallcars.size()<1){
			
			try {
				createCarList();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		addItemsOnSpinner();
		if(settings.getOwnCar()){
			predefined.setSelection(0);
		}else{
			predefined.setSelection(settings.getSpinnerPosition());
		}
		
		
	}
	
	public Context getThisContext(){
		return this.getApplicationContext();
	}
	
	public void addItemsOnSpinner() {
		 
		
		
		//Formatierung für den Spinner festlegen, indem eine ArrayList mit Strings angelegt wird. Die Indexierung bleibt für die Fahrzeuge gleich, damit man mit Position
		//direkt auf die List zugreifen kann
		
		
		List<String> test=new ArrayList<String>();
		test.add("Your own car");
		for(Cars tmp:listofallcars){
			test.add(tmp.getMfr()+" "+tmp.getModel()+", FC: "+tmp.getFc());
		}			
				
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, test);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		predefined.setAdapter(dataAdapter);
		
		predefined.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				 predefined.setSelection(position);
				 prefc.setText(listofallcars.get(position).getFc()+"");
				 preco.setText(listofallcars.get(position).getCo2()+"");
				 
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	  }
	
	@SuppressLint("NewApi")
	public void createCarList() throws IOException{
				
		BufferedReader br = new BufferedReader(new InputStreamReader(this.getAssets().open("fuelconsumption.txt")));
	    try {
	       
	    	String line = br.readLine();

	        while (line != null) {
	            String[] tmp=line.split("\t");
	            tmp[2]=tmp[2].replace(",",".");		
	            tmp[3]=tmp[3].replace(",",".");
	            tmp[4]=tmp[4].replace(",",".");
	            Cars tmpcar=new Cars(tmp[0],tmp[1],Double.valueOf(tmp[3]),Double.valueOf(tmp[4]));
	            this.listofallcars.add(tmpcar);
	            line = br.readLine();
	        }
	        
	        
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_settingsfr,
					container, false);
			return rootView;
		}
	}

}
