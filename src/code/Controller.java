/**
 * 
 */
package code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Observable;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.co2mpare.fragments.Main;

/**
 * @author $ Martin Haslinger
 *
 */
public class Controller {
	
	static ArrayList<Route> fromserver;
	ArrayList<Route> favroutes;
	static Controller controller;
	boolean TBareEmpty=true;

	public Controller(){
		
	}
	
	public static Controller getInstance(){
		if(controller==null){
			return controller=new Controller();
		}else{
			return controller;
		}
	}
	
	public void setFromTo(String from, String to){
		
	}
	
	public ArrayList<Route> getFavRoutes(){
		return favroutes;
	}
	
	public void setFavRoutest(ArrayList<Route> favroutes){
		this.favroutes=favroutes;
	}
	
	public ArrayList<Route> getRoutes(){
		
		return fromserver;
	}
	
	public void setRoutes(ArrayList<Route> fromserver){
		this.fromserver=fromserver;
	}
	
	public void createURL(String from, String to){
		String url;
		String serverip="http://mhaslinger.com:8080/Server_v01/";
		url=serverip+"str$from="+from+"&to="+to;
		
			getJSONObject(url);
		
	}
	
	public void createURL(double from, double to){
		String url;
		String serverip="http://mhaslinger.com:8080/Server_v01/";
		url=serverip+"latlng$from="+from+"&to="+to;
		
			getJSONObject(url);
		
	}
	
	public void createURL(LatLng from, String to){
		
		String url;
		String fromstr=from.getLat()+","+from.getLng();
		String serverip="http://mhaslinger.com:8080/Server_v01/";
		url=serverip+"latlngstr$from="+fromstr+"&to="+to;
		getJSONObject(url);
	}
	
	
	private static void JSONParser(JSONArray rawroutes){
		fromserver=new ArrayList<Route>();
		String[] stationsstr;
		for(int i=0;i<3;i++){
			try {
				JSONObject tmp=rawroutes.getJSONObject(i);
				int duration=Integer.valueOf(tmp.getInt("duration"));
				int length=Integer.valueOf(tmp.getInt("length"));
				String type=tmp.getString("type");
				if(i==1){
					JSONArray stations=tmp.getJSONArray("text");
					stationsstr=new String[stations.length()];
					for(int j=0;j<stations.length()-1;j++){
						stationsstr[j]=stations.getString(j);
					}
				}else{
					stationsstr=null;
				}
				ArrayList<ArrayList<LatLng>> polylinePoints=null;
				double cost=tmp.getDouble("costs");
				Date date=null;
				double co2=tmp.getDouble("co2");
				Route tmproute=new Route(duration,length,type,polylinePoints,stationsstr,co2,cost,date);
				fromserver.add(tmproute);
				Log.e("Size routes:",""+fromserver.size());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private static void getJSONObject(String url) {
		JSONArray rawroutes=null;
		Log.e("Generated url:",url);
		try {
			rawroutes=new RetrieveData().execute(url).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONParser(rawroutes);
	}

}

class RetrieveData extends AsyncTask<String, Void, JSONArray> {
	
	@Override
    protected JSONArray doInBackground(String... url) {
		InputStream is=null;
		JSONArray rawroutes=null;
		try {
			is = new URL(url[0]).openStream();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try{
     		String jsonText;
     		try{
     			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
     			jsonText = rd.readLine();
     			
     			rawroutes= new JSONArray(jsonText);
     			
     		} catch (IOException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		} catch (JSONException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		} finally {
     			
     		}
		}finally{
			
		}
		
        return rawroutes;
	 }
	
	 protected void onPostExecute(JSONArray Result){

		 
         
     }
	 
}