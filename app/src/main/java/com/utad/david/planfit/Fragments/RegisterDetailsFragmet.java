package com.utad.david.planfit.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.utad.david.planfit.Activitys.FirstActivity;
import com.utad.david.planfit.Activitys.MainMenuActivity;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class RegisterDetailsFragmet extends Fragment implements FirebaseAdmin.FirebaseAdminLisener {

    private OnFragmentInteractionListener mListener;

    public RegisterDetailsFragmet() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionUser.getInstance().firebaseAdmin.setAdminLisener(this);

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

        fullName.setText("");
        nickName.setText("");
        imageViewUser.setImageResource(R.drawable.icon_gallery);
        fullName.addTextChangedListener(textWatcherRegistreDetailsFragment);
        nickName.addTextChangedListener(textWatcherRegistreDetailsFragment);

        return view;
    }

    private TextWatcher textWatcherRegistreDetailsFragment = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            buttonOk.setEnabled(enableButton());
        }
    };

    private boolean enableButton(){
        if(!checkEditText()){
            return true;
        }else {
            return false;
        }
    }

    private boolean checkEditText(){
        if(nickName.getText().toString().isEmpty() || fullName.getText().toString().isEmpty()){
            return true;
        }else{
            return false;
        }
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

    private boolean endRegister=false;

    @Override
    public void registerWithEmailAndPassword(boolean end) {
        if (end == true) {
            endRegister=true;
            insertUserDataInFirebase(end);
        } else {
            endRegister=false;
        }
    }

    @Override
    public void insertUserDataInFirebase(boolean end) {
        if(endRegister==true){
            if(end==true){
                SessionUser.getInstance().firebaseAdmin.addDataCouldFirestore();
                Toast.makeText(getContext(), "Register Completed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), MainMenuActivity.class);
                startActivity(intent);
                getActivity().finish();
            }else{
                Toast.makeText(getContext(), "Register Fail", Toast.LENGTH_LONG).show();
                errorSingInRegister("Register Fail");
            }
        }else{
            Toast.makeText(getContext(), "Register Fail", Toast.LENGTH_LONG).show();
            errorSingInRegister("Register Fail");
        }
    }

    private void errorSingInRegister(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogTheme);
        builder.setMessage(title)
                .setPositiveButton(R.string.info_dialog_err, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getContext(), FirstActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }

    @Override
    public void singInWithEmailAndPassword(boolean end) {

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
