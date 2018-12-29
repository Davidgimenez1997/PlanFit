package com.utad.david.planfit.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.utad.david.planfit.Activitys.FirstActivity;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.User;
import com.utad.david.planfit.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class EditPersonalDataUser extends DialogFragment implements FirebaseAdmin.FirebaseAdminUpdateAndDeleteUserListener {

    //Nuestra variable communities coge el valor que se le está pasando
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminUpdateUserListener(this);
    }

    private ImageView imageView;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextFullName;
    private EditText editTextNickName;
    private Button buttonUpdateEmail;
    private Button buttonUpdatePassword;
    private Button buttonUpdateNickName;
    private Button buttonUpdateFullName;
    private Button buttonDeletePhoto;
    private Button buttonUpdatePhoto;
    private Button buttonDeleteAccount;
    private User userUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_edit_user_data, container, false);
        v.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        userUpdate = SessionUser.getInstance().firebaseAdmin.userDataFirebase;

        findById(v);
        putData();
        checkImageUser();
        openGallery();
        onClickButtonDeletePhoto();
        onClickButtonUpdateEmail();
        onClickButtonUpdatePassword();
        onClickButtonUpdateNickName();
        onClickButtonUpdateFullName();
        onClickButtonDeleteAccount();
        configView();

        return v;
    }

    private void configView(){
        editTextEmail.addTextChangedListener(textWatcherEditPesonalDataEmail);
        editTextPassword.addTextChangedListener(textWatcherEditPesonalDataPassword);
        editTextFullName.addTextChangedListener(textWatcherEditPesonalDataFullName);
        editTextNickName.addTextChangedListener(textWatcherEditPesonalDataNickName);
    }

    private void checkImageUser(){
        if(userUpdate.getImgUser()!=null || !userUpdate.getImgUser().equals("")){
            buttonUpdatePhoto.setEnabled(false);
        }else{
            onClickButtonUpdatePhoto();
            buttonUpdatePhoto.setEnabled(true);
        }
    }

    private void openGallery(){
        imageView.setOnClickListener(new View.OnClickListener() {
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

    private TextWatcher textWatcherEditPesonalDataEmail = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String email = editTextEmail.getText().toString().trim();

            if (emailValidate(email)) {
                buttonUpdateEmail.setEnabled(true);
                userUpdate.setEmail(email);
            }else{
                editTextEmail.setError(getString(R.string.err_email));
            }

        }
    };

    private TextWatcher textWatcherEditPesonalDataPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String password = editTextPassword.getText().toString().trim();

            if (passwordValidate(password)) {
                buttonUpdatePassword.setEnabled(true);
                userUpdate.setPassword(password);
            }else{
                editTextPassword.setError(getString(R.string.err_password));
            }
        }
    };

    private TextWatcher textWatcherEditPesonalDataFullName = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String fullName = editTextFullName.getText().toString().trim();

            if(fullNameValidate(fullName)){
                buttonUpdateFullName.setEnabled(true);
                userUpdate.setFullName(fullName);
            }
        }
    };

    private TextWatcher textWatcherEditPesonalDataNickName = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String nickName = editTextNickName.getText().toString().trim();
            if(nickNameValidate(nickName)){
                buttonUpdateNickName.setEnabled(true);
                userUpdate.setNickName(nickName);
            }
        }
    };

    private boolean emailValidate(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (pattern.matcher(email).matches()) {
            return true;
        } else {
            return false;

        }
    }

    public boolean passwordValidate(String password) {
        if (password.length() >= 6) {
            return true;
        } else {
            return false;
        }
    }

    public boolean fullNameValidate(String fullName){
        if(fullName.equals(userUpdate.getFullName())){
            return false;
        }else{
            return true;
        }
    }

    public boolean nickNameValidate(String nickName){
        if(nickName.equals(userUpdate.getNickName())){
            return false;
        }else{
            return true;
        }
    }

    private void findById(View v){
        imageView = v.findViewById(R.id.imageViewEditUser);
        editTextEmail = v.findViewById(R.id.nickEmailEditUser);
        editTextFullName = v.findViewById(R.id.fullNameEditUser);
        editTextNickName  = v.findViewById(R.id.nickNameEditUser);
        editTextPassword  = v.findViewById(R.id.passwordEditUser);
        buttonUpdateEmail = v.findViewById(R.id.update_emial_button);
        buttonDeletePhoto = v.findViewById(R.id.button_delete_photo);
        buttonUpdateFullName = v.findViewById(R.id.button_update_fullname);
        buttonUpdateNickName = v.findViewById(R.id.button_nick_update);
        buttonUpdatePassword = v.findViewById(R.id.button_password_update);
        buttonUpdatePhoto = v.findViewById(R.id.button_update_photo);
        buttonDeleteAccount = v.findViewById(R.id.button_delete_account);
    }

    private void putData(){
        User user = SessionUser.getInstance().firebaseAdmin.userDataFirebase;
        if(user !=null){
            editTextEmail.setText(user.getEmail());
            editTextPassword.setText(user.getPassword());
            editTextNickName.setText(user.getNickName());
            editTextFullName.setText(user.getFullName());
            checkAndPhotoUser(user);
        }
    }

    private void checkAndPhotoUser(User user){
        if(user.getImgUser().equals("")){
            imageView.setImageResource(R.drawable.icon_gallery);
        }else{
            putPhotoUser(user.getImgUser());
        }
    }

    public void putPhotoUser(String stringUri) {
        Uri uri = Uri.parse(stringUri);
        final InputStream imageStream;
        try {
            imageStream = getActivity().getContentResolver().openInputStream(uri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            RoundedBitmapDrawable roundedDrawable1 =
                    RoundedBitmapDrawableFactory.create(getResources(), selectedImage);

            //asignamos el CornerRadius
            roundedDrawable1.setCornerRadius(selectedImage.getHeight());
            imageView.setImageDrawable(roundedDrawable1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void onClickButtonDeletePhoto(){
        buttonDeletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.icon_gallery);
                if(!userUpdate.getImgUser().equals("")){
                    buttonUpdatePhoto.setEnabled(true);
                    userUpdate.setImgUser("");
                    onClickButtonUpdatePhoto();
                }else{
                    buttonUpdatePhoto.setEnabled(false);
                }
            }
        });
    }

    private void onClickButtonUpdatePhoto(){
        buttonUpdatePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionUser.getInstance().firebaseAdmin.userDataFirebase = userUpdate;
                SessionUser.getInstance().firebaseAdmin.updatePhotoUserInFirebase();
            }
        });
    }

    private void onClickButtonUpdatePassword(){
        buttonUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionUser.getInstance().firebaseAdmin.userDataFirebase = userUpdate;
                SessionUser.getInstance().firebaseAdmin.updatePasswordUserInFirebase();
            }
        });
    }

    private void onClickButtonUpdateEmail(){
        buttonUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionUser.getInstance().firebaseAdmin.userDataFirebase = userUpdate;
                SessionUser.getInstance().firebaseAdmin.updateEmailUserInFirebase();
            }
        });
    }

    private void onClickButtonUpdateFullName(){
        buttonUpdateFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionUser.getInstance().firebaseAdmin.userDataFirebase = userUpdate;
                SessionUser.getInstance().firebaseAdmin.updateFullNameUserInFirebase();
            }
        });
    }

    private void onClickButtonUpdateNickName(){
        buttonUpdateNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionUser.getInstance().firebaseAdmin.userDataFirebase = userUpdate;
                SessionUser.getInstance().firebaseAdmin.updateNickNameUserInFirebase();
            }
        });
    }

    private void onClickButtonDeleteAccount(){
        buttonDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogUpdateDeleteUser(getString(R.string.info_delete_acount_1)+" "+userUpdate.getNickName()+
                        getString(R.string.info_delete_acount_2));
            }
        });
    }

    @Override
    public void updatePhotoInFirebase(boolean end) {
        if(end){
            buttonUpdatePhoto.setEnabled(false);
        }
    }

    @Override
    public void updateEmailInFirebase(boolean end) {
        if(end==true){
            createAndShowAlertDialogUpdate(getString(R.string.info_update_email));
        }
    }

    @Override
    public void updatePasswordInFirebase(boolean end) {
        if(end==true){
            createAndShowAlertDialogUpdate(getString(R.string.info_update_password));
        }
    }

    @Override
    public void updateNickNameInFirebase(boolean end) {
        if(end){
            buttonUpdateNickName.setEnabled(false);
            createAndShowAlertDialogUpdateFullNameOrNickName(getString(R.string.info_update_nickname));
            editTextNickName.setText(userUpdate.getNickName());
        }
    }

    @Override
    public void updateFullNameInFirebase(boolean end) {
        if(end){
            buttonUpdateFullName.setEnabled(false);
            createAndShowAlertDialogUpdateFullNameOrNickName(getString(R.string.info_update_fullname));
            editTextFullName.setText(userUpdate.getFullName());
        }
    }

    @Override
    public void deleteUserInFirebase(boolean end) {
        if(end==true){
            navigatedUserLoginRegister();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;
        if (resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                final InputStream imageStream = getActivity().getApplicationContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
                buttonUpdatePhoto.setEnabled(true);
                if(imageUri!=null){
                   SessionUser.getInstance().firebaseAdmin.userDataFirebase.setImgUser(imageUri.toString());
                   onClickButtonUpdatePhoto();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getActivity().getApplicationContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void createAndShowAlertDialogUpdate(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(message)
                .setPositiveButton(R.string.info_dialog_err, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        navigatedUserLoginRegister();
                    }
                });
        builder.create();
        builder.show();
    }

    private void createAndShowAlertDialogUpdateFullNameOrNickName(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(message)
                .setPositiveButton(R.string.info_dialog_err, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        navigatedUserLoginRegister();
                    }
                });
        builder.create();
        builder.show();
    }

    private void createAndShowAlertDialogUpdateDeleteUser(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(message)
                .setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SessionUser.getInstance().firebaseAdmin.deleteAccountInFirebase();
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
        });
        builder.create();
        builder.show();
    }

    private void navigatedUserLoginRegister(){
        Intent intent =new Intent(getContext(),FirstActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }
}