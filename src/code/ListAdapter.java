package code;
import java.util.ArrayList;
  


import com.co2mpare.R;
  


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
  
public class ListAdapter extends BaseAdapter {
    private static ArrayList<Route> searchArrayList;
  
    private LayoutInflater mInflater;
    SettingsObject settings=SettingsObject.getInstance();
  
    public ListAdapter(Context context, ArrayList<Route> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }
  
    public int getCount() {
        return searchArrayList.size();
    }
  
    public Object getItem(int position) {
        return searchArrayList.get(position);
    }
  
    public long getItemId(int position) {
        return position;
    }
  

    
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_row_view, null);
            holder = new ViewHolder();
            holder.txtvehicle = (TextView) convertView.findViewById(R.id.vehicle);
            holder.txtmoney = (TextView) convertView.findViewById(R.id.money);
            holder.txtco2 = (TextView) convertView.findViewById(R.id.co2cons);
            holder.txttime = (TextView) convertView.findViewById(R.id.time);
            holder.txtdistance = (TextView) convertView.findViewById(R.id.distance);
         
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        double co2car=settings.getCO2();
        double verbrauch=settings.getVerbrauch();
        double co2pte=0.0;
        
        if(searchArrayList.get(position).getType().equals("driving")){
        	holder.txtvehicle.setText("Car");
        	holder.txtvehicle.setTextColor(Color.RED);
        	holder.txtco2.setText(Math.round(((((searchArrayList.get(position).getLength()/1000)*co2car/1000))*100)/100.00)+" kg CO2");
        	holder.txtmoney.setText(Math.round(((searchArrayList.get(position).getLength()/1000)*(verbrauch/100*1.4))*100)/100.00+" EUR");
        }else if(searchArrayList.get(position).getType().equals("PTE")){
        	holder.txtvehicle.setText("Public");
        	holder.txtco2.setText(Math.round((((searchArrayList.get(position).getLength()/1000)*co2pte/1000)*100)/100.00)+" kg CO2");
        	holder.txtmoney.setText(1.80+" EUR");
        }else if(searchArrayList.get(position).getType().equals("bicycling")){
        	holder.txtvehicle.setText("Bike");
        	holder.txtvehicle.setTextColor(Color.rgb(0, 162, 4));
        	holder.txtco2.setText(0.0+" kg CO2");
        	holder.txtmoney.setText(0+" EUR");
        }else{
        	holder.txtvehicle.setText(searchArrayList.get(position).getType());
        }
        
        
        
        
        
        if(searchArrayList.get(position).getDuration()>3600){
        	int hour=searchArrayList.get(position).getDuration()/3600;
        	int minute=(searchArrayList.get(position).getDuration()%3600)/60;
        	int sec=searchArrayList.get(position).getDuration()%60;
        	holder.txttime.setText(hour+" h "+minute+" min "+sec+" sec");
        }else if(searchArrayList.get(position).getDuration()>60){
        	holder.txttime.setText(searchArrayList.get(position).getDuration()/60+" min "+searchArrayList.get(position).getDuration()%60+" sec");
        }else{
        	holder.txttime.setText(searchArrayList.get(position).getDuration()+" sec");
        }
        Log.e("Kontrolle:",""+((searchArrayList.get(position).getDuration()+" sec")));
        if(searchArrayList.get(position).getLength()>1000){
        	holder.txtdistance.setText(searchArrayList.get(position).getLength()/1000+" km "+searchArrayList.get(position).getLength()%1000+" m");
        }else{
        	holder.txtdistance.setText(searchArrayList.get(position).getLength()+" m");
        }
        Log.e("Kontrolle 2:",""+((searchArrayList.get(position).getLength()+" m")));
        
        
        
        return convertView;
    }
  
    static class ViewHolder {
        TextView txtvehicle;
        TextView txtmoney;
        TextView txtco2;
        TextView txttime;
        TextView txtdistance;
    }
    
    public void refill(ArrayList<Route> searchArrayList) {
        this.searchArrayList=searchArrayList;
        
        notifyDataSetChanged();
    }
}