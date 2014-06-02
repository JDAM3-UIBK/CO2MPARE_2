package weather;

import java.util.ArrayList;

/**
 * Interface with contracting methods
 * for the other components.
 * 
 * @author Manuel Kapferer
 *
 */
public interface IWeather {
	
	/**
	 * method to deliver the actual weather
	 * @param cityName
	 * 				string of the city the user wants the weather
	 * @return
	 * 				weather object with saved data
	 */
	public Weather getTodayWeather(String cityName);
	
	/**
	 * method to deliver forecast weather for 8 days
	 * @param cityName
	 * 				string of the city the user wants the weather
	 * @return
	 * 				weather object with saved data
	 */
	public ArrayList<Weather> getForecastWeather(String cityName);
}
