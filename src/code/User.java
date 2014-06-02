package code;

/**
 * This class which represents our
 * Costumer/user entity, in annotation form.
 * @author Manuel Kapferer
 *
 */
import java.io.Serializable;

import flexjson.JSONSerializer;




public class User implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//@Column(name="COSTUMER_NAME", nullable = true)
	private String user_name_id;			///id of the user
	//@Column(name="PASSWORD", nullable = true)
	private String user_pw;					///pw of user
	
	/**
	 * empty constructor for hibernate
	 */
	public User() {
	}

	/**
	 * constructor to create a costumer
	 * @param user_name_id 
	 * 			user nickname
	 * @param user_pw 
	 * 			password of user
	 */
	public User(String user_name_id, String user_pw) {
		super();
		this.user_name_id = user_name_id;
		this.user_pw = user_pw;
	}

	public String getUser_name_id() {
		return user_name_id;
	}

	public void setUser_name_id(String user_name_id) {
		this.user_name_id = user_name_id;
	}

	public String getUser_pw() {
		return user_pw;
	}

	public void setUser_pw(String user_pw) {
		this.user_pw = user_pw;
	}

	@Override
	public String toString() {
		return "User [user_name_id=" + user_name_id + ", user_pw=" + user_pw
				+ "]";
	}
	public String toJson() {
		   return new JSONSerializer().exclude("*.class").serialize(this);
	}
	//um methoden von Json Erstellung auszuschließen  @JSON(include=false)
	//http://flexjson.sourceforge.net/
}