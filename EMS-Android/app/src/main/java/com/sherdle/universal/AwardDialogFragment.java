package com.sherdle.universal;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sherdle.universal.model.Constant_Awards_id;
import com.sherdle.universal.model.ItemsRecord;
import com.sherdle.universal.model.LocalUserVariable;
import com.sherdle.universal.model.MySingleton;
import com.sherdle.universal.providers.woocommerce.model.users.User;
import com.sherdle.universal.util.Log;

import java.util.HashMap;
import java.util.Map;

public class AwardDialogFragment extends DialogFragment {
private EditText StudentId,FirstName,LastName,FallAmount,SpringAmount , SummerAmount , WinterAmount;
private Spinner SchoolSpinner,YearSpinner,SportSpinner,LevelSpinner;
private Button SaveBtn,CancelBtn;
private TextView fallText , springText , summerText , winterText ;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.fragment_award_dialog,null);
        InitializeViews(view);
        addInfoForSpinner();

        // handle with year spinner when clicked  . . . . ;
        YearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hide_season(false);

                String yearName = LocalUserVariable.year.get(position);
                           Log.i("year name ",yearName);
                     for(int x = 0 ; x <LocalUserVariable.yearsItems.size() ; x++){
                         if( yearName.equals(LocalUserVariable.yearsItems.get(x).getName())){
                             String yearId = LocalUserVariable.yearsItems.get(x).getId();

                             for(int j = 0 ; j<LocalUserVariable.terms.size() ; j++){

                                 if(yearId.equals(LocalUserVariable.termsItems.get(j).getId())){
                                     String season  = LocalUserVariable.termsItems.get(j).getName();
                                     Log.i("term is ",season);
                                     // this method for hide views depending their term . . . . ;
                                     getSeasons(season);




                                 }
                             }



                         }
                     }




            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {
              hide_season(false);
            }
        });

        // for cancel button . . . ;
        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // for save button . . . . ;
        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ViewsFilled()) {
                    // check connection . . . ;
                    if(CheckConnection.isNetworkOnline(getActivity())){
                    passDataToDatabase();
                    }else {
                        Toast.makeText(getActivity(),"Please Check Your Connection ! ", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getActivity(), "Please Fill The Required Field !", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }
    private void InitializeViews(View view){
        StudentId=view.findViewById(R.id.add_id);
        FirstName=view.findViewById(R.id.add_fname);
        LastName=view.findViewById(R.id.add_lname);
        FallAmount=view.findViewById(R.id.add_fall_amount);
        SpringAmount=view.findViewById(R.id.add_spring_amount);
        SummerAmount = view.findViewById(R.id.summer_edit);
        WinterAmount = view.findViewById(R.id.winter_edit);
        summerText = view.findViewById(R.id.summer_label);
        winterText = view.findViewById(R.id.winter_label);
        SaveBtn=view.findViewById(R.id.add_save_btn);
        CancelBtn=view.findViewById(R.id.add_cancel_btn);
        SchoolSpinner=view.findViewById(R.id.add_school_spinner);
        YearSpinner=view.findViewById(R.id.add_year_spinner);
        SportSpinner=view.findViewById(R.id.add_sport_spinner);
        LevelSpinner=view.findViewById(R.id.add_level_spinner);
        fallText = view.findViewById(R.id.fall_label);
        springText = view.findViewById(R.id.spring_label);
    }

    private void hide_season(boolean visible){
        if(visible == true){
            fallText.setVisibility(View.VISIBLE);
            springText.setVisibility(View.VISIBLE);
            SpringAmount.setVisibility(View.VISIBLE);
            FallAmount.setVisibility(View.VISIBLE);
            WinterAmount.setVisibility(View.VISIBLE);
            winterText.setVisibility(View.VISIBLE);
            summerText.setVisibility(View.VISIBLE);
            SummerAmount.setVisibility(View.VISIBLE);
        }else{
            fallText.setVisibility(View.GONE);
            springText.setVisibility(View.GONE);
            SpringAmount.setVisibility(View.GONE);
            FallAmount.setVisibility(View.GONE);
            WinterAmount.setVisibility(View.GONE);
            winterText.setVisibility(View.GONE);
            summerText.setVisibility(View.GONE);
            SummerAmount.setVisibility(View.GONE);
            // set edit text empty . . . . ;
            FallAmount.setText("");
            SpringAmount.setText("");
            WinterAmount.setText("");
            SummerAmount.setText("");
        }

    }

    private void addInfoForSpinner(){
        ArrayAdapter<String>schoolAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item, LocalUserVariable.school);
        ArrayAdapter<String>yearAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item, LocalUserVariable.year);
        ArrayAdapter<String>sportAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item, LocalUserVariable.sport);
        ArrayAdapter<String>levelAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,new String[]{"Varsity","JV"});
        SchoolSpinner.setAdapter(schoolAdapter);
        YearSpinner.setAdapter(yearAdapter);
        SportSpinner.setAdapter(sportAdapter);
        LevelSpinner.setAdapter(levelAdapter);



    }

    private void passDataToDatabase(){
        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        StringRequest request=new StringRequest(Request.Method.POST, ContractUrl.ADD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                  if (response.contains("add successfull")){
                      MyDialogFragmentListener ll=(MyDialogFragmentListener)getActivity();
                      ll.setIsSaved(true);
                      dismiss();

                  }else {
                      Toast.makeText(getActivity(), "Something Was Wrong !", Toast.LENGTH_SHORT).show();
                  }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Something Was Error !", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("user_id",LocalUserVariable.userid);

                String schoolText=SchoolSpinner.getSelectedItem().toString();
                params.put(Constant_Awards_id.ADD_SCHOOL_NAME, ItemsRecord.searchForId(schoolText,LocalUserVariable.schoolsItems));

                String yearText=YearSpinner.getSelectedItem().toString();
                params.put(Constant_Awards_id.ADD_AWARD,ItemsRecord.searchForId(yearText,LocalUserVariable.yearsItems));

                params.put(Constant_Awards_id.ADD_STUDENT_id,StudentId.getText().toString());
                params.put(Constant_Awards_id.ADD_FIRST_NAME,FirstName.getText().toString());
                params.put(Constant_Awards_id.ADD_LAST_NAME,LastName.getText().toString());

                String sportText=SportSpinner.getSelectedItem().toString();
                params.put(Constant_Awards_id.ADD_SPORT,ItemsRecord.searchForId(sportText,LocalUserVariable.sportsItems));

                params.put(Constant_Awards_id.ADD_SPORT_LEVEL,LevelSpinner.getSelectedItem().toString());
                params.put(Constant_Awards_id.ADD_FALL,FallAmount.getText().toString());
                params.put(Constant_Awards_id.ADD_SPRING, SpringAmount.getText().toString());
                params.put(Constant_Awards_id.ADD_SUMMER,SummerAmount.getText().toString());
                params.put(Constant_Awards_id.ADD_WINTER,WinterAmount.getText().toString());
                return params;

            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }

   // create method to check view is empty or not  . . . ;
    private boolean ViewsFilled(){
        String std__id=StudentId.getText().toString();
        String Fname=FirstName.getText().toString();
        String Lname=LastName.getText().toString();



        if(std__id.isEmpty()||Fname.isEmpty()||Lname.isEmpty()){
            return false;
        }
        return true;
    }


    //1 is for fall, 2 is for winter, 3 is for spring , 4 is for summer
    // method to get season depended of term . . . . ;
    private void getSeasons(String term){
        switch (term){
            case "1":
                fallText.setVisibility(View.VISIBLE);
                FallAmount.setVisibility(View.VISIBLE);
                break;
            case "2":
                winterText.setVisibility(View.VISIBLE);
                WinterAmount.setVisibility(View.VISIBLE);
                break;
            case "3":
                springText.setVisibility(View.VISIBLE);
                SpringAmount.setVisibility(View.VISIBLE);
                break;
            case "4":
                summerText.setVisibility(View.VISIBLE);
                SummerAmount.setVisibility(View.VISIBLE);
                break;
        }


    }
}
