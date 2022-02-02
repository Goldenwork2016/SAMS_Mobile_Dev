package com.sherdle.universal;


import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sherdle.universal.model.LocalUserVariable;
import com.sherdle.universal.model.MySingleton;
import com.sherdle.universal.model.SupportInformation;
import com.sherdle.universal.model.SupportInformationLab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SupportFragment extends Fragment {
    private RecyclerView recyclerSupport;
    LinearLayout linearSupport;
    ProgressBar supportProgress;
    RecyclerSupportAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_support, container, false);
        if(LocalUserVariable.usertype=="admin"){
            setHasOptionsMenu(false);
        }
        initializeViews(view);
        // check if support array in (singleton class) is empty . . . ;
        boolean supportIsEmpty=SupportInformationLab.getInstance().getSupportlist().size()==0;
        if(supportIsEmpty) {
            getSupportFromDatabase();
        }else {
            updateRecyclerView(SupportInformationLab.getInstance().getSupportlist());
        }
        return view;
    }


    // for initialize views and cast ( if required) . . . ;
    private void initializeViews(View view) {
        recyclerSupport = view.findViewById(R.id.support_recycler);
        linearSupport = view.findViewById(R.id.linear_view_support);
        supportProgress = view.findViewById(R.id.progress_support);

    }

    private void updateRecyclerView(List<SupportInformation> informations) {
        adapter = new RecyclerSupportAdapter(informations);
        recyclerSupport.setHasFixedSize(true);
        recyclerSupport.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerSupport.setAdapter(adapter);
        hideViews(true);

    }

    public static Fragment newInstance() {
        return new SupportFragment();
    }

    // create holder for support . . . ;
    private class SupportHolder extends RecyclerView.ViewHolder {
        TextView ticketSupport, subjectSupport;
        ImageView imageSupport;

        public SupportHolder(@NonNull View itemView) {
            super(itemView);
            ticketSupport = itemView.findViewById(R.id.ticket_label_support);
            subjectSupport = itemView.findViewById(R.id.subject_label_support);
            imageSupport = itemView.findViewById(R.id.image_support_status);
        }

        // bind data inside views . . . in recycler view (using method ) . . . ;
        public void bindSupport(SupportInformation information) {
            ticketSupport.setText(information.getTicket());
            subjectSupport.setText(information.getSubject());
            imageSupport.setImageResource(setColorFromStatus(information.getStatus()));
        }

    }

    private int setColorFromStatus(String status) {
        int num = Integer.parseInt(status);
        if (num == 3) {
            return R.drawable.color_dot_circle_yallow;
        } else if (num == 4) {
            return R.drawable.color_dot_circle_blue;
        }
        return R.drawable.color_dot_circle_green;
    }

    // create adapter for recycler view using SupportHolder . . . ;
    private class RecyclerSupportAdapter extends RecyclerView.Adapter<SupportHolder> {
        List<SupportInformation> listInformations;

        public RecyclerSupportAdapter(List<SupportInformation> listInformations) {
            this.listInformations = listInformations;
        }

        @NonNull
        @Override
        public SupportHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_support_item, viewGroup, false);
            return new SupportHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SupportHolder supportHolder, int i) {
            if(i%2==0){
                supportHolder.itemView.setBackgroundColor(getResources().getColor(R.color.recyler_gray));
            }else {
                supportHolder.itemView.setBackgroundColor(getResources().getColor(R.color.white));
            }
            SupportInformation supportInformation = listInformations.get(i);
            supportHolder.bindSupport(supportInformation);

            supportHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),ChattingActivity.class);
                    intent.putExtra("cst",supportInformation.getCst_id());
                    intent.putExtra("userId",LocalUserVariable.userid);
                    intent.putExtra("ticket_num",listInformations.get(i).getTicket());
                    startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return listInformations.size();
        }
    }

    private void getSupportFromDatabase() {
        MySingleton.getInstance(getActivity()).getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.GET, ContractUrl.SUPPORT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fetchSupportFromJsonResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);
    }

    private void fetchSupportFromJsonResponse(String response) {
        try {
            JSONObject object = new JSONObject(response);
            JSONArray supportArray = object.optJSONArray("support_ticket");
            for (int i = 0; i < supportArray.length(); i++) {
                JSONObject arrObject = supportArray.optJSONObject(i);
                String user_id = arrObject.optString("user_id");
                String cst_id=arrObject.optString("cst_id");
                String ticket = arrObject.optString("ticket_id");
                String subject = arrObject.optString("ticket_subject");
                String status = arrObject.optString("ticket_status");
                // fetch user type and pass data . . . ;
                LocalUserVariable.cst_id=cst_id;
                fetchUserTypeFromJsonResponse(response,user_id,ticket,cst_id,subject,status);

            }
            updateRecyclerView(SupportInformationLab.getInstance().getSupportlist());


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // fetch user type from json response . . . ..  ;
    private void fetchUserTypeFromJsonResponse(String response,String userid,String ticket,String cst_id,String subject,String status){
        try {
            JSONObject object=new JSONObject(response);
            JSONObject userAccount=object.optJSONObject("user_accounts_list");
            JSONObject user=userAccount.optJSONObject(userid);
            String name=user.optString("name");
            String type=user.optString("permission_type");
            SupportInformationLab.getInstance().addSupport(new SupportInformation(ticket,cst_id, subject, status, userid,type,name));


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void hideViews(boolean show) {
        if (show == true) {
            linearSupport.setVisibility(View.VISIBLE);
            supportProgress.setVisibility(View.GONE);
            return;
        }
        linearSupport.setVisibility(View.GONE);
        supportProgress.setVisibility(View.VISIBLE);
    }
}
