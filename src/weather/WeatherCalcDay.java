package weather;

import android.os.AsyncTask;

/**
 * Class for threading the http request 
 * of today weather for android.
 * 
 * @author Manuel Kapferer
 *
 */
public class WeatherCalcDay extends AsyncTask<String, Void, Weather>{
	JsonWeatherParser parser = new JsonWeatherParser();

	@Override
	protected Weather doInBackground(String... params) {
		String data = ((new WeatherHttpClient()).getWeatherData(params[0],1));
		return parser.getTodayWeather(data);
	}

}
