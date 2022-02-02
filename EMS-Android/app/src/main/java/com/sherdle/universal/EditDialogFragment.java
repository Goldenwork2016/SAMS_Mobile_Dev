package com.sherdle.universal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.sherdle.universal.model.ChattingInformation;
import com.sherdle.universal.model.MySingleton;
import com.sherdle.universal.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EditDialogFragment extends DialogFragment {
    private EditText editTextmessage ;
    private ImageView image_attach;
    private Button editBtn , deleteBtn,cancelBtn;
    private String chat_id,img_url,msg,user_id;
    private boolean isEdit=true;
    public static EditDialogFragment getInstance(String chatId,String img_url,String msg , String user_id){
        Bundle args = new Bundle();
        args.putString("chatId",chatId);
        args.putString("img_url",img_url);
        args.putString("msg",msg);
        args.putString("user_id",user_id);
        EditDialogFragment editDialog=new EditDialogFragment();
        editDialog.setArguments(args);
        return editDialog;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_dialog_fragment,null);
        initializeViews(view);
         chat_id=(String)getArguments().getString("chatId");
         img_url=(String)getArguments().getString("img_url");
         msg=(String)getArguments().getString("msg");
         user_id=(String)getArguments().getString("user_id");

        editTextmessage.setText(msg);

        if(img_url=="null"){
            image_attach.setVisibility(View.INVISIBLE);
        }else {
            image_attach.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(img_url)
                    .into(image_attach);
        }

        // for handle when click on cancel button . . .. ;
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit==false){
                    editTextmessage.setText(msg);
                    editTextmessage.setEnabled(false);
                    editBtn.setText("Edit");
                }else {
                    dismiss();
                }
            }
        });

        // for handle when click on delete button . . . .;
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteChatFromDatabase();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(isEdit==true){
                  editBtn.setText("Save");
                  editTextmessage.setEnabled(true);
                  isEdit=false;
              }else {
                  editTextmessage.setEnabled(false);
                  editChatFromDatabase();
                  isEdit=true;

              }
            }
        });



        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setCancelable(true)
                .create();
    }
     // to edit chat from database . . . . ;
    private void editChatFromDatabase() {

        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        StringRequest editRequest= new StringRequest(Request.Method.POST, ContractUrl.EDIT_CHAT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                MyDialogFragmentListener listener=(MyDialogFragmentListener)getActivity();
                listener.setIsSaved(true);
                    dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            Log.e("editError",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("chat_id",chat_id);
                params.put("message",editTextmessage.getText().toString());
                params.put("user_id",user_id);
                return params;
            }
        };
       MySingleton.getInstance(getActivity()).addToRequestQueue(editRequest);
    }

    // to delete chat from database . . . . ;
    private void deleteChatFromDatabase() {
        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        StringRequest request=new StringRequest(Request.Method.GET, ContractUrl.REMOVE_CHAT+chat_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             if(response.contains("deleted")){
                 Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                 MyDialogFragmentListener listener=(MyDialogFragmentListener)getActivity();
                 listener.setIsSaved(true);
                 dismiss();
             }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("delete chat error",error.getMessage());
            }
        });

        MySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }

    private void initializeViews(View view){
        editTextmessage =view.findViewById(R.id.edit_msg);
        image_attach=view.findViewById(R.id.image_d);
        editBtn=view.findViewById(R.id.edit_btn_dialog);
        deleteBtn=view.findViewById(R.id.delet_msg_btn);
        cancelBtn=view.findViewById(R.id.cancel_edit_dialog);

    }
}
