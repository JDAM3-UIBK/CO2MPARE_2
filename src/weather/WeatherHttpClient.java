package weather;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class for creating the http weather
 * request for today and coming days.
 * 
 * @author Manuel Kapferer
 *
 */
public class WeatherHttpClient {		 
		private static String TODAY_CAST_URL = "http://api.openweathermap.org/data/2.5/weather?q=";	
	    private static String FORCAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=";	
	    
	    /**
	     * method for applying http request 
	     * @param location
	     * 				name of target city location 
	     * @param whichType
	     * 				defines if today or forecasting weather
	     * @return
	     * 				string of the received json file
	     */
	    public String getWeatherData(String location, int whichType) {
	        HttpURLConnection con = null ;												//http connection
	        InputStream inStream = null;												//inputstream
	        
	        try {
	        	// create setup connection + connect 
	        	// which type of weather does someone want
	        	switch (whichType) {
				case 1:
					con = (HttpURLConnection) ( new URL(TODAY_CAST_URL + location + "&mode=json")).openConnection();
					break;
				case 2:
					con = (HttpURLConnection) ( new URL(FORCAST_URL + location + "&mode=json")).openConnection();
					break;
				default:
					break;
				}
	        	
	        	// settings of the coming http request
	            con.setRequestMethod("GET");
	            con.setDoInput(true);
	            //con.setDoOutput(true);
	            con.connect();
	      
	  
	            // read the response
	            StringBuffer buffer = new StringBuffer();
	            inStream = con.getInputStream();										// set inputstream
	            BufferedReader br = new BufferedReader(new InputStreamReader(inStream));// set buffer
	            
	            // set string and read message
	            String line = null;
	            while (  (line = br.readLine()) != null )
	                buffer.append(line + "\r\n");
	            
	            // close stream + connection
	            inStream.close();
	            con.disconnect();
	            
	            return buffer.toString();
	        }
	        catch(Throwable t) {
	            t.printStackTrace();
	        }
	        
	        
	        finally {
	            try { inStream.close(); } catch(Throwable t) {}
	            try { con.disconnect(); } catch(Throwable t) {}
	        }
	 
	        return null;      
	    }
	    
}
