package code;

import java.io.Serializable;
import java.util.Date;

import flexjson.JSONSerializer;


public class LoggedRoute implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	private int duration;
	private String userName;
	private int length;
	private String type;
	//private ArrayList<ArrayList<LatLng>> polylinePoints; //google maps route -> noch konvertierung in polyline (PTE?)
	//private String text[]; //PTE routen text -> wie IVB app
	private double CO2;
	private double costs;
	private Date date;
	private int referencelength;
	private double referenceco2;
	private double referencecosts;
	
	public LoggedRoute(String userName, int duration, int length, String type, double CO2, double costs, Date date,
			int referencelength, double referenceco2, double referencecosts){
		this.duration = duration;
		this.length = length;
		this.type = type;
		//this.polylinePoints = polylinePoints;
		//this.text = text;
		this.CO2 = CO2;
		this.costs = costs;
		this.date = date;
		this.referencelength=referencelength;
		this.referenceco2=referenceco2;
		this.referencecosts=referencecosts;
		this.userName=userName;
	}
	
	

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/*public ArrayList<ArrayList<LatLng>> getPolylinePoints() {
		return polylinePoints;
	}

	public void setPolylinePoints(ArrayList<ArrayList<LatLng>> polylinePoints) {
		this.polylinePoints = polylinePoints;
	}*/

	/*public String[] getText() {
		return text;
	}

	public void setText(String[] text) {
		this.text = text;
	}*/

	public double getCO2() {
		return CO2;
	}

	public void setCO2(double cO2) {
		CO2 = cO2;
	}

	public double getCosts() {
		return costs;
	}

	public void setCosts(double costs) {
		this.costs = costs;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "duration="+duration+" distance="+length+" type="+type+" polyline=xxx text=xxx Co2="+CO2+" costs="+costs;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getReferencelength() {
		return referencelength;
	}

	public void setReferencelength(int referencelength) {
		this.referencelength = referencelength;
	}

	public double getReferenceco2() {
		return referenceco2;
	}

	public void setReferenceco2(double referenceco2) {
		this.referenceco2 = referenceco2;
	}

	public double getReferencecosts() {
		return referencecosts;
	}

	public void setReferencecosts(double referencecosts) {
		this.referencecosts = referencecosts;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String toJson() {
		   return new JSONSerializer().exclude("*.class").serialize(this);
	}
}
