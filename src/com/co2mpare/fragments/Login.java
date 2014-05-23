package com.co2mpare.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.co2mpare.MainActivity;
import com.co2mpare.R;

public class Login extends Activity{
	
	Button loginbtn;
	EditText user,pw;
	String userStr="",pwStr="";
	TextView guest, register;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        // Be sure to call the super class.
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // See assets/res/any/layout/translucent_background.xml for this
        // view layout definition, which is being set here as
        // the content of our screen.
        setContentView(R.layout.fragment_login);
       loginbtn=(Button)findViewById(R.id.loginBTN);
       user=(EditText)findViewById(R.id.UserTF);
       pw=(EditText)findViewById(R.id.PasswordTF);
       guest=(TextView)findViewById(R.id.guestBTN);
       register=(TextView)findViewById(R.id.register);
       
       /**
        * 
        * LOGIN as registered USER
        * 
        */
       
       loginbtn.setOnClickListener(new OnClickListener(){
     
		@Override
		public void onClick(View v) {
			
			if(user.getText().toString().equals("")&&pw.getText().toString().equals("")){
				Toast toast=new Toast(v.getContext());
				toast.makeText(v.getContext(),"Warning: Please enter Username and Password or continue as guest.",Toast.LENGTH_LONG).show();
			}else{
				userStr=user.getText().toString();
				pwStr=pw.getText().toString();	
				Intent intent=new Intent(v.getContext(), MainActivity.class);
				v.getContext().startActivity(intent);
				Log.e("User:",userStr);
			}
						
		}
    	   
       });
       
       
       /**
        * 
        * LOGIN as unregistered USER - guest
        * 
        */
       guest.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intent=new Intent(v.getContext(), MainActivity.class);
			v.getContext().startActivity(intent);
		}
    	   
       });
       
       /**
        * 
        * Register as new User
        * 
        */
       
       register.setOnClickListener(new OnClickListener(){

   			@Override
   			public void onClick(View v) {
   				if(user.getText().toString().equals("")&&pw.getText().toString().equals("")){
   					Toast toast=new Toast(v.getContext());
   					toast.makeText(v.getContext(),"Just enter your desired username and password above.",Toast.LENGTH_LONG).show();
   				}else{
   					//New registration
   					
   					Toast toast=new Toast(v.getContext());
   					toast.makeText(v.getContext(),"Registration successfully completed.",Toast.LENGTH_LONG).show();
   					Intent intent=new Intent(v.getContext(), MainActivity.class);
   					v.getContext().startActivity(intent);
   				}
   			}
       	   
       });
       
    }
}
