package weather;

/**
 * Container class for holding weather data.
 * Nothing special to mention, it's used for 
 * transforming and storing json data.
 * 
 * id:
 * 200 - 232 thunderstorms
 * 300 - 321 drizzle 
 * 500 - 531 rain
 * 600 - 622 snow
 * 700 - 771 atmosphere
 * 800 - 804 clouds
 * 900 - 906 extrems
 * 
 * @author Manuel Kapferer
 *
 */
public class Weather {
	private int id;				// id of the weather
	private int temperature;	// temperature in int and grad
	private String condition;	// string which condition
	private String description;	// descripes weather
	
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature-273;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAllDataString(){
		return " " + id + " " + condition + " " + description + " " + temperature;
		
	}
}
