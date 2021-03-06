/**
 * 
 */
package code;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import com.co2mpare.MainActivity;
import com.co2mpare.fragments.Login;

import android.content.Context;
import android.util.Log;

/**
 * @author $ Martin Haslinger
 *
 */
public class SettingsObject {
	
	
	boolean cardisabled;
	boolean remember;
	boolean owncar;
	double verbrauch;
	double co2;
	static SettingsObject settings;
	String myhome;
	LinkedList<Route> favouriteroutes;
	int spinnerposition;
	String username,pw;
	boolean isLoggedIn;
	
	public static SettingsObject getInstance(){
		if(settings==null){
			return settings=new SettingsObject();
		}else{
			return settings;
		}
	}
	
	public void addFavouriteRoute(Route route, Context context){
		favouriteroutes.add(route);
		
		writeRoutesToFile(context);
	}
	
	public boolean getRememberMe(){
		return this.remember;
	}
	
	public void setRememberMe(boolean remember){
		this.remember=remember;
	}
	
	public String getUserName(){
		return username;
	}
	
	public void setUserName(String username){
		this.username=username;
	}
	
	
	
	public void setIsLoggedIn(boolean loggedin){
		this.isLoggedIn=loggedin;
	}
	
	public boolean isLoggedIn(){
		return isLoggedIn;
	}
	
	public int getSizeOfFavRoutes(){
		if(favouriteroutes==null){
			return 0;
		}else{
			return favouriteroutes.size();
		}
	}
	
	public String getDestOfFavRoutes(int i){
		String destination=favouriteroutes.get(i).getText()[(favouriteroutes.get(i).getText().length)-1];
		return destination;
	}
	
	public String getStartPosOfFavRoutes(int i){
		String startpos=favouriteroutes.get(i).getText()[0];
		return startpos;
	}
	
	public String getVehicleOfFavRoutes(int i){
		return favouriteroutes.get(i).getType();
		
	}
	
	public boolean getOwnCar(){
		return owncar;
	}
	
	public void setOwnCar(boolean owncar){
		this.owncar=owncar;
	}
	
	public void setSpinnerPosition(int spinnerposition){
		this.spinnerposition=spinnerposition;
	}
	
	public int getSpinnerPosition(){
		return spinnerposition;
	}
	
	public void saveUsernameAndPassword(boolean save,String usr, String pw){
		String path = Login.activityInstance.getFilesDir()+"";
		String data=save+";"+usr+";"+pw;
		
		path += "/login.txt";
		OutputStream out;
		
		try {
			out = new BufferedOutputStream(new FileOutputStream(path,false));
			out.write(data.getBytes());
			out.flush();
			out.close();
			} catch (FileNotFoundException e) {
			    e.printStackTrace();
			} catch (IOException e) {
			    e.printStackTrace();
			}
	}
	
	public String getSavedUsernameAndPassword(){
		String input="";
		
	    try {
	        File file = new File(Login.activityInstance.getFilesDir(),"login.txt");
	        if(file.exists() && !file.isDirectory()){
	        	BufferedReader br = new BufferedReader(new FileReader(file));  
		        input=br.readLine();
		        br.close();
	        }
	        
	    }catch (IOException e) {
	        e.printStackTrace();
	    }
	    
		return input;
	}
	
	public void retrieveFromStorage(Context context){
		String input="";
	    try {
	        File file = new File(context.getFilesDir(),"settings.txt");
	        BufferedReader br = new BufferedReader(new FileReader(file));  
	        input=br.readLine();
	        br.close();
	    }catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	   //Favoritenrouten wiederherstellen und in die LinkedList schreiben - Route Objekt UNBEDINGT IMPLEMENTS SERIALIZABLE
	    
	    File file = new File(context.getFilesDir() + "favroutes.obj");

		LinkedList<Route> tmp = new LinkedList<Route>();

		if (file.isFile()) {
			try {
				ObjectInputStream is = new ObjectInputStream(
						new FileInputStream(file));
				tmp = (LinkedList<Route>) (is.readObject());
				is.close();
			} catch (Exception e) {
				System.out.println("IOException occured :(");
				e.printStackTrace();
			}
		}
		if(tmp==null){
			favouriteroutes=new LinkedList<Route>();
		}else{
			favouriteroutes = tmp;
		}
		
	    
	    //Daten in das Objekt schreiben, damit die Controller darauf zugriff haben
	    if(!input.equals("")){
			
			String[] parts=input.split(";");
			
			if(parts[0].equals("true")){
				settings.setCardisabled(true);
			}else{
				settings.setCardisabled(false);
			}
			
			
			setCO2(Double.valueOf(parts[1]));
			setVerbrauch(Double.valueOf(parts[2]));
			setMyHome(parts[3]);
			if(parts[4].equals("true")){
				settings.setOwnCar(true);
			}else{
				settings.setOwnCar(false);
			}
			if(parts[5].equals(null)){
				setSpinnerPosition(0);
			}else{
				setSpinnerPosition(Integer.valueOf(parts[5]));
			}
			
			
	    }
	}
	
	
	public void writeToFile(Context context,String data){
		String path = context.getFilesDir()+"";
		
		path += "/settings.txt";
		OutputStream out;
		Log.e("Path:",path);
		try {
			out = new BufferedOutputStream(new FileOutputStream(path,false));
			out.write(data.getBytes());
			out.flush();
			out.close();
			} catch (FileNotFoundException e) {
			    e.printStackTrace();
			} catch (IOException e) {
			    e.printStackTrace();
			}
		writeRoutesToFile(context);
	}
	
	//Favoritenrouten in *.obj File schreiben - Route Objekt UNBEDINGT IMPLEMENTS SERIALIZABLE
	public void writeRoutesToFile(Context context){
		File file = new File(context.getFilesDir() + "favroutes.obj");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, false);
		} catch (FileNotFoundException e1) {

			e1.printStackTrace();
		}
		try {
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(favouriteroutes);
			os.close();
		} catch (IOException e) {
			System.out.println("IOException occured :( " + e);
			e.printStackTrace();
		}
	}

	public void setMyHome(String myhome){
		this.myhome=myhome;
	}
	
	public String getMyHome(){
		return myhome;
	}
	
	public void setCO2(double co2){
		this.co2=co2;
	}
	
	public double getCO2(){
		return co2;
	}
	
	/**
	 * @return the cardisabled
	 */
	public boolean isCardisabled() {
		return cardisabled;
	}

	/**
	 * @param cardisabled the cardisabled to set
	 */
	public void setCardisabled(boolean cardisabled) {
		this.cardisabled = cardisabled;
	}


	/**
	 * @return the verbrauch
	 */
	public double getVerbrauch() {
		return verbrauch;
	}

	/**
	 * @param verbrauch the verbrauch to set
	 */
	public void setVerbrauch(double verbrauch) {
		this.verbrauch = verbrauch;
	}
	
	

}
