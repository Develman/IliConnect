package com.example.ilias;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.ilias.R;


public class Suche extends Fragment {
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
	
	    LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(R.layout.suche_layout,
                container, false);
//        Button kurse = (Button) mLinearLayout.findViewById(R.id.show_dozent_course);
//        kurse.setOnClickListener(new View.OnClickListener() {
//	        public void onClick(View v) {
//		    	Intent i = new Intent();	
//			    i.setClass(getActivity(), Dozent_kurse.class);
//			    i.putExtra("selected","Prof. Dr. rer. nat. Fa�bender");		
//			    startActivity(i);
//	    	}
//        });
        Button suche = (Button) mLinearLayout.findViewById(R.id.search_button);
        suche.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				ProgressDialog searching = ProgressDialog.show(getActivity(), "Suche", "Bitte Warten....", true);
				
			}
		});
        
        //liste f�r kurse
//        List<String> valueList = new ArrayList<String>();
//		valueList.add("Physik");
//		
//		 ListAdapter fileList = new ArrayAdapter<String>( getActivity(), R.layout.black_list_item, valueList);	 
//		  final ListView lv = (ListView) mLinearLayout.findViewById(R.id.kurs_list);
//		  lv.setAdapter(fileList);
//			lv.setOnItemClickListener(new OnItemClickListener(){
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
//			{			
//				showAlertMessage();
//			}
//			});
			//liste f�r dozenten
		 List<String> valueList2 = new ArrayList<String>();
		 valueList2.add("Prof. Dr. rer. nat. Fa�bender");
				
			ListAdapter fileList2 = new ArrayAdapter<String>( getActivity(), R.layout.black_list_item, valueList2);	 
			final ListView lv2 = (ListView) mLinearLayout.findViewById(R.id.dozent_list);
			lv2.setAdapter(fileList2);
			lv2.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{			
				Intent i = new Intent();	
			    i.setClass(getActivity(), Dozent_kurse.class);
			    i.putExtra("selected","Prof. Dr. rer. nat. Fa�bender");		
			    startActivity(i);
			}
			});
        
//        Button join = (Button) mLinearLayout.findViewById(R.id.kurs_beitreten);
//        join.setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				showAlertMessage();
//			}
//		});


        return mLinearLayout;
		
		
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		 
		  
		 
		 }
	
	
	private void showAlertMessage(){
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
    	alertDialog.setIcon(android.R.drawable.ic_dialog_info);
    	alertDialog.setTitle("Kursanmeldung!");
    	alertDialog.setMessage("Wollen Sie sich zu dem Kurs: Physik anmelden?");
    	alertDialog.setPositiveButton("Anmelden", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
    	alertDialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), "abbruch", Toast.LENGTH_LONG).show();
			}
		});
    	AlertDialog alertDialog1= alertDialog.create();
    	alertDialog1.show();
    }  
}