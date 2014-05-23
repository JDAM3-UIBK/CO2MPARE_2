/**
 * 
 */
package code;

/**
 * @author $ Martin Haslinger
 *
 */
public class Cars {

	String mfr;
	String model;
	double fc;
	double co2;
	
	public Cars(String mfr, String model, double fc, double co2){
		setMfr(mfr);
		setModel(model);
		setFc(fc);
		setCo2(co2);
	}

	/**
	 * @return the mfr
	 */
	public String getMfr() {
		return mfr;
	}

	/**
	 * @param mfr the mfr to set
	 */
	public void setMfr(String mfr) {
		this.mfr = mfr;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the fc
	 */
	public double getFc() {
		return fc;
	}

	/**
	 * @param fc the fc to set
	 */
	public void setFc(double fc) {
		this.fc = fc;
	}

	/**
	 * @return the co2
	 */
	public double getCo2() {
		return co2;
	}

	/**
	 * @param co2 the co2 to set
	 */
	public void setCo2(double co2) {
		this.co2 = co2;
	}

}
