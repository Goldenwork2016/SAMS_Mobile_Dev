package com.sherdle.universal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sherdle.universal.model.DialogDashboardListener;
import com.sherdle.universal.model.ItemsRecord;
import com.sherdle.universal.model.LocalUserVariable;
import com.sherdle.universal.model.MySingleton;
import com.sherdle.universal.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class YearDashboardDialog extends DialogFragment implements View.OnClickListener {
Button saveButton , closeButton ;
ListView listView ;
String selectedYear ="";
    CheckedTextView checkedTextView ;
    View view;
public static YearDashboardDialog getInstance(String selected_year){
    Bundle bundle = new Bundle();
    bundle.putString("selected_year",selected_year);
    YearDashboardDialog dashboardDialog = new YearDashboardDialog();
    dashboardDialog.setArguments(bundle);
    return dashboardDialog ;

}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         view = LayoutInflater.from(getActivity()).inflate(R.layout.year_dashboard_dialog,null);
        selectedYear = getArguments().getString("selected_year");
        // to get selected year and put him inside variable . . . .;

        initalizeViews(view);

     saveButton.setOnClickListener(this);
     closeButton.setOnClickListener(this);

     // for list view ( multiple choice ) . . . . . ;
        // create adapter for year item . . . ;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                view.getContext(),
                android.R.layout.simple_list_item_multiple_choice,
                LocalUserVariable.year);




        // check if year is empty or  . . . .;
        if (LocalUserVariable.year.size() == 0 ){
            saveButton.setEnabled(false);
        }else {
            saveButton.setEnabled(true);
        }

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        setSelectedYear(selectedYear);

        return new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setView(view)
                .create() ;
    }
    private void initalizeViews(View view){
        saveButton=view.findViewById(R.id.save_year_dash);
        closeButton=view.findViewById(R.id.close_year_dash);
        listView = view.findViewById(R.id.list_year);
        checkedTextView = view.findViewById(R.id.text1);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.save_year_dash :
                // to save year for dashboard . . . . ;
                String year = getSelecteItemFromList() ;

                putYearInsideDatabase(year);
                  dismiss();

                break;
            case R.id.close_year_dash :
                 this.dismiss();
                break;
        }

    }

    private  void putYearInsideDatabase(String year){

        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.POST, ContractUrl.ADD_YEAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // put callback to refresh previous activity when changed data from server . . . . ;
                Toast.makeText(view.getContext(),"Add Successfull !", Toast.LENGTH_SHORT).show();
                DialogDashboardListener dialogListener =(DialogDashboardListener)view.getContext();
                dialogListener.isDismiss();
                 dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error while connect with database " , error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params = new HashMap<>();
                params.put("user_id",LocalUserVariable.userid);
                params.put("yearsFilterHome",year);
                Log.i("year in yearsss",year);
                return params ;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }


    public void setSelectedYear(String selectedYear){

    // split string to an array . . . . . ;
        String []years =selectedYear.split(",");
        for(int i=0 ;i<years.length;i++){

            // check if the list view contain this text . . . . ;
            for(int a=0 ; a<LocalUserVariable.year.size();a++){
                if(years[i].equals(LocalUserVariable.year.get(a))){
                    listView.setItemChecked(a,true);

                }
            }
        }

    }





    public String getSelecteItemFromList(){

        String selected = "";



        int cntChoice = listView.getCount();

        SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();

        for(int i = 0; i < cntChoice; i++){

            if(sparseBooleanArray.get(i)) {
                String yearText = listView.getItemAtPosition(i).toString();
               // String yearId =  ItemsRecord.searchForId(yearText,LocalUserVariable.yearsItems);
                selected +=yearText + ",";
                Log.i("selected is ",selected);

            }

        }




        return deleteLastComma(selected);
    }
    public String deleteLastComma(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
