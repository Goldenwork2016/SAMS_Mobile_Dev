package com.sherdle.universal;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sherdle.universal.model.LocalUserVariable;
import com.sherdle.universal.model.MySingleton;

import java.util.HashMap;
import java.util.Map;

public class SupportDialogFragment extends DialogFragment implements View.OnClickListener {
EditText editSubject,editMessage;
Button addButton,cancelBtn;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.add_support_dialog,null);
        initilizeViews(view);

        addButton.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);


        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    // initialize views and casting (if required) . . . . ;
    public void initilizeViews(View view){
        editSubject=view.findViewById(R.id.subject_support_text);
        editMessage=view.findViewById(R.id.desc_support_text);
        addButton=view.findViewById(R.id.support_add_button);
        cancelBtn=view.findViewById(R.id.support_cancel_button);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.support_add_button:
                // todo : add Subject with message to the server . . . ;
                  passDataToDatabase();
                break;
            case R.id.support_cancel_button:
                dismiss();
                break;
        }
    }

    private void passDataToDatabase() {
        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        StringRequest request=new StringRequest(Request.Method.POST, ContractUrl.ADD_SUPPORT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                  if(response.contains("add successfull")){
                      // Todo : refresh previous fragment from this class  . . . ;
                      MyDialogFragmentListener dialogListener=(MyDialogFragmentListener)getActivity();
                      dialogListener.setIsSaved(true);
                      dismiss();

                  }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("user_id", LocalUserVariable.userid);
                params.put("ticket_subject",editSubject.getText().toString());
                params.put("ticket_message",editMessage.getText().toString());
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }


}
