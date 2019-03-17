package com.utad.david.planfit.Fragments.FragmentsFirstActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.utad.david.planfit.Activitys.MainMenuActivity;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class RegisterDetailsFragmet extends Fragment implements FirebaseAdmin.FirebaseAdminLoginAndRegisterListener,FirebaseAdmin.FirebaseAdminInsertAndDownloandListener {

    private OnFragmentInteractionListener mListener;

    public RegisterDetailsFragmet() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminLoginAndRegisterListener(this);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminInsertAndDownloandListener(this);

    }

    private EditText fullName;
    private EditText nickName;
    private ImageView imageViewUser;
    private Button buttonOk;
    private Button buttonBackDetails;
    private Uri imageUri;
    private ProgressDialog mProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_details, container, false);

        findViewById(view);
        onClickButtonOk();
        onClickButtonBackDetails();
        openGallery();
        configView();
        showDialog();

        return view;
    }

    private void configView(){
        fullName.setText("");
        nickName.setText("");
        imageViewUser.setImageResource(R.drawable.icon_gallery);
        fullName.addTextChangedListener(textWatcherRegistreDetailsFragment);
        nickName.addTextChangedListener(textWatcherRegistreDetailsFragment);
    }

    private void showDialog(){
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle(getString(R.string.title_register));
        mProgress.setMessage(getString(R.string.message_register));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
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
                    mProgress.show();
                    mListener.clickButtonOk();
                }else{
                    mProgress.dismiss();
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
                Intent intent = new Intent();
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                } else {
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Selecciona imagen..."),1);
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
                }else{
                    SessionUser.getInstance().user.setImgUser(null);
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
        if(SessionUser.getInstance().user.getImgUser()!=null){
            SessionUser.getInstance().user.setImgUser(imageUri.toString());
        }else{
            SessionUser.getInstance().user.setImgUser(null);
        }
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
            SessionUser.getInstance().firebaseAdmin.addDataUserCouldFirestore();

            //insertUserDataInFirebase(end);
        } else {
            mProgress.dismiss();
            //insertUserDataInFirebase(end);
            endRegister=false;
        }
    }

    @Override
    public void insertUserDataInFirebase(boolean end) {
        if(endRegister==true){
            if(end==true){
                //SessionUser.getInstance().firebaseAdmin.uploadImage(SessionUser.getInstance().user.getImgUser());


                mProgress.dismiss();
                Toast.makeText(getContext(), "Register Completed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), MainMenuActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().finish();

            }else{
                mProgress.dismiss();
                Toast.makeText(getContext(), "Register Fail", Toast.LENGTH_LONG).show();
                errorSingInRegister("Register Fail");
            }
        }else{
            mProgress.dismiss();
            Toast.makeText(getContext(), "Register Fail", Toast.LENGTH_LONG).show();
            errorSingInRegister(getString(R.string.err_register_fail));
        }
    }

    private void errorSingInRegister(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogTheme);
        builder.setMessage(title)
                .setPositiveButton(R.string.info_dialog_err, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RegisterFragment registerFragment = new RegisterFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frameLayout_FirstActivity, registerFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }

    @Override
    public void singInWithEmailAndPassword(boolean end) {
        //Metodo implementado pero no se usa
    }

    @Override
    public void downloadUserDataInFirebase(boolean end) {
    }

    @Override
    public void downloadInfotDeveloper(boolean end) {
        //Metodo implementado pero no se usa
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
