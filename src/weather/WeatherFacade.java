package weather;

import java.util.ArrayList;

/**
 * Class providing interface facade
 * for the other componets.
 * 
 * @author Manuel Kapferer
 *
 */
public class WeatherFacade implements IWeather{
	private static WeatherFacade INSTANCE = null;
	private ArrayList<Weather> weatherArr = null;
	private Weather	todayWeather = null;			 
	private WeatherCalcDay calc1 = null;		// thread for calculation of today weather
	private WeatherCalcForecast calc2 = null;	// thread for calculation of forecast weather
	
	/**
	 * default constructor
	 */
	public WeatherFacade() {
		this.calc1 = new WeatherCalcDay();
		this.calc2 = new WeatherCalcForecast();
	}
	
	@Override
	public Weather getTodayWeather(String cityName) {
		try {
			todayWeather = calc1.execute(cityName).get();//" Tag: ";
		} catch (Exception e) {	}
		
		return todayWeather;
	}

	@Override
	public ArrayList<Weather> getForecastWeather(String cityName) {
		try {
			weatherArr = calc2.execute(cityName).get();//"8 Tag: ";
		} catch (Exception e) {	}
		
		return weatherArr;
	}

	/**
	 * method to get instance of singleton
	 * @return
	 * 			instance of this class
	 */
	public static WeatherFacade getInstance() {
		if(INSTANCE == null){
			INSTANCE = new WeatherFacade();
		}
		
		return INSTANCE;
	}

}
