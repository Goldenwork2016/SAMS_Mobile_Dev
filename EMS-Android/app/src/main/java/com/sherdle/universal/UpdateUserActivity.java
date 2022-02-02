package com.sherdle.universal;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.sherdle.universal.model.AwardInformation;
import com.sherdle.universal.model.AwardsInformationLab;
import com.sherdle.universal.model.Constant_Awards_id;
import com.sherdle.universal.model.ItemsRecord;
import com.sherdle.universal.model.LocalUserVariable;
import com.sherdle.universal.model.MySingleton;
import com.sherdle.universal.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sherdle.universal.ContractUrl.UPDATE_AWARD_ACTIVE_STATUS;

public class UpdateUserActivity extends AppCompatActivity implements View.OnClickListener {
private Toolbar toolbar;
private String id;
private Spinner schoolSpinner,yearSpinner,sportSpinner,levelSpinner;
private EditText studentId, firstName,lastName,fallAmount,springAmount,summerAmount,winterAmount;
private TextView fallText , springText , summerText , winterText ;
private Button editButton ,approveBtn , denyBtn;
private boolean editEnabled=false;
private boolean isUpdated=false;
    AwardInformation information ;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id= getIntent().getStringExtra("std_id");
        setContentView(R.layout.activity_update);
        InitializeActionBar();
        InitailizeViews();
        putDataInsideViews(id);
        putDataInsideSpinner();
        enabledViews(false);
         visiblePermission();

         // when click on approve or deny button . ..  ;
        denyBtn.setOnClickListener(this);
        approveBtn.setOnClickListener(this);
        // when selected item in year spinner . . . . ;
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void InitializeActionBar() {
     toolbar=findViewById(R.id.update_toolbar);
     setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));


    }

    // check if approve and deny permission type is not equal "0" then visisble button . . .;
    private void visiblePermission(){
        Log.i("active","active status is "+information.getAward_active_status());
        if(!LocalUserVariable.permission_approve.equals("0") && information.getAward_active_status().equals("1")){
            approveBtn.setVisibility(View.VISIBLE);
        }else{
            approveBtn.setVisibility(View.INVISIBLE);

        }
        if(!LocalUserVariable.permission_deny.equals("0") && information.getAward_active_status().equals("1")){
            denyBtn.setVisibility(View.VISIBLE);
        }else{
            denyBtn.setVisibility(View.INVISIBLE);

        }
    }

    // for initialize view and cast(if required) . . .;
    private void InitailizeViews(){
        //fallText , springText , summerText , winterText ;
         schoolSpinner=findViewById(R.id.school_spinner);
         yearSpinner=findViewById(R.id.year_spinner);
         sportSpinner=findViewById(R.id.sport_spinner);
         levelSpinner=findViewById(R.id.level_spinner);
         firstName=findViewById(R.id.edit_fname);
         lastName=findViewById(R.id.edit_lname);
         fallAmount=findViewById(R.id.edit_fall_ammount);
         summerAmount=findViewById(R.id.edit_summer_amount);
         winterAmount=findViewById(R.id.edit_winter_amount);
         springAmount=findViewById(R.id.edit_spring_amount);
         studentId=findViewById(R.id.edit_id);
         editButton=findViewById(R.id.award_save);
         fallText=findViewById(R.id.label_fall_amount);
         springText=findViewById(R.id.label_spring_amount);
         summerText=findViewById(R.id.label_summer_amount);
         winterText=findViewById(R.id.label_winter_amount);
         approveBtn = findViewById(R.id.approve_btn);
         denyBtn = findViewById(R.id.deny_btn);


    }

    private void putDataInsideSpinner(){
         information=AwardsInformationLab.getInstance().getStudentInformation(id);
        ArrayAdapter<String> sportAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,LocalUserVariable.sport);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, LocalUserVariable.year);
        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, LocalUserVariable.school);
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,new String[]{"JV","Varsity"});
        schoolSpinner.setAdapter(schoolAdapter);
        yearSpinner.setAdapter(yearAdapter);
        sportSpinner.setAdapter(sportAdapter);
        levelSpinner.setAdapter(levelAdapter);

        // set selected item . . . ;
        if (information.getSchool() != null&&information.getSport()!=null&&information.getYear()!=null) {

            int schoolPosition = schoolAdapter.getPosition(information.getSchool());
            int sportPosition=sportAdapter.getPosition(information.getSport());
            int yearPosition=yearAdapter.getPosition(information.getYear());
            int levelPostition=levelAdapter.getPosition(information.getLevel());
            schoolSpinner.setSelection(schoolPosition);
            sportSpinner.setSelection(sportPosition);
            yearSpinner.setSelection(yearPosition);
            levelSpinner.setSelection(levelPostition);
        }

    }

    // to put student information in views . . . ;
    private void putDataInsideViews(String id){
        AwardInformation information=AwardsInformationLab.getInstance().getStudentInformation(id);
        studentId.setText(information.getStudentId());
        firstName.setText(information.getName());
        lastName.setText(information.getLastName());
        fallAmount.setText(information.getFall());
        springAmount.setText(information.getSpring());
        winterAmount.setText(information.getWinters());
        summerAmount.setText(information.getSummer());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_menu,menu);

        if(LocalUserVariable.permission_edit.equals("0")){
            menu.findItem(R.id.menu_edits).setVisible(false);
            return true ;
        }else {
            menu.findItem(R.id.menu_edits).setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home :
                this.finish();
                return true;
            case R.id.menu_edits:
                if(editEnabled==false){
                // Todo ;  complete the enabled views  . . .  ;
                enabledViews(true);
                editButton.setVisibility(View.VISIBLE);
                editEnabled=true;
                item.setIcon(R.drawable.ic_cancel);
                // hide deny and approve button if edit is enabled . . . ;
                    denyBtn.setVisibility(View.INVISIBLE);
                    approveBtn.setVisibility(View.INVISIBLE);
                return true;
                }else {
                    editButton.setVisibility(View.GONE);
                    item.setIcon(R.drawable.edit_update);
                    enabledViews(false);
                    putDataInsideViews(this.id);
                    putDataInsideSpinner();
                    editEnabled=false;
                    // hide deny and approve buttonn if edit is enabled . . . ;
                   visiblePermission();

                    // Todo :  return edit image . . . ;
                }
        }

        return super.onOptionsItemSelected(item);
    }

    public void enabledViews(boolean isEnabled){
        firstName.setEnabled(isEnabled);
        lastName.setEnabled(isEnabled);
        studentId.setEnabled(isEnabled);
        fallAmount.setEnabled(isEnabled);
        springAmount.setEnabled(isEnabled);
        schoolSpinner.setEnabled(isEnabled);
        yearSpinner.setEnabled(isEnabled);
        sportSpinner.setEnabled(isEnabled);
        levelSpinner.setEnabled(isEnabled);

    }

    public void buttonAward(View view) {
        if(ViewsFilled()){
        //  check connection for internet . . . ;
               if(CheckConnection.isNetworkOnline(this)){
                   SeasonAvailable();

                   UpdateAwardInDatabase();
               }        else {
                   Toast.makeText(this, "Please Check Your Connection !", Toast.LENGTH_SHORT).show();
               }
        }else {
            Toast.makeText(this, "Please Fill The Required Field !", Toast.LENGTH_SHORT).show();
        }

    }
// for update data base . . . ;
    private void UpdateAwardInDatabase() {
        MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        StringRequest request=new StringRequest(Request.Method.POST, ContractUrl.UPDATES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              if(response.contains("updated Successfully!")){
                  // when updating is successfull  . . . . ;
                  isUpdated=true;
                  Toast.makeText(UpdateUserActivity.this, "updated Successfully!", Toast.LENGTH_SHORT).show();
                  AwardsInformationLab.getInstance().deleteAwards();
                  enabledViews(false);
                  editButton.setVisibility(View.GONE);
                  finish();

              }else {
                  Toast.makeText(UpdateUserActivity.this,"Updated Field !", Toast.LENGTH_LONG).show();
              }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateUserActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                 information=AwardsInformationLab.getInstance().getStudentInformation(id);
                Map<String,String>params=new HashMap<>();
                params.put(Constant_Awards_id.UPDATED_ID,id);
                // get selected text from spinner item . .
                String schoolText=schoolSpinner.getSelectedItem().toString();
                params.put(Constant_Awards_id.SCHOOL, ItemsRecord.searchForId(schoolText,LocalUserVariable.schoolsItems));
                // get selected text from spinner item . .

                String yearText=yearSpinner.getSelectedItem().toString();
                params.put(Constant_Awards_id.YEAR,ItemsRecord.searchForId(yearText,LocalUserVariable.yearsItems));
                params.put(Constant_Awards_id.UPDATED_STUDENT_ID,studentId.getText().toString());
                params.put(Constant_Awards_id.FIRST_NAME,firstName.getText().toString());
                params.put(Constant_Awards_id.LAST_NAME,lastName.getText().toString());
                params.put(Constant_Awards_id.STUDENT_TYPE,information.getStudent_type());
                params.put(Constant_Awards_id.ADMIT_TYPE,information.getAdmit_type());
                params.put(Constant_Awards_id.UPDATED_CLASS_LEVEL,information.getClass_level());

                Log.i("seasonAvailable",fallAmount.getText().toString()+" \n winter "+winterAmount.getText().toString()+" \n spring"+springAmount.getText().toString()+" \n summer"+summerAmount.getText().toString());

                // get selected text from spinner item . .
                 String sportText=sportSpinner.getSelectedItem().toString();
                params.put(Constant_Awards_id.SPORT_ID,ItemsRecord.searchForId(sportText,LocalUserVariable.sportsItems));
                params.put(Constant_Awards_id.SPORT_LEVEL,levelSpinner.getSelectedItem().toString());
                params.put(Constant_Awards_id.FALL,fallAmount.getText().toString());
                params.put(Constant_Awards_id.WINTER,winterAmount.getText().toString());
                params.put(Constant_Awards_id.SPRING,springAmount.getText().toString());
                params.put(Constant_Awards_id.SUMMER,summerAmount.getText().toString());
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    // tell the previous activity if the data is updated or not to refresh recycler view . . . ;
    @Override
    public void finish() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("isUpdated", isUpdated);
        // setResult(RESULT_OK);
        setResult(RESULT_OK, returnIntent);
        super.finish();
    }
  // to visible and hide season edit text . . . . ;
    private boolean ViewsFilled(){
        String std_id,fname,lname,famount,samount;
        std_id=studentId.getText().toString();
        fname=firstName.getText().toString();
        lname=lastName.getText().toString();


        if(std_id.isEmpty()||fname.isEmpty()||lname.isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    private void hide_season(boolean visible){
        if(visible == true){
            fallText.setVisibility(View.VISIBLE);
            springText.setVisibility(View.VISIBLE);
            springAmount.setVisibility(View.VISIBLE);
            fallAmount.setVisibility(View.VISIBLE);
            winterAmount.setVisibility(View.VISIBLE);
            winterText.setVisibility(View.VISIBLE);
            summerText.setVisibility(View.VISIBLE);
            summerAmount.setVisibility(View.VISIBLE);
        }else{
            fallText.setVisibility(View.GONE);
            springText.setVisibility(View.GONE);
            springAmount.setVisibility(View.GONE);
            fallAmount.setVisibility(View.GONE);
            winterAmount.setVisibility(View.GONE);
            winterText.setVisibility(View.GONE);
            summerText.setVisibility(View.GONE);
            summerAmount.setVisibility(View.GONE);
            // set edit text empty . . . . ;
        /*    fallAmount.setText("");
            springAmount.setText("");
            winterAmount.setText("");
            summerAmount.setText("");
            */
        }

    }
    //1 is for fall, 2 is for winter, 3 is for spring , 4 is for summer
    // method to get season depended of term . . . . ;
    private void getSeasons(String term){
        switch (term){
            case "1":
                fallText.setVisibility(View.VISIBLE);
                fallAmount.setVisibility(View.VISIBLE);
                break;
            case "2":
                winterText.setVisibility(View.VISIBLE);
                winterAmount.setVisibility(View.VISIBLE);
                break;
            case "3":
                springText.setVisibility(View.VISIBLE);
                springAmount.setVisibility(View.VISIBLE);
                break;
            case "4":
                summerText.setVisibility(View.VISIBLE);
                summerAmount.setVisibility(View.VISIBLE);
                break;
        }


    }

    public void SeasonAvailable (){
        if(fallAmount.getVisibility() == View.GONE){
            fallAmount.setText("");
        }
        if(winterAmount.getVisibility() == View.GONE){
            winterAmount.setText("");
        }
        if(springAmount.getVisibility() == View.GONE){
            springAmount.setText("");
        }
        if(summerAmount.getVisibility() == View.GONE){
            summerAmount.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId){
            // when click on approve button . .. . . ;
            case R.id.approve_btn :
                UpdateDenyOrApprovedInDatabase("approve");
                break;

                // when click on deny button . . . . ;
            case R.id.deny_btn :
                UpdateDenyOrApprovedInDatabase("deny");
                break;
        }

    }

    private void UpdateDenyOrApprovedInDatabase(String awardActiveStatus){
        MySingleton.getInstance(this.getApplicationContext()).getRequestQueue() ;
        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_AWARD_ACTIVE_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.contains("update successfull")){
                // when update successfull . .. .;
                if(awardActiveStatus.equals("approve")){
                    information.setAward_active_status("2");
                }else {
                    information.setAward_active_status("5");
                }
                visiblePermission();
            }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("update Award Status",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>map = new HashMap<>();
                map.put("id",information.getId());
                map.put("award_active_status",awardActiveStatus);
                return map;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }
}
