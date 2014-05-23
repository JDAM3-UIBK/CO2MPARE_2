package com.co2mpare;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import code.Cars;
import code.SettingsObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
import android.os.Build;

public class SettingsActivity extends Activity {
	
	
	List<Cars> listofallcars=new ArrayList<Cars>();
	Button savebtn;
	CheckBox disablecar,disablebike,disablepublic;
	Spinner predefined;
	EditText fuel,co2,home;
	SettingsObject settings=SettingsObject.getInstance();
	TextView prefc, preco;
	
	boolean dcar=false,dbike=false,dpublic=false;
	String fuelconsumption,co2emission;
	Double fc,co;
	int errorcounter=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		this.setContentView(R.layout.activity_settings);
		
		savebtn=(Button)findViewById(R.id.buttonsv);
		disablecar=(CheckBox)findViewById(R.id.cbdisablecar);
		disablepublic=(CheckBox)findViewById(R.id.cbdisablepublic);
		disablebike=(CheckBox)findViewById(R.id.cbdisablebike);
		fuel=(EditText)findViewById(R.id.from);
		co2=(EditText)findViewById(R.id.co2cons);
		preco=(TextView)findViewById(R.id.cotv);
		prefc=(TextView)findViewById(R.id.fctv);
		home=(EditText)findViewById(R.id.homeadd);
		
		
		//Settings aus gespeichertem File wieder herstellen
		
		settings.retrieveFromStorage(getThisContext());
				
	    fuel.setText(settings.getVerbrauch()+"");
		co2.setText(settings.getCO2()+"");
		home.setText(settings.getMyHome());
			
		if(settings.isBikedisabled()){
			disablebike.setChecked(true);
		}
		if(settings.isCardisabled()){
			disablecar.setChecked(true);
		}
		if(settings.isPublicdisabled()){
			disablepublic.setChecked(true);
		}	
		
		savebtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(disablecar.isChecked()){
					dcar=true;
					errorcounter++;
				}
				if(disablepublic.isChecked()){
					dpublic=true;
					errorcounter++;
				}
				if(disablebike.isChecked()){
					
					dbike=true;
					errorcounter++;
				}
				Log.e("Hier",""+disablebike.isChecked());
				if(!fuel.getText().toString().equals("")){
					fc=Double.valueOf(fuel.getText().toString());
				}else{
					fc=Double.valueOf(prefc.getText().toString());
				}
				if(!co2.getText().toString().equals("")){
					co=Double.valueOf(co2.getText().toString());
				}else{
					co=Double.valueOf(preco.getText().toString());
				}
				
				
				if(errorcounter>=3){
					Toast.makeText(v.getContext(), "There is an error; please check the values.", Toast.LENGTH_SHORT).show();
				}else{
					
					settings.setBikedisabled(disablebike.isChecked());
					settings.setCardisabled(disablecar.isChecked());
					settings.setPublicdisabled(disablepublic.isChecked());
					settings.setCO2(co);
					settings.setVerbrauch(fc);
					settings.setMyHome(home.getText().toString());
					
					Intent intent = new Intent(v.getContext(), MainActivity.class);
					
					//Einstellungen in Datei schreiben
					
					String toWrite=""+disablebike.isChecked()+";"+disablecar.isChecked()+";"+disablepublic.isChecked()+";"+co+";"+fc+";"+home.getText()+";";
					
					settings.writeToFile(getThisContext(),toWrite);
			        
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
		
	}
	
	public Context getThisContext(){
		return this.getApplicationContext();
	}
	
	public void addItemsOnSpinner() {
		 
		predefined=(Spinner)findViewById(R.id.spinner1);
		
		//Formatierung für den Spinner festlegen, indem eine ArrayList mit Strings angelegt wird. Die Indexierung bleibt für die Fahrzeuge gleich, damit man mit Position
		//direkt auf die List zugreifen kann
		
		
		List<String> test=new ArrayList<String>();
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
