package com.co2mpare.fragments;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import code.Controller;
import code.LoggedRoute;
import code.SettingsObject;

import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.DynamicTableModel;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.co2mpare.R;
import com.co2mpare.androidplot.MultitouchPlot;

public class Chart extends Fragment{

	private static final String ARG_SECTION_NUMBER = "section_number1";

	 private MultitouchPlot plot;
	 private RadioButton rdbtnmoney, rdbtnco2;
	 String tocompare="Money";
	 Number[] series1Numbers;
	 Number[] series2Numbers;
	 Controller controller=Controller.getInstance();
	 LineAndPointFormatter series1Format,series2Format;
	 View rootView;
	 ArrayList<LoggedRoute> allRoutes=new ArrayList<LoggedRoute>();
	 SettingsObject settings=SettingsObject.getInstance();
	 TextView saved;
	 
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static Chart newInstance(int sectionNumber) {
		Chart fragment = new Chart();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public Chart() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 rootView = inflater.inflate(R.layout.fragment_chart, container,
				false);
		
		Paint pen=new Paint();
		Paint pen2=new Paint();
		pen.setTextSize(12 * getResources().getDisplayMetrics().density);
		pen.setColor(Color.WHITE);
		pen2.setColor(Color.BLACK);
		
		rdbtnmoney=(RadioButton) rootView.findViewById(R.id.rdbtnmoney);
		rdbtnco2=(RadioButton) rootView.findViewById(R.id.rdbtnco2);
		rdbtnmoney.setChecked(true);
		saved=(TextView) rootView.findViewById(R.id.saved);
		
			plot = (MultitouchPlot) rootView.findViewById(R.id.multiplot);
		
		
        
        // setup our xyplot, 
		plot.getBackgroundPaint().setColor(Color.TRANSPARENT);						// set background transparent
		plot.setBorderStyle(XYPlot.BorderStyle.NONE, null, null);
		plot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT); 		// set graph background transparent
		plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.TRANSPARENT);   // set grid graph background transparent
        plot.getGraphWidget().setDomainLabelWidth((float) 50);						// lifts the bottom 50 up
        plot.getGraphWidget().getDomainLabelPaint().setTextSize(9 * getResources().getDisplayMetrics().density);					// sets bottom text size 20
        plot.getGraphWidget().getRangeLabelPaint().setTextSize(9 * getResources().getDisplayMetrics().density);					// sets Left text size 20
        plot.getGraphWidget().getRangeLabelPaint().setColor(Color.TRANSPARENT);
        plot.getGraphWidget().getDomainLabelPaint().setColor(Color.TRANSPARENT);
        
        // ************************************* legend changes *****************************
        // use a 2x2 grid:
        plot.getLegendWidget().setTableModel(new DynamicTableModel(2, 1));
 
        // adjust the legend size so there is enough room
        // to draw the new legend grid:
        plot.getLegendWidget().setSize(new SizeMetrics(100, SizeLayoutType.ABSOLUTE, 450, SizeLayoutType.ABSOLUTE));
        plot.getLegendWidget().setTextPaint(pen);
 
        // add a semi-transparent black background to the legend
        // so it's easier to see overlaid on top of our plot:
        Paint bgPaint = new Paint();
        bgPaint.setColor(Color.BLACK);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setAlpha(140);
        plot.getLegendWidget().setBackgroundPaint(bgPaint);
 
        // adjust the padding of the legend widget to look a little nicer:
        plot.getLegendWidget().setPadding(1, 1, 1, 1);      
 
        // reposition the grid so that it rests above the bottom-left
        // edge of the graph widget:
        plot.getLegendWidget().position(
                20,
                XLayoutStyle.ABSOLUTE_FROM_LEFT,
                80,
                YLayoutStyle.ABSOLUTE_FROM_TOP,
                AnchorPosition.LEFT_TOP);

       
        
        // Create a couple arrays of y-values to plot:
        // Here you can implement "fromArrayListToNumberArray" method instead
        // of fix numbers.
       
        series1Numbers=allocateNumberArray(allRoutes.size());
        series2Numbers=allocateNumberArray(allRoutes.size());
        
       for(int i=0;i<allRoutes.size();i++){
        	series1Numbers[i]=allRoutes.get(i).getCosts();
        	
        }
        for(int j=0;j<allRoutes.size();j++){
        	series2Numbers[j]=allRoutes.get(j).getReferencecosts();
        }
        
       
        
        rdbtnco2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				tocompare="CO2";
				for(int i=0;i<allRoutes.size();i++){
		        	series1Numbers[i]=allRoutes.get(i).getCO2();
		        }
		        for(int i=0;i<allRoutes.size();i++){
		        	series2Numbers[i]=allRoutes.get(i).getReferenceco2();
		        }
				
				plot.clear();
				graph(tocompare);
				plot.redraw();
				setSavedTextView(false);
			}
		});
        
        rdbtnmoney.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				tocompare="Money";
				
				for(int i=0;i<allRoutes.size();i++){
		        	series1Numbers[i]=allRoutes.get(i).getCosts();
		        }
		        for(int i=0;i<allRoutes.size();i++){
		        	series2Numbers[i]=allRoutes.get(i).getReferencecosts();
		        }
				
				plot.clear();
				graph(tocompare);
				plot.redraw();
				setSavedTextView(true);
			}
		});
        
              
        
               
        // reduce the number of range labels
        plot.setTicksPerRangeLabel(3);
        plot.getGraphWidget().setDomainLabelOrientation(-45);

		
		return rootView;
	}
	
	public Number[] allocateNumberArray(int length){
		Number[] number=new Number[length];
		return number;
	}
	
	public void setSavedTextView(boolean money){
		NumberFormat n=NumberFormat.getInstance();  
        n.setMaximumFractionDigits(2);
        
		if(money){
			saved.setText("You saved "+(n.format(calcSavedValues(money)*100/100.00))+" EUR. Congratulation!");
		}else{
			saved.setText("You saved "+(n.format((calcSavedValues(money)*100/100.00)/1000))+" kg CO2. Congratulation!\n"+"This is the amount "+(n.format(inBeeches()))+" beeches can "
					+ "bind in one year.");
		}
	}
	
	public double inBeeches(){
		return ((calcSavedValues(false)*100/100.00)/1000)/12.5;
	}
	
	public void graph(String tocompare){
		 	series1Format = new LineAndPointFormatter();
	        //series1Format.setPointLabelFormatter(new PointLabelFormatter());
	        series1Format.configure(rootView.getContext(),
	                R.xml.line_point_formatter_with_plf1);
	        
	        
	        series2Format = new LineAndPointFormatter();
	        //series2Format.setPointLabelFormatter(new PointLabelFormatter());
	        series2Format.configure(rootView.getContext(),
	                R.xml.line_point_formatter_with_plf2);


	        XYSeries series1 = new SimpleXYSeries(
	                Arrays.asList(series1Numbers),          // SimpleXYSeries takes a List so turn our array into a List
	                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
	                tocompare);
	        
	        
	        XYSeries series2 = new SimpleXYSeries(Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Reference");
			 
			plot.addSeries(series1, series1Format);
			plot.addSeries(series2, series2Format);
	        
	}
	
	public double calcSavedValues(boolean money){
		double[] values=new double[2];	//0: ist-Wert, 1: Referenz
		if(money){
			for(int i=0;i<allRoutes.size();i++){
				values[0]=values[0]+allRoutes.get(i).getCosts();
			}
			for(int j=0;j<allRoutes.size();j++){
				values[1]=values[1]+allRoutes.get(j).getReferencecosts();
			}
		}else{
			for(int i=0;i<allRoutes.size();i++){
				Log.e("All co2:",""+allRoutes.get(i).getCO2());
				values[0]=values[0]+allRoutes.get(i).getCO2();
			}
			for(int j=0;j<allRoutes.size();j++){
				values[1]=values[1]+allRoutes.get(j).getReferenceco2();
			}
		}
		
		return values[1]-values[0];
	}
	
	
	
	 //Alle geloggten Routen abrufen
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	  super.setUserVisibleHint(isVisibleToUser);
	  if (isVisibleToUser) {
		  
		  if(settings.isLoggedIn()){
			  
			  allRoutes=controller.getAllLoggedRoutes(new LoggedRoute(settings.getUserName(), 0, 0, tocompare, 0.0, 0.0, null, 0, 0.0, 0.0));
			  tocompare="Money";
			  series1Numbers=allocateNumberArray(allRoutes.size());
			  series2Numbers=allocateNumberArray(allRoutes.size());
			  if(series1Numbers.length!=0){
				for(int i=0;i<allRoutes.size();i++){
		        	series1Numbers[i]=allRoutes.get(i).getCosts();
		        }
		        for(int i=0;i<allRoutes.size();i++){
		        	series2Numbers[i]=allRoutes.get(i).getReferencecosts();
		        }
				
				plot.clear();
				graph(tocompare);
				plot.redraw();
				setSavedTextView(true);
			  }else{
				  series1Numbers=allocateNumberArray(1);
				  series2Numbers=allocateNumberArray(1);
				  series1Numbers[0]=0;
				  series2Numbers[0]=0;
				  plot.clear();
					graph(tocompare);
					plot.redraw();
				  Toast toast=new Toast(rootView.getContext());
				  toast.makeText(rootView.getContext(),"No logged routes yet.",Toast.LENGTH_LONG).show();
			  }
		  }else{
			  Toast toast=new Toast(rootView.getContext());
			  toast.makeText(rootView.getContext(),"You need to be logged in to show your chart.",Toast.LENGTH_LONG).show();
		  }
		 
	  }
	}
	
		
}
