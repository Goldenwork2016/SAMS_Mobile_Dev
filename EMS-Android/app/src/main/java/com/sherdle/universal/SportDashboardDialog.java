package com.sherdle.universal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SportDashboardDialog extends DialogFragment implements View.OnClickListener {

   private ListView listSport ;
   private Button saveBtn  , closeBtn ;
   private String sports_id ="";
   private List<String> sportsFormatted ;
   private String selected_sport="" ;
   private View view ;

   // if we need to put argument . .. . . . ;
   public static SportDashboardDialog getInstance(String sport_ids,String selected_sport){
       SportDashboardDialog dialog = new SportDashboardDialog();

       Bundle bundle = new Bundle();
       bundle.putString("selected_sport",selected_sport);
       bundle.putString("sport_id",sport_ids);
       dialog.setArguments(bundle);

       return dialog ;
   }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        sportsFormatted = new ArrayList<>();
       sports_id = getArguments().getString("sport_id");
       selected_sport = getArguments().getString("selected_sport");
         view = LayoutInflater.from(getActivity()).inflate(R.layout.sport_dashboard_dialog,null);
        // test sport ide's  . . . ;
        initializeViews(view);

        saveBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);

        // split data from variable and put in adrray adapter . . . . ;

        String[]sport_split = sports_id.split(",");

        for(int i =0 ;i<sport_split.length ; i++){
            sportsFormatted.add(ItemsRecord.searchForName(sport_split[i], LocalUserVariable.sportsItems));
        }

       ArrayAdapter<String>sportAdapter = new ArrayAdapter<String>(view.getContext()
                ,android.R.layout.simple_list_item_multiple_choice,
                sportsFormatted);

        // check if sports is empty or filled . . . .;
        if (sportsFormatted.size() == 0 ){
            saveBtn.setEnabled(false);
        }else {
            saveBtn.setEnabled(true);
        }

        listSport.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listSport.setAdapter(sportAdapter);
                setSelectedSport(selected_sport);








        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setCancelable(true)
                .create() ;
    }


    // ihitialize views . . . . . ;
    private void initializeViews(View view){
        listSport = view.findViewById(R.id.list_sport);
        saveBtn = view.findViewById(R.id.save_sport_dash);
        closeBtn = view.findViewById(R.id.close_sport_dash);

    }

    // for onclick handle event on views . . . . ;
    @Override
    public void onClick(View v) {
       int id = v.getId();
       switch (id){
           case R.id.save_sport_dash :
               String selected = getSelecteItemFromList();
               if(selected == "" || selected == null){
                   selected = "0";
               }
               putSportsinDatabase(selected);
               this.dismiss();
               break;
           case R.id.close_sport_dash :
           this.dismiss();
               break;
       }

    }

    public void setSelectedSport(String selectedYear){

        // split string to an array . . . . . ;
        String []sports =selectedYear.split(",");
        for(int i=0 ;i<sports.length;i++){

            // check if the list view contain this text . . . . ;
            for(int a=0 ; a<sportsFormatted.size();a++){
                if(sports[i].equals(ItemsRecord.searchForId(sportsFormatted.get(a),LocalUserVariable.sportsItems))){
                    listSport.setItemChecked(a,true);

                }
            }
        }

    }
    public String getSelecteItemFromList() {

        String selected = "";


        int cntChoice = listSport.getCount();

        SparseBooleanArray sparseBooleanArray = listSport.getCheckedItemPositions();

        for (int i = 0; i < cntChoice; i++) {

            if (sparseBooleanArray.get(i)) {
                String sportText = listSport.getItemAtPosition(i).toString();
                String sportId = ItemsRecord.searchForId(sportText, LocalUserVariable.sportsItems);
                selected += sportId + ",";
                Log.i("selected is ", selected);

            }

        }
        return deleteLastComma(selected);
    }

    private void putSportsinDatabase(String ss_sport){

        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.POST, ContractUrl.ADD_SPORT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
           Log.i("SportDialog",response);
                Toast.makeText(view.getContext(), "Add Successfull !", Toast.LENGTH_SHORT).show();
                DialogDashboardListener listener = (DialogDashboardListener)view.getContext();
                listener.isDismiss();
                dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             Log.e("sportDialog",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params = new HashMap<>();
                params.put("user_id",LocalUserVariable.userid);
                params.put("sportsFilterHome",ss_sport);
                Log.i("sport filter is ",ss_sport);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    public String deleteLastComma(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
