package weather;
/**
 * Asset class for checking if Internet 
 * connection is available for http request.
 * 
 * @Important For the use someone has to set the permissions
 * of in AndroidManifest: 	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 *							<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
 * Also using if("isNetworkAvailable") in the Activity 
 * for the WeatherFacade methods.
 * 
 * @author Manuel Kapferer
 */
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetection {
	
	/**
	 * method to check if the user has enabled Internet connection
	 * @param ctx
	 * 			context activity for checking Internet connection, example MainActivity
	 * @return
	 * 			true if connected else false
	 */
	public static boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		
		if (netInfo != null && netInfo.isConnectedOrConnecting()
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}

}
