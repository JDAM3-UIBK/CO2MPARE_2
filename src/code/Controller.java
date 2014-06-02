/**
 * 
 */
package code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.HttpClient; 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;


/**
 * @author $ Martin Haslinger
 *
 */
public class Controller {
	
	static ArrayList<Route> fromserver;
	ArrayList<Route> favroutes;
	static ArrayList<LoggedRoute> loggedRoutes;
	boolean rememberme=false;
	static Controller controller;
	boolean TBareEmpty=true;
	static SettingsObject settings=SettingsObject.getInstance();
	
	
	public Controller(){
		
	}
	
	public static Controller getInstance(){
		if(controller==null){
			return controller=new Controller();
		}else{
			return controller;
		}
	}
	
	public void setRememberMe(boolean rememberme, String usr, String pw){
		settings.saveUsernameAndPassword(rememberme, usr, pw);
		this.rememberme=rememberme;
	}
	
	public boolean getRememberMe(){
		getSavedUsernameAndPassword();
		return rememberme;
	}
	
	public String[] getSavedUsernameAndPassword(){
		String[] tmp=settings.getSavedUsernameAndPassword().split(";");
		String[] toreturn;
		if(tmp[0].equals("true")){
			this.rememberme=true;
		}else{
			this.rememberme=false;
		}
		if(tmp.length==3){
			toreturn=new String[2];
			toreturn[0]=tmp[1];
			toreturn[1]=tmp[2];
			Log.e("USERNAME:",toreturn[0]);
		}else{
			toreturn=new String[2];
			toreturn[0]="";
			toreturn[1]="";
		}

		return toreturn;
	}
	
	public boolean userLogin(String username, String pw){
		if(true){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isCarDisabled(){
		return settings.isCardisabled();
	}
	
	
	public boolean isUserRegistered(String username, String pw){
		try {
			
			int status=new IsUserRegistered().execute(username,pw).get();
			if(status==500){
				return false;
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public void registerUser(String username, String pw){
		if(username.contains("ü")){
			username.replace("ü", "ue");
		}
		if(username.contains("ö")){
			username.replace("ö", "oe");
		}
		if(username.contains("ä")){
			username.replace("ä", "ae");
		}
		if(username.contains("ß")){
			username.replace("ß", "ss");
		}
		if(pw.contains("ü")){
			pw.replace("ü", "ue");
		}
		if(pw.contains("ö")){
			pw.replace("ö", "oe");
		}
		if(pw.contains("ä")){
			pw.replace("ä", "ae");
		}
		if(pw.contains("ß")){
			pw.replace("ß", "ss");
		}
		
		try {
			
			int status=new RegisterUser().execute(username,pw).get();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public ArrayList<LoggedRoute> getAllLoggedRoutes(LoggedRoute sample){
		
		JSONArray rawroutes=null;
		
		try {
			
			rawroutes=new GetLoggedRoutes().execute(sample).get();
			parseLoggedRoutes(rawroutes);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return loggedRoutes;
	}
	
	private static void parseLoggedRoutes(JSONArray rawroutes){
		loggedRoutes=new ArrayList<LoggedRoute>();
		SimpleDateFormat sdf=new SimpleDateFormat();
		
		GregorianCalendar calendar = new GregorianCalendar();
        
			
		for(int i=0;i<rawroutes.length();i++){
			
			try {
				JSONObject tmp=rawroutes.getJSONObject(i);
				int duration=Integer.valueOf(tmp.getInt("duration"));
				int length=Integer.valueOf(tmp.getInt("length"));
				String type=tmp.getString("type");

				double cost=Double.valueOf(tmp.getDouble("costs"));
				double co2=Double.valueOf(tmp.getDouble("CO2"));
				
				calendar.setTimeInMillis(tmp.getLong("date"));
				Date date=sdf.parse(sdf.format(calendar.getTime()));
				
				int referencelength=Integer.valueOf(tmp.getInt("referencelength"));
				double referenceco2=Double.valueOf(tmp.getDouble("referenceco2"));
				double referencecosts=Double.valueOf(tmp.getDouble("referencecosts"));
				
				LoggedRoute tmproute=new LoggedRoute(settings.getUserName(),duration,length,type,co2,cost,date,referencelength,referenceco2,referencecosts);
				loggedRoutes.add(tmproute);
				
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
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
		
		if(to.contains("ü")){
			to.replaceAll("ü", "ue");
		}
		if(to.contains("ö")){
			to=to.replaceAll("ö", "oe");
		}
		if(to.contains("ä")){
			to=to.replaceAll("ä", "ae");
		}
		if(to.contains("ß")){
			to=to.replaceAll("ß", "ss");
		}
		if(from.contains("ü")){
			from=from.replaceAll("ü", "ue");
		}
		if(from.contains("ö")){
			from=from.replaceAll("ö", "oe");
		}
		if(from.contains("ä")){
			from=from.replaceAll("ä", "ae");
		}
		if(from.contains("ß")){
			from=from.replaceAll("ß", "ss");
		}
		
			String url;
			String serverip="http://mhaslinger.com:8080/JDAMserver_02/calc/";
			url=serverip+"str$from="+from.replace(" ", "+")+"&to="+to.replace(" ", "+");
			

			getJSONObject(url);
		
	}
	
	public void createURL(LatLng from, String to){
		
			if(to.contains("ü")){
				to=to.replaceAll("ü", "ue");
			}
			if(to.contains("ö")){
				to=to.replaceAll("ö", "oe");
			}
			if(to.contains("ä")){
				to=to.replaceAll("ä", "ae");
			}
			if(to.contains("ß")){
				to=to.replaceAll("ß", "ss");
			}
			if(to.contains(" ")){
				to=to.replaceAll(" ", "+");
			}
		
			
			String url;
			String serverip="http://mhaslinger.com:8080/JDAMserver_02/calc/";
			
			url=serverip+"latlngstr$from="+from.getLat()+","+from.getLng()+"&to="+to.replaceAll(" ","+")+".";
			
			getJSONObject(url);
	}
	
	private static Date getCurrentDate(){
		
		Date date = new Date();
		return date;
	}
	
	private static void JSONParser(JSONArray rawroutes){
		fromserver=new ArrayList<Route>();
		String[] stationsstr;
		int h=0;
		if(isCarDisabled()){
			h=1;
		}
		for(int i=h;i<3;i++){
			
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
				double cost=0.0,co2=0.0;
				if(type.equals("driving")){
					cost=(length/1000)*(settings.getVerbrauch()/100*1.4);
				}
				if(type.equals("PTE")){
					cost=Double.valueOf(tmp.getDouble("costs"));
				}
				Date date=getCurrentDate();
				if(type.equals("driving")){
					co2=(length/1000)*(settings.getCO2());
				}
				if(type.equals("PTE")){
					co2=Double.valueOf(tmp.getDouble("co2"));
				}
				Route tmproute=new Route(duration,length,type,polylinePoints,stationsstr,co2,cost,date);
				fromserver.add(tmproute);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void sendLoggedRouteToServer(LoggedRoute route){
		try {
			
			int status=new LogRoute().execute(route).get();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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


class GetLoggedRoutes extends AsyncTask<LoggedRoute, Void, JSONArray> {

	
	@Override
	protected JSONArray doInBackground(LoggedRoute... params) {
		final HttpClient httpClient = new HttpClient();

		String jsonRoute = params[0].toJson(); // flexJson
        int status = 0;
        JSONArray rawroutes=null;
       try {


            StringRequestEntity requestEntity = new StringRequestEntity(jsonRoute,
                    "application/json",
                    "UTF-8");

            

            System.out.println(requestEntity.toString());
            PostMethod postMethod = new PostMethod("http://mhaslinger.com:8080/cp423/routemanagement/showRoutePerUser");
            postMethod.setRequestEntity(requestEntity);
            
            status = httpClient.executeMethod(postMethod); 

            Log.e("STATUS:","status: " + status);
            //getResponseBodyAsStream
            String tmp = postMethod.getResponseBodyAsString();
            Log.e("Response:",tmp);
            postMethod.releaseConnection(); 
		
            rawroutes=new JSONArray(tmp);
		
        
	 } catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		 
	 }
		return rawroutes;
	}
	
	
	 
}


class LogRoute extends AsyncTask<LoggedRoute, Void, Integer> {

	
	@Override
	protected Integer doInBackground(LoggedRoute... params) {
		final HttpClient httpClient = new HttpClient();


       

		String jsonRoute = params[0].toJson(); // flexJson
        int status = 0;
        
       try {


            StringRequestEntity requestEntity = new StringRequestEntity(jsonRoute,
                    "application/json",
                    "UTF-8");

            Log.e("JSONSTRING:",jsonRoute);

            System.out.println(requestEntity.toString());
            PostMethod postMethod = new PostMethod("http://mhaslinger.com:8080/cp423/routemanagement/saveRoute");
            postMethod.setRequestEntity(requestEntity);
            
            status = httpClient.executeMethod(postMethod); 

            Log.e("STATUS:","status: " + status);
            //getResponseBodyAsStream
            String tmp = postMethod.getResponseBodyAsString();
            postMethod.releaseConnection(); 
		
		
        
	 } catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		 
	 }
		return status;
	}
	
	
	 
}


class RegisterUser extends AsyncTask<String, Void, Integer> {

	@Override
	protected Integer doInBackground(String... params) {
		final HttpClient httpClient = new HttpClient();


        User user = new User(params[0], params[1]);

        String jsonString = user.toJson(); // flexJson
        int status = 0;
        
       try {


            StringRequestEntity requestEntity = new StringRequestEntity(jsonString,
                    "application/json",
                    "UTF-8");

            Log.e("JSONSTRING:",jsonString);

            System.out.println(requestEntity.toString());
            PostMethod postMethod = new PostMethod("http://mhaslinger.com:8080/cp423/usermanagement/Register");
            postMethod.setRequestEntity(requestEntity);
            
            status = httpClient.executeMethod(postMethod); 

            Log.e("STATUS:","status: " + status);
            //getResponseBodyAsStream
            String tmp = postMethod.getResponseBodyAsString();
            postMethod.releaseConnection(); 
		
		
        
	 } catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		 
	 }
		return status;
	}
	
	 
}

class IsUserRegistered extends AsyncTask<String, Void, Integer> {
	
	
	@Override
	protected Integer doInBackground(String... params) {
		final HttpClient httpClient = new HttpClient();


        User user = new User(params[0], params[1]);

        String jsonString = user.toJson(); // flexJson
        int status = 0;
        
       try {


            StringRequestEntity requestEntity = new StringRequestEntity(jsonString,
                    "application/json",
                    "UTF-8");


            System.out.println(requestEntity.toString());
            PostMethod postMethod = new PostMethod("http://mhaslinger.com:8080/cp423/usermanagement/Anmelden");
            postMethod.setRequestEntity(requestEntity);
            
            status = httpClient.executeMethod(postMethod); 

            Log.e("STATUS:","status: " + status);
            //getResponseBodyAsStream
            String tmp = postMethod.getResponseBodyAsString();
            postMethod.releaseConnection(); 
		
		
        
	 } catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		 
	 }
		return status;
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
     			Log.e("jsonText:",jsonText);
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
	
}