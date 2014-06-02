package weather;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class for parsing the  two different 
 * JSON strings, into weather data.
 * 
 * @author Manuel Kapferer
 *
 */
public class JsonWeatherParser {
		
	/**
	 * method to work on the json string of
	 * the forecast
	 * @param data
	 * 				json string
	 * @return
	 * 				ArrayList of Weather objects
	 */
	public ArrayList<Weather> getWeatherForecast(String data){
		ArrayList<Weather> weatherList = new ArrayList<Weather>();
		
		// We create out JSONObject from the data
		JSONObject jObj, jObj2, jObj3;

		try {
			jObj = new JSONObject(data);
			// We get weather info (This is an array of the list)
			JSONArray jList = jObj.getJSONArray("list");
			// create save variables
			JSONArray jArr = null;
			JSONObject JSONWeather = null;

			// get the weather arrays out of the list and
			// save the weather data in weatherList
			for (int i = 0; i < jList.length(); i++) {
				jObj2 = jList.getJSONObject(i);
				jObj3 = jObj2.getJSONObject("temp");
				jArr = jObj2.getJSONArray("weather");
				JSONWeather = jArr.getJSONObject(0);

				weatherList.add(new Weather());

				// We use only the first value
				weatherList.get(i).setTemperature(jObj3.getInt("day"));
				weatherList.get(i).setId(JSONWeather.getInt("id"));
				weatherList.get(i).setDescription(
						JSONWeather.getString("description"));
				weatherList.get(i).setCondition(JSONWeather.getString("main"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return weatherList;
	}
	
	/**
	 * method to work on the json string of
	 * the today weather
	 * @param data
	 * 				json string
	 * @return
	 * 				Weather objects
	 */
	public Weather getTodayWeather(String data){
		Weather todayWeather = new Weather();
		JSONObject jObj;
		
		try {
			jObj = new JSONObject(data);
			
			// We get weather info (This is an array)
			JSONArray jWeatherDataArr = jObj.getJSONArray("weather");
			JSONObject JSONWeather = jWeatherDataArr.getJSONObject(0);
			
			// get data out of WeatherDataArray
			todayWeather.setId(JSONWeather.getInt("id"));
			todayWeather.setDescription(JSONWeather.getString("description"));
			todayWeather.setCondition(JSONWeather.getString("main"));
			
			// get temperature from main
			JSONObject mainObj = jObj.getJSONObject("main");
			todayWeather.setTemperature(mainObj.getInt("temp"));
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return todayWeather;
	}
}
