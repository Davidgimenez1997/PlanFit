package com.utad.david.planfit.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class RegisterDetailsFragmet extends Fragment {


    private OnFragmentInteractionListener mListener;

    public RegisterDetailsFragmet() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText fullName;
    private EditText nickName;
    private ImageView imageViewUser;
    private Button buttonOk;
    private Button buttonBackDetails;
    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_details, container, false);

        findViewById(view);
        onClickButtonOk();
        onClickButtonBackDetails();
        openGallery();

        return view;
    }


    private void findViewById(View view){
        fullName = view.findViewById(R.id.fullName);
        nickName = view.findViewById(R.id.nickName);
        imageViewUser = view.findViewById(R.id.imageViewUser);
        buttonOk = view.findViewById(R.id.buttonOk);
        buttonBackDetails = view.findViewById(R.id.buttonBackDetails);
    }

    private void onClickButtonOk(){
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null){
                    setDataUser();
                    mListener.clickButtonOk();
                }
            }
        });
    }

    private void onClickButtonBackDetails(){
        buttonBackDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.clickButtonBackDetails();
                }
            }
        });
    }

    private void openGallery(){
        imageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                final InputStream imageStream = getActivity().getApplicationContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageViewUser.setImageBitmap(selectedImage);
                if(imageUri!=null){
                    SessionUser.getInstance().user.setImgUser(imageUri.toString());
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getActivity().getApplicationContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void setDataUser(){
        SessionUser.getInstance().user.setFullName(fullName.getText().toString());
        SessionUser.getInstance().user.setNickName(nickName.getText().toString());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void clickButtonOk();
        void clickButtonBackDetails();
    }
}
