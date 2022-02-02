package com.sherdle.universal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.sherdle.universal.model.Constant_Awards_id.FETCH_SCHOOL;
import static com.sherdle.universal.model.Constant_Awards_id.FETCH_SPORT;
import static com.sherdle.universal.model.Constant_Awards_id.FETCH_TERM;
import static com.sherdle.universal.model.Constant_Awards_id.FETCH_YEAR;
import static com.sherdle.universal.model.Constant_Awards_id.FETCH_YEAR_FORMATTED;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {
    Button yearButton , sportButton ;
    TextView goalText,rosterCurrent,pendingText , budgetText , awardedText , limitText , schoolCurrent , jvText , varsityText ;
    LinearLayout dashboardViewGroup ;
            String selected_year ="";
            String sports_ids = "" ;
            String selected_sport = "" ;
    // create instance directly from this method . . .;
 public static Fragment getInstance(){
     return new DashboardFragment();
 }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      HomeActivity.isDashboard = true ;
        DialogDashboardListener listener = (DialogDashboardListener)getActivity();
        listener.hideItem(false);


     getDataFromDatabase();
     View view = inflater.inflate(R.layout.fragment_dashboard, container, false) ;
     initalizeViews(view);

     // check if user type equal admin then will not showing dashboard view in the screen . . . . ;
        if (LocalUserVariable.usertype.equals("admin")) {
            dashboardViewGroup.setVisibility(View.GONE);
        }




        yearButton.setOnClickListener(this);
      sportButton.setOnClickListener(this);

        return view;
    }

    // initalize views or casting ( if required );
    private void initalizeViews(View view){
     yearButton = view.findViewById(R.id.year_btn_dash);
     sportButton = view.findViewById(R.id.sport_btn_dash);
     goalText = view.findViewById(R.id.goal_text);
     rosterCurrent = view.findViewById(R.id.roster_current_text);
     pendingText = view.findViewById(R.id.pending_text);
     budgetText = view.findViewById(R.id.budget_text);
     awardedText = view.findViewById(R.id.awarded_text);
     limitText = view.findViewById(R.id.limit_text);
     schoolCurrent = view.findViewById(R.id.school_text);
     dashboardViewGroup = view.findViewById(R.id.dashboard_view_group);
     jvText = view.findViewById(R.id.jv_text);
     varsityText = view.findViewById(R.id.varsity_text);
    }


    // get data from database . . . ;
    private void getDataFromDatabase(){
        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue() ;

        StringRequest request = new StringRequest(Request.Method.POST, ContractUrl.DASHBOARD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             Log.i("Dashboard Resaponse is ", response);
                LocalUserVariable.deleteLocaleVariables();

                fetchDataFromJsonResponse(response);
                fetchYearDashboardFromJsonResponse(response);
                fetchSportDashobardFromJsonResponse(response);
                fetchselectedSportFromJsonResponse(response);
                fetchAllSchoolsFromJson(response);
                fetchAllYearFromJson(response);
                fetchAllSportFromJson(response);
                fetchAllYearTermFromResponse(response);
                fetchYearFormattedFromJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params = new HashMap<>();
                params.put("user_id", LocalUserVariable.userid);
                return  params ;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
    // fetch data from json response . . . . ;
    private void fetchDataFromJsonResponse(String response)  {
        try {
            JSONObject root = new JSONObject(response);
            JSONObject dashboard = root.optJSONObject("dashboard");
            String goal = dashboard.getString("goal");
            String roaster_current = dashboard.getString("roster_current");
            String pending = dashboard.getString("pending");
            String budget = dashboard.getString("budget");
            String awarded = dashboard.getString("awarded");
            String limit = dashboard.getString("limit");
            String school_current = dashboard.getString("schoolarship_current");
            String jv = dashboard.getString("jv");
            String varsity = dashboard.getString("varsity");
            // put these data inside views . . . . . ;
            putDataInsideLabel(goal,roaster_current,pending,budget,awarded,limit,school_current,jv,varsity);
        } catch (JSONException e) {
            Log.e("Dashboard Json Error :",e.getMessage());
        }

    }
    private void fetchYearDashboardFromJsonResponse(String response){
        try {
            JSONObject object = new JSONObject(response);
            JSONArray array = object.optJSONArray("selected_year");
            for(int i=0 ;i<array.length();i++){
                selected_year +=array.getString(i)+",";
            }



        } catch (JSONException e) {
            Log.e("error is : ",e.getMessage());
                    }

    }

    private void  fetchSportDashobardFromJsonResponse(String response) {
        try {
            JSONObject object = new JSONObject(response);
            JSONObject sport = object.optJSONObject("sport");
            sports_ids =  sport.optString("sport_id");


        } catch (JSONException e) {
            Log.e("json sport formatter error : ",e.getMessage());
        }

    }

    private void fetchselectedSportFromJsonResponse(String response){

        try {
           JSONObject object = new JSONObject(response);
            selected_sport = object.optString("selected_sport");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // put data inside label . . . . ;
    private void putDataInsideLabel(String goal, String roaster_current, String pending, String budget, String awarded, String limit, String school_current,String jv , String varsity ) {
   goalText.setText(goal);
   rosterCurrent.setText(roaster_current);
   pendingText.setText(pending);
   budgetText.setText(budget);
   awardedText.setText(awarded);
   limitText.setText(limit);
   schoolCurrent.setText(school_current);
   // put jv varsity inside his labels . . .
   jvText.setText(jv);
   varsityText.setText(varsity);

    }

    @Override

    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager() ;
     switch (v.getId()){
         case R.id.year_btn_dash :
             YearDashboardDialog yeardialog = YearDashboardDialog.getInstance(selected_year);
             yeardialog.show(fragmentManager , "yeardialog");

             break;
         case R.id.sport_btn_dash :
             SportDashboardDialog sportDialog = SportDashboardDialog.getInstance(sports_ids,selected_sport);
             sportDialog.show(fragmentManager, "sportdialog");
          break;

     }
       }


       // fetchecd data from json response . . . ;
       // fetch sport from json object  . . . ;
    // fetch school from json object . . . ;
    private String fetchSchoolNameFromJsonResponse(String response,String schoolId){
        String School=null;
        try {
            JSONObject object=new JSONObject(response);
            JSONObject school_name=object.getJSONObject(FETCH_SCHOOL);
            School=school_name.optString(schoolId);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return School;
    }

    private void fetchAllSchoolsFromJson(String response){
        try {
            JSONObject object=new JSONObject(response);
            JSONObject schools=object.getJSONObject(FETCH_SCHOOL);
            Log.i("schools is ", schools.toString());
            Iterator<String> iter = schools.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    // Todo : passing key for schools . . . ;
                    String a=schools.getString(key);
                    LocalUserVariable.school.add(a);
                    LocalUserVariable.schoolsItems.add(new ItemsRecord(a,key));

                    // Toast.makeText(getActivity(), a,LocalUserVariable.school, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    // Something went wrong!
                    Log.e("Schools response error","something went wrong");
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void fetchAllYearFromJson(String response){
        try {
            JSONObject object=new JSONObject(response);
            JSONObject years=object.getJSONObject(FETCH_YEAR);
            Log.i("years is ",years.toString());
            Iterator<String> iter = years.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    // Todo : passing key for years . . . ;
                    String a=years.getString(key);
                    if (!isYearDuplicated(a)){
                        LocalUserVariable.year.add(a);
                    }
                    LocalUserVariable.yearsItems.add(new ItemsRecord(a, key));


                } catch (JSONException e) {
                    // Something went wrong!
                    Log.e("years response error","something went wrong");
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    // check if year is duplicated
    private boolean isYearDuplicated(String name){
        for (int i = 0 ;i<LocalUserVariable.yearsItems.size();i++){
            if (LocalUserVariable.yearsItems.get(i).getName().equals(name)){
                Log.i("Years","years id = "+LocalUserVariable.yearsItems.get(i).getName()+"  parameter is "+name+"year ss is ");
                return true ;
            }
        }
        return false;
    }

    private void fetchYearFormattedFromJson(String response){
        try {
            JSONObject object=new JSONObject(response);
            JSONObject years=object.getJSONObject(FETCH_YEAR_FORMATTED);
            Iterator<String> iter = years.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    // Todo : passing key for years . . . ;
                    String a=years.getString(key);
                    Log.i("yearFormat is ",key);

                    LocalUserVariable.YearFormattedItems.add(new ItemsRecord(a,key));


                } catch (JSONException e) {
                    // Something went wrong!
                    Log.e("years response error","something went wrong");
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void fetchAllYearTermFromResponse(String response){
        try {
            JSONObject object=new JSONObject(response);
            JSONObject term=object.getJSONObject(FETCH_TERM);
            Log.i("term is ",term.toString());
            Iterator<String> iter = term.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    // Todo : passing key for years . . . ;
                    String a=term.getString(key);
                    LocalUserVariable.terms.add(a);
                    LocalUserVariable.termsItems.add(new ItemsRecord(a,key));


                } catch (JSONException e) {
                    // Something went wrong!
                    Log.e("term response error","something went wrong");
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




    private void fetchAllSportFromJson(String response){
        try {
            JSONObject object=new JSONObject(response);
            JSONObject sport=object.getJSONObject(FETCH_SPORT);
            Iterator<String> iter = sport.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    // Todo : passing key for sports . . . ;
                    String a=sport.getString(key);
                    LocalUserVariable.sport.add(a);
                    LocalUserVariable.sportsItems.add(new ItemsRecord(a,key));
                    Log.i("Sports Items : ","key is : "+ a + "\n"+ " value is : "+key);
                } catch (JSONException e) {
                    // Something went wrong!
                    Log.e("years response error","something went wrong");
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
