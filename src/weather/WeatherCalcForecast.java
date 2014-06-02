package weather;

import java.util.ArrayList;

import android.os.AsyncTask;

/**
 * Class for threading the http request 
 * of weather forecast for android.
 * 
 * @author Manuel Kapferer
 *
 */
public class WeatherCalcForecast extends AsyncTask<String, Void, ArrayList<Weather>>{
	JsonWeatherParser parser = new JsonWeatherParser();

	@Override
	protected ArrayList<Weather> doInBackground(String... params) {
		String data = ((new WeatherHttpClient()).getWeatherData(params[0],2));
		return parser.getWeatherForecast(data);
	}

}
