package com.sherdle.universal;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.sherdle.universal.model.MySingleton;
import com.sherdle.universal.model.LocalUserVariable;
import com.sherdle.universal.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * This file is part of the Universal template
 * For license information, please check the LICENSE
 * file in the root of this project
 *
 * @author Sherdle
 * Copyright 2018
 */
public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView txtError;
    private EditText editText;
    private EditText editText2;

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        TextView txtError = (TextView) findViewById(R.id.txtError);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




    public void login() { TextView txtError = (TextView) findViewById(R.id.txtError);
        EditText editText = (EditText) findViewById(R.id.editText);
        EditText editText2 = (EditText) findViewById(R.id.editText2);


        // check connection . . . .;
        if(!CheckConnection.isNetworkOnline(this)){
            txtError.setText("Please Check Your Connection !");
            return;
        }
        else


        Log.d("Login","Login Pressed");

        txtError.setText("");

      //  RequestQueue queue = Volley.newRequestQueue(this);

        MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        String url ="https://sams.emsystemsolutions.com/api/login/?email=" + editText.getText().toString() + "&password=" + editText2.getText().toString();



        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Display the first 500 characters of the response string.
                        if(response.contains("Email & Password is Incorrect!"))
                        {
                            txtError.setText(response);


                        } else if(response.contains("not support mobile")){
                            txtError.setText("Your school doesn't support mobile app ");
                        }
                        else {
                            txtError.setText("login successfully");
                            try {
                                JSONObject object=new JSONObject(response);
                                LocalUserVariable.userid=object.optString("result");
                                LocalUserVariable.usertype=object.optString("user_type");
                                LocalUserVariable.permission_add = object.optString("permission_add");
                                LocalUserVariable.permission_approve = object.optString("permission_approve");
                                LocalUserVariable.permission_deny = object.optString("permission_deny");
                                LocalUserVariable.permission_edit = object.optString("permission_edit");
                                Log.i("Permission ","permission add is "+LocalUserVariable.permission_add);
                                Log.i("Permission","permission approve is "+LocalUserVariable.permission_approve);
                                Log.i("Permission","permission deny is "+LocalUserVariable.permission_deny);
                                Log.i("Permission","permission edit is "+LocalUserVariable.permission_edit);


                                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                                startActivity(intent);
                                LoginActivity.this.finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtError.setText(error.getMessage());
                Toast.makeText(LoginActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){

        };


// Add the request to the RequestQueue.
      //  queue.add(stringRequest);

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    }


