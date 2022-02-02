package com.sherdle.universal;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageChatDialog extends DialogFragment {
    public static final String ARG_DATE="url_dialog";

   public static ImageChatDialog newInstance(String url){
       Bundle args = new Bundle();
       args.putString(ARG_DATE, url);
       ImageChatDialog chatDialog=new ImageChatDialog();
       chatDialog.setArguments(args);
       return chatDialog;
   }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       String url=(String)getArguments().getString(ARG_DATE);
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_image_chat_dialog,null);

        ImageView imageView=view.findViewById(R.id.image_chatting_dialog);

        // set image inside image view using glide . . .;
        Glide.with(getActivity())
                .load(url)
                .into(imageView);



        return new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setView(view)
                .create();
    }
}
