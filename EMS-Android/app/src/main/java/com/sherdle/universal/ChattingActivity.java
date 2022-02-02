package com.sherdle.universal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sherdle.universal.model.ChattingInformation;
import com.sherdle.universal.model.LocalUserVariable;
import com.sherdle.universal.model.MySingleton;
import com.sherdle.universal.providers.woocommerce.model.products.Image;
import com.sherdle.universal.util.Log;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChattingActivity extends AppCompatActivity implements MyDialogFragmentListener{
    private static final int EXTERNAL = 112;
    private static final int RESULT_LOAD_IMAGE = 22;
    TextView nameImage;
    ImageView imagetest;
    EditText editMessage;
    private RecyclerView recyclerChat;
    Bitmap bitmap;
    Uri imageUri;
    static String imageString;
    String cstData, userId, ticket_num;
    ProgressBar progressChat;
    Toolbar chattingtoolbar ;

    private List<ChattingInformation> informationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        informationList = new ArrayList<>();
        imagetest = new ImageView(this);
        cstData = getIntent().getStringExtra("cst");
        userId = getIntent().getStringExtra("userId");
        ticket_num = getIntent().getStringExtra("ticket_num");
        setContentView(R.layout.activity_chatting);
        initializeComponent();
        setActionBarComponent();
        getChattingFromDataBase();


    }

    private void getChattingFromDataBase() {
        MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ContractUrl.CHAT_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("chattingInfo", response);
                fetchChattingFromJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Todo put arch_ticket and number ticket for getting chatting data . . . . ;
                Map<String, String> map = new HashMap<>();
                map.put("user_id", LocalUserVariable.userid);
                map.put("cst_id", cstData);
                map.put("ticket_number", ticket_num);
                return map;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(request);


    }


    private void initializeComponent() {
        chattingtoolbar = findViewById(R.id.chatting_toolbar);
        recyclerChat = findViewById(R.id.recycler_chatting);
        progressChat = findViewById(R.id.progressChat);
        editMessage = findViewById(R.id.editMessageChat);
        imagetest = findViewById(R.id.file_name_attach);

    }

    private void setActionBarComponent(){
        setSupportActionBar(chattingtoolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        chattingtoolbar.setNavigationIcon(R.drawable.ic_back);
    }

    private void updateRecyclerView(List<ChattingInformation> list) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerChat.setLayoutManager(manager);
        recyclerChat.setHasFixedSize(true);
        progressChat.setVisibility(View.GONE);
        recyclerChat.setVisibility(View.VISIBLE);
        recyclerChat.setAdapter(new ChattingRecyclerAdapter(list));
        recyclerChat.scrollToPosition(list.size() - 1);
    }

    public void replayBtn(View view) {
        String msg = editMessage.getText().toString();
        if ((msg.isEmpty() || msg.equals(null)) && imageString == "") {
            Toast.makeText(ChattingActivity.this, "nothing to show !", Toast.LENGTH_SHORT).show();
            return;
        }
        // if message text not equal null or image uri not null . . .then implement these statement . . . . . ;
        passDataToPhpApi(cstData, msg, userId, LocalUserVariable.usertype, ticket_num);
        editMessage.setText("");
        imageString = "";
        imagetest.setVisibility(View.GONE);


    }

    private void passDataToPhpApi(String cstData, String msg, String userId, String usertype, String ticket_num) {
        MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ContractUrl.ADD_CHAT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("responseChat",response);
                Toast.makeText(ChattingActivity.this, "Support Chat", Toast.LENGTH_SHORT).show();

                MyDialogFragmentListener listener=(MyDialogFragmentListener)ChattingActivity.this;
                listener.setIsSaved(true);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChattingActivity.this, "Something was error !", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cst_id", cstData);
                params.put("message", msg);
                params.put("user_id", userId);
                params.put("user_type", usertype);
                params.put("ticket_number", ticket_num);
                params.put("file_content", imageString);
                params.put("reply", "reply");
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId() ;
        switch (id){
            case android.R.id.home :
                ChattingActivity.this.finish();
                return true ;
        }
        return super.onOptionsItemSelected(item);
    }

    private void askForPermission(String permission, int request) {
        if (ContextCompat.checkSelfPermission(ChattingActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ChattingActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(ChattingActivity.this, new String[]{permission}, request);

            } else {

                ActivityCompat.requestPermissions(ChattingActivity.this, new String[]{permission}, request);
            }
        } else {

            getImageFromAlbum();
        }
    }

    public void attachBtn(View view) {
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, EXTERNAL);
    }

    @Override
    public void setIsSaved(boolean isSaved) {
        if(isSaved==true){
        recreate();
        }
    }


    // create View holder for chatting recycler view (Chatting Holder) . . . . ;
    private class ChattingHolder extends RecyclerView.ViewHolder {
        ImageView imagePerson, imageAttach;
        TextView personName, chatDate, chatText, chatType;
        ConstraintLayout layout ;

        public ChattingHolder(View itemView) {
            super(itemView);
            imagePerson = itemView.findViewById(R.id.person_image);
            imageAttach = itemView.findViewById(R.id.image_attach);
            personName = itemView.findViewById(R.id.person_name);
            chatDate = itemView.findViewById(R.id.date_chat_text);
            chatText = itemView.findViewById(R.id.chat_text);
            chatType = itemView.findViewById(R.id.chat_type_label);
            layout = itemView.findViewById(R.id.constraintLayout);

        }



    }


    // create adapter for recycler views . . . . ;
    private class ChattingRecyclerAdapter extends RecyclerView.Adapter<ChattingHolder> {

        List<ChattingInformation> chattingList;

        public ChattingRecyclerAdapter(List<ChattingInformation> chattingList) {
            this.chattingList = chattingList;

        }

        @Override
        public ChattingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;
            switch (i) {

                case 0:
                    view = LayoutInflater.from(ChattingActivity.this).inflate(R.layout.sender_chat_list, viewGroup, false);
                    break;
                case 1:
                    view = LayoutInflater.from(ChattingActivity.this).inflate(R.layout.receiver_chat_list, viewGroup, false);
                    break;
            }
            return new ChattingHolder(view);
        }

        // get chat type (if exists .. for admin only . . . . ;
           private String getChatType(String removed , String edited_admin,String edited){
               if(removed !="null"){
                   return "(deleted)";
               }
               if(edited_admin!="null"){
                   return "(edited above)";
               }
               if(edited !="null"){
                   return "(edited)";
               }
               return "nothing";
           }
        @Override
        public void onBindViewHolder(@NonNull ChattingHolder chattingHolder, int i) {
            ChattingInformation information = chattingList.get(i);
            chattingHolder.personName.setText(information.getUser_name());
            chattingHolder.chatDate.setText(information.getChattime());
            chattingHolder.chatText.setText(information.getMessage());




            String chatType=getChatType(information.getRemoved(),information.getEdited_admin(),information.getEdited());
            // check chat type if nothing hidden the text field ... else . show the text field with chat type  . . . . . ;
            if(chatType.equals("nothing")){
                chattingHolder.chatType.setVisibility(View.INVISIBLE);
            }else {
                chattingHolder.chatType.setVisibility(View.VISIBLE);
                chattingHolder.chatType.setText(chatType);
            }





            // to show image in dialog . . . . ;
            chattingHolder.imageAttach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chattingHolder.imageAttach.getVisibility() == View.VISIBLE) {
                        FragmentManager fragmentmanager = getSupportFragmentManager();
                        ImageChatDialog chatDialog = ImageChatDialog.newInstance(information.getImage());
                        chatDialog.show(fragmentmanager, "imagedialog");


                    }
                }
            });

     // when user press long click on items in recycler view ( chatting ) . . .. ;
                chattingHolder.layout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        if(information.getUser_id().equals(LocalUserVariable.userid)&&!information.getMessage().equals("user_upload_images")) {
                            // check if user id equal chat user id ... ;
                            FragmentManager manager = getSupportFragmentManager();
                            EditDialogFragment dialogFragment = EditDialogFragment.getInstance(information.getChat_id(), information.getImage(), information.getMessage(), information.getUser_id());
                            dialogFragment.show(manager, "dialog chatting");
                        }
                        return true;


                    }
                });




            // set image for reciever person . . . ;
            if (information.getUser_profile_image_sender() != null) {
                Glide.with(ChattingActivity.this)
                        .load("https://admin.emsystemsolutions.com/assest/upload_profile/" + information.getUser_profile_image_sender())
                        .into(chattingHolder.imagePerson);
            } else {
                Glide.with(ChattingActivity.this)
                        .load("https://admin.emsystemsolutions.com/assest/upload_profile/avatar1.png")
                        .into(chattingHolder.imagePerson);
            }

            // chcek if attachement is empty or not . . . . ;
            if (information.getAttachment().equals("null") || information.getAttachment().isEmpty()) {

                if (information.getMessage().contains("user_upload_images")) {
                    chattingHolder.chatText.setText("");
                    chattingHolder.imageAttach.setVisibility(View.VISIBLE);
                    Glide.with(ChattingActivity.this)
                            .load(information.getImage())
                            .into(chattingHolder.imageAttach);


                } else {
                    chattingHolder.imageAttach.setVisibility(View.GONE);
                }


            } else {
                chattingHolder.chatText.setText(information.getMessage());
                chattingHolder.imageAttach.setVisibility(View.VISIBLE);
                Glide.with(ChattingActivity.this)
                        .asBitmap()
                        .load(information.getImage())
                        .into(chattingHolder.imageAttach);
            }

        }

        @Override
        public int getItemCount() {
            return chattingList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (informationList.get(position).getUser_id().equals(userId)) {
                return 0;
            }
            return 1;
        }


    }


    // to fetch data from json response .  . . .  ;
    private void fetchChattingFromJson(String response) {
        try {

            JSONObject root = new JSONObject(response);
            JSONArray array = root.getJSONArray("ticket_chatting_result");
            for (int i = 0; i < array.length(); i++) {
                JSONObject message = array.optJSONObject(i);
                String chatId = message.optString("id");
                String uId = message.optString("user_id");
                String cst_id = message.optString("cst_id");
                String msg = message.optString("message").trim();
                String attachment = message.optString("attachment");
                String chattime = message.optString("chattime");
                String image = message.optString("image").trim();
                String username = fetchUserNameFromJson(response, uId);
                String img_profile_url_sender = fetchUserImageFromJson(response, uId);
                String removed = message.optString("removed");
                String edited = message.optString("edited");
                String edit_admin = message.optString("edited_admin");


                if (msg.equals("user_upload_images") && image.equals("no")) {
                    continue;
                    // inside this if statement add check if user is admin or not if admin put data if not hide dont store data . . .

                } else if (removed != "null" || edit_admin != "null") {

                    // check if user type is admin ( store data ) if not ( hide data ) continue . . . . ;
                    if(!LocalUserVariable.usertype.equals("admin")){
                        continue;
                    }else {
                        informationList.add(new ChattingInformation(chatId, uId, cst_id, msg, chattime, username, img_profile_url_sender, attachment, image, removed, edit_admin,edited));
                    }

                } else {
                    informationList.add(new ChattingInformation(chatId, uId, cst_id, msg, chattime, username, img_profile_url_sender, attachment, image, removed, edit_admin,edited));
                }


              /*  if(!msg.equals("user_upload_images")&&image!="no"){
                    informationList.add(new ChattingInformation(chatId, uId, cst_id, msg, chattime, username, img_profile_url_sender, attachment,image));
                }else if(!msg.equals("user_upload_image")&&image=="no"){
                    informationList.add(new ChattingInformation(chatId, uId, cst_id, msg, chattime, username, img_profile_url_sender, attachment,image));
                    // dont do anythiing . . . . ;
                }else if(msg.equals("user_upload_image")&&image!="no"){
                    informationList.add(new ChattingInformation(chatId, uId, cst_id, msg, chattime, username, img_profile_url_sender, attachment,image));
                }*/

            }
            Log.i("chatresponse", "message = " + informationList.get(informationList.size() - 1).getMessage() + " \n " + informationList.get(informationList.size() - 1).getMessage() + "\n" + "image =" + informationList.get(informationList.size() - 1).getImage() + "\n" + "image =" + informationList.get(informationList.size() - 1).getImage());
            updateRecyclerView(informationList);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    // get name from json response . . . . ;
    public String fetchUserNameFromJson(String response, String userId) throws JSONException {
        JSONObject root = new JSONObject(response);
        JSONObject object = root.optJSONObject("user_name_list");
        String name = object.optString(userId);
        return name;
    }

    public String fetchUserImageFromJson(String response, String userId) throws JSONException {
        JSONObject root = new JSONObject(response);
        JSONObject o = root.optJSONObject("profile_images_list");
        // Todo : check if user id is the goal of any other info . . . . ;
        String profile_image_url = o.optString(userId);
        if (profile_image_url == null || profile_image_url.isEmpty()) {
            return null;
        }
        String image_url = profile_image_url;

        return image_url;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case EXTERNAL:
                    // Todo : // you can go the image here and get path . .. . . ; 
                    Toast.makeText(this, "Permission Granted !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                imageUri = data.getData();
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imagetest.setImageBitmap(bitmap);
                imagetest.setVisibility(View.VISIBLE);

                // convert image to byte code then to base 64 . . .  ;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 60, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                // Converted to base 46
                imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ChattingActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(ChattingActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }


    }


    private void getImageFromAlbum() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        } catch (Exception exp) {
            Log.i("Error", exp.toString());
        }
    }




}
