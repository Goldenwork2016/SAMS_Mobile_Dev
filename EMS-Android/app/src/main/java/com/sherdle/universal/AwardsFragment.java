package com.sherdle.universal;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sherdle.universal.model.AwardInformation;
import com.sherdle.universal.model.AwardsInformationLab;
import com.sherdle.universal.model.ItemsRecord;
import com.sherdle.universal.model.MySingleton;
import com.sherdle.universal.model.LocalUserVariable;
import com.sherdle.universal.model.SearchingInterface;
import com.sherdle.universal.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.sherdle.universal.model.Constant_Awards_id.ADMIT_TYPE;
import static com.sherdle.universal.model.Constant_Awards_id.AWARD_ACTIVE_USER;
import static com.sherdle.universal.model.Constant_Awards_id.CLASS_TYPE;
import static com.sherdle.universal.model.Constant_Awards_id.FALL;

import static com.sherdle.universal.model.Constant_Awards_id.FETCH_SCHOOL;
import static com.sherdle.universal.model.Constant_Awards_id.FETCH_SPORT;
import static com.sherdle.universal.model.Constant_Awards_id.FETCH_TERM;
import static com.sherdle.universal.model.Constant_Awards_id.FETCH_USER;
import static com.sherdle.universal.model.Constant_Awards_id.FETCH_YEAR;
import static com.sherdle.universal.model.Constant_Awards_id.FETCH_YEAR_FORMATTED;
import static com.sherdle.universal.model.Constant_Awards_id.FIRST_NAME;
import static com.sherdle.universal.model.Constant_Awards_id.ID;
import static com.sherdle.universal.model.Constant_Awards_id.LAST_NAME;
import static com.sherdle.universal.model.Constant_Awards_id.SCHOOL;
import static com.sherdle.universal.model.Constant_Awards_id.SPORT_ID;
import static com.sherdle.universal.model.Constant_Awards_id.SPORT_LEVEL;
import static com.sherdle.universal.model.Constant_Awards_id.SPRING;
import static com.sherdle.universal.model.Constant_Awards_id.STATUS;
import static com.sherdle.universal.model.Constant_Awards_id.STUDENT_ID;
import static com.sherdle.universal.model.Constant_Awards_id.STUDENT_TYPE;
import static com.sherdle.universal.model.Constant_Awards_id.SUMMER;
import static com.sherdle.universal.model.Constant_Awards_id.WINTER;
import static com.sherdle.universal.model.Constant_Awards_id.YEAR;

/**
 * A simple {@link Fragment} subclass.
 */
public class AwardsFragment extends Fragment implements SearchingInterface {
    private static final int RECYCLER_REQUEST =1112 ;
    private RecyclerView recyclerView;
  private RecyclerAwardsAdapter recyclerAdapter ;
  private LinearLayout linearLayout;
  private ProgressBar progressBar;
  List<AwardInformation>informations;


    public static Fragment newInstance(){
      return new AwardsFragment();
  }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fragment_awards, container, false);

         initalizeViews(view);

         // check if data exist in awardInformation lab or not . . . ;
        informations=AwardsInformationLab.getInstance().getAwardsList();
        if(informations.size()>0){
            updateRecyclerView(informations);
        }else {
       GetAwardsFromDatabase();
        }

        return view;
    }

    // initialize and casting (if required) views . . . ;
    private void initalizeViews(View view){
        recyclerView=view.findViewById(R.id.awards_recycler);
        linearLayout=view.findViewById(R.id.linear_view_award);
        progressBar=view.findViewById(R.id.progress_awards);

    }


    // update recycler view . . . ;
    private void updateRecyclerView(List<AwardInformation> informations){
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        recyclerAdapter=new RecyclerAwardsAdapter(getActivity(),informations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);
    }

    // here to handle with search in recycler view . . . . ;
    @Override
    public void UpdateList(String s) {
        Log.i("string is ",s);
        String textInput = s.toLowerCase() ;
        List<AwardInformation>listInformation = new ArrayList<>();

        // check if first name contain text input . . . . ;
        for( AwardInformation award : informations){
            if(award.getName().toLowerCase().contains(textInput) || award.getStudentId().toLowerCase().contains(textInput)){
                listInformation.add(award);
            }
        }
        recyclerAdapter.updateList(listInformation);

    }



    // create inner class for view holder . . . . ;
    private class AwardHolder extends RecyclerView.ViewHolder{
        TextView textId,textName;
        ImageView imageColor;
        public AwardHolder(View itemView) {
            super(itemView);
            textId=itemView.findViewById(R.id.id_label_award);
            textName=itemView.findViewById(R.id.name_label_award);
            imageColor=itemView.findViewById(R.id.image_status);
        }
        // put data inside view inside this method . . ;
        public void onBind(AwardInformation information){
          textId.setText(information.getStudentId());
          textName.setText(information.getName());
          imageColor.setImageResource(getImageFromStatus(information.getStatus()));
          itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent=new Intent(getActivity(),UpdateUserActivity.class);
                  intent.putExtra("std_id",information.getId());
                  startActivityForResult(intent,RECYCLER_REQUEST);
              }
          });
        }
    }

    private int getImageFromStatus(String status) {
      int s=Integer.parseInt(status);
      if(s==1){
          return R.drawable.color_dot_circle_red ;
      }else if (s==2){
          return R.drawable.color_dot_circle_yallow ;
      }else if(s==4){
          return R.drawable.color_dot_circle_green ;
      }else
          return R.drawable.color_dot_circle_black ;
    }


    // create inner class for Adapter (Recycler view Adapter ) . . . . ;
    private class RecyclerAwardsAdapter extends RecyclerView.Adapter<AwardHolder>{
         Context context;
         List<AwardInformation>awardList;

         // create constructor to passing data . . . ;
        public RecyclerAwardsAdapter (Context context,List<AwardInformation>awardList){
            // passing data to local variable . . . ;
            this.context=context;
            this.awardList=awardList;
        }

        @Override
        public AwardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view=LayoutInflater.from(context).inflate(R.layout.list_award_item,viewGroup,false);
            return new AwardHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AwardHolder awardHolder, int i) {
            AwardInformation info=awardList.get(i);
            // set color for view items . . . ;
            if (i % 2 == 0) {
                awardHolder.itemView.setBackgroundColor(getResources().getColor(R.color.recyler_gray));
            } else {
                awardHolder.itemView.setBackgroundColor(Color.WHITE);
            }

            awardHolder.onBind(info);

        }

        @Override
        public int getItemCount() {
            return awardList.size();
        }

        public void updateList(List<AwardInformation>list){
            awardList=new ArrayList<>();
            awardList.addAll(list);
            notifyDataSetChanged();
        }

    }

    public void GetAwardsFromDatabase(){

        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();

      StringRequest request=new StringRequest(Request.Method.POST, ContractUrl.AWARDS_URL, new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
              Log.i("awardResponse",response);
         //     LocalUserVariable.deleteLocaleVariables();
          //    fetchAllSchoolsFromJson(response);
         //     fetchAllYearFromJson(response);
           //   fetchAllSportFromJson(response);
             // fetchAllYearTermFromResponse(response);
              //etchYearFormattedFromJson(response);

           fetchAwardsFromJsonResponse(response);
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
          }
      }){
          @Override
          protected Map<String, String> getParams() throws AuthFailureError {
              Map<String,String>params=new HashMap<>();
              params.put("user_id", LocalUserVariable.userid);
              return params;
          }
      };

        MySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }

    private void fetchAwardsFromJsonResponse(String response) {

        try {

            JSONObject object=new JSONObject(response);
            // todo : get user_type from login json response . . . . ;
               String user_type=object.optString("user_type");

            JSONArray array=object.getJSONArray(FETCH_USER);
            for(int i=0;i<array.length();i++){
                JSONObject student=array.getJSONObject(i);
                String id=student.optString(ID);
                String std_id=student.optString(STUDENT_ID);
                String student_type=student.optString(STUDENT_TYPE);
                String admit_type=student.optString(ADMIT_TYPE);
                String class_level=student.optString(CLASS_TYPE);
                String fname=student.optString(FIRST_NAME);
                String lname=student.optString(LAST_NAME);
                String sportId=student.optString(SPORT_ID);
                String sport=fetchSportFromJsonResponse(response,sportId);
                String level=student.optString(SPORT_LEVEL);
                String status=student.optString(STATUS);
                String schoolId=student.optString(SCHOOL);
                String school=fetchSchoolNameFromJsonResponse(response,schoolId);
                String fall=student.optString(FALL);
                String winter=student.optString(WINTER);
                String spring=student.optString(SPRING);
                String summer=student.optString(SUMMER);
                String year=student.optString(YEAR);
                String award_active_status = student.optString(AWARD_ACTIVE_USER);



                AwardsInformationLab.getInstance().putAward(new AwardInformation(id,std_id,fname,lname,sportId,sport,level,status,schoolId,school,fall,winter,spring,summer,year,student_type,admit_type,class_level,user_type,award_active_status));

            }

            updateRecyclerView(AwardsInformationLab.getInstance().getAwardsList());

        } catch (JSONException e) {
            Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    // fetch sport from json object  . . .   ;
    private String fetchSportFromJsonResponse(String response,String sportId){
      String Sport = null;
        try {
            //Todo : put sport inside Sport variable . ..  . ;
            JSONObject object=new JSONObject(response);
            JSONObject sport_name=object.getJSONObject(FETCH_SPORT);
            Sport=sport_name.optString(sportId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Sport;
    }
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
    /*
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
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECYCLER_REQUEST && resultCode == RESULT_OK) {
         boolean isupdated=  data.getExtras().getBoolean("isUpdated");
         if(isupdated==true){
             linearLayout.setVisibility(View.GONE);
             progressBar.setVisibility(View.VISIBLE);
             GetAwardsFromDatabase();

         }

        }
    }


}
