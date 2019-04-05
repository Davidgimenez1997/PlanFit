package com.utad.david.planfit.DialogFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Activitys.FirstActivity;
import com.utad.david.planfit.Data.EncryptDecrypt;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.User;
import com.utad.david.planfit.R;
import io.fabric.sdk.android.Fabric;

import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class EditPersonalDataUser extends DialogFragment implements FirebaseAdmin.FirebaseAdminUpdateAndDeleteUserListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(getContext(), new Crashlytics());

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
    private Button buttonClose;
    private User userUpdate;
    private String oldPassword;
    private EditPersonalDataUser.OnFragmentInteractionListener mListener;
    private ProgressDialog mProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_edit_user_data, container, false);
        v.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        userUpdate = SessionUser.getInstance().firebaseAdmin.userDataFirebase;
        if(userUpdate!=null){
            oldPassword = userUpdate.getPassword();
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
            onClickClose();
            configView();
        }
        return v;
    }

    private void onClickClose() {
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void configView(){
        buttonUpdatePhoto.setEnabled(false);
        editTextEmail.addTextChangedListener(textWatcherEditPesonalDataEmail);
        editTextPassword.addTextChangedListener(textWatcherEditPesonalDataPassword);
        editTextFullName.addTextChangedListener(textWatcherEditPesonalDataFullName);
        editTextNickName.addTextChangedListener(textWatcherEditPesonalDataNickName);
    }

    private void checkImageUser(){
        if(userUpdate.getImgUser()==null || userUpdate.getImgUser().equals("")){
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
                startActivityForResult(Intent.createChooser(intent, getString(R.string.menuopengalery)),1);
            }
        });
    }

    private TextWatcher textWatcherEditPesonalDataEmail = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

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
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

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
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

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
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

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
        buttonClose = v.findViewById(R.id.button_close_info);
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
        if(user.getImgUser()==null){
            imageView.setImageResource(R.drawable.icon_gallery);
        }else{
            putPhotoUser(user.getImgUser());
        }
    }

    public void putPhotoUser(String stringUri) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.icon_user);
        Glide.with(this).setDefaultRequestOptions(requestOptions).load(stringUri).into(imageView);
    }

    private void onClickButtonDeletePhoto(){
        buttonDeletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(getString(R.string.title_delete_boto),getString(R.string.message_delete_photo));
                mProgress.show();
                SessionUser.getInstance().firebaseAdmin.userDataFirebase = userUpdate;
                SessionUser.getInstance().firebaseAdmin.deletePhoto();
            }
        });
    }

    private void onClickButtonUpdatePhoto(){
        buttonUpdatePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(getString(R.string.title_update_foto),getString(R.string.message_update_photo));
                mProgress.show();
                SessionUser.getInstance().firebaseAdmin.userDataFirebase = userUpdate;
                SessionUser.getInstance().firebaseAdmin.updatePhotoUserInFirebase();
            }
        });
    }

    private void onClickButtonUpdatePassword(){
        buttonUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(getString(R.string.title_update_pass),getString(R.string.message_update_pass));
                mProgress.show();
                try {
                    String pass = EncryptDecrypt.encrypt(userUpdate.getPassword());
                    userUpdate.setPassword(pass);
                    SessionUser.getInstance().firebaseAdmin.userDataFirebase = userUpdate;
                    SessionUser.getInstance().firebaseAdmin.updatePasswordUserInFirebase(oldPassword);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void onClickButtonUpdateEmail(){
        buttonUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(getString(R.string.title_update_email),getString(R.string.message_update_email));
                mProgress.show();
                SessionUser.getInstance().firebaseAdmin.userDataFirebase = userUpdate;
                SessionUser.getInstance().firebaseAdmin.updateEmailUserInFirebase();
            }
        });
    }

    private void onClickButtonUpdateFullName(){
        buttonUpdateFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(getString(R.string.title_update_name),getString(R.string.message_update_name));
                mProgress.show();
                SessionUser.getInstance().firebaseAdmin.userDataFirebase = userUpdate;
                SessionUser.getInstance().firebaseAdmin.updateFullNameUserInFirebase();
            }
        });
    }

    private void onClickButtonUpdateNickName(){
        buttonUpdateNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(getString(R.string.title_update_nick),getString(R.string.message_update_nick));
                mProgress.show();
                SessionUser.getInstance().firebaseAdmin.userDataFirebase = userUpdate;
                SessionUser.getInstance().firebaseAdmin.updateNickNameUserInFirebase();
            }
        });
    }

    private void onClickButtonDeleteAccount(){
        buttonDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogDeleteUser(getString(R.string.info_delete_acount_1)+" "+userUpdate.getNickName()+
                        getString(R.string.info_delete_acount_2));
            }
        });
    }

    private void createAndShowAlertDialogDeleteUser(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(message)
                .setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showDialog(getString(R.string.title_delete_user),getString(R.string.message_delete_user));
                        mProgress.show();
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

    @Override
    public void updatePhotoInFirebase(boolean end) {
        if(end){
            if(mListener!=null){
                mProgress.dismiss();
                buttonUpdatePhoto.setEnabled(false);
                putPhotoUser(userUpdate.getImgUser());
                mListener.updateData(userUpdate);
                Toast.makeText(getContext(),getString(R.string.info_update_photo),Toast.LENGTH_LONG).show();
            }
        }
        else{
            mProgress.dismiss();
            errorUpdatePhoto(getString(R.string.error_update_photo));
            mListener.updateData(userUpdate);
        }
    }

    private void errorUpdatePhoto(String messageError) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(messageError)
                .setPositiveButton(R.string.info_dialog_err, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public void deletePhotoInFirebase(boolean end) {
        if(end){
            if(mListener!=null){
                mProgress.dismiss();
                imageView.setImageResource(R.drawable.icon_gallery);
                SessionUser.getInstance().user.setImgUser(null);
                userUpdate.setImgUser(null);
                mListener.updateData(userUpdate);
                Toast.makeText(getContext(),getString(R.string.delete_photo_info),Toast.LENGTH_LONG).show();
            }
        }else{
            mProgress.dismiss();
            errorDeletePhoto(getString(R.string.error_delete_photo));
        }
    }

    private void errorDeletePhoto(String messageError) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(messageError)
                .setPositiveButton(R.string.info_dialog_err, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public void updateEmailInFirebase(boolean end) {
        if(end){
            if(mListener!=null){
                buttonUpdateEmail.setEnabled(false);
                mProgress.dismiss();
                mListener.updateData(userUpdate);
                Toast.makeText(getContext(),getString(R.string.info_update_email),Toast.LENGTH_LONG).show();
            }
        }else{
            mProgress.dismiss();
            errorUpdateEmail(getString(R.string.error_email));

        }
    }

    private void errorUpdateEmail(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(message)
                .setPositiveButton(R.string.info_dialog_err, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public void updatePasswordInFirebase(boolean end) {
        if(end){
            buttonUpdatePassword.setEnabled(false);
            mProgress.dismiss();
            Toast.makeText(getContext(),getString(R.string.info_update_password),Toast.LENGTH_LONG).show();
        }else{
            mProgress.dismiss();
            errorUpdatePassword(getString(R.string.error_password));
        }
    }

    private void errorUpdatePassword(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(message)
                .setPositiveButton(R.string.info_dialog_err, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public void updateNickNameInFirebase(boolean end) {
        if(end){
            if(mListener!=null){
                mProgress.dismiss();
                buttonUpdateNickName.setEnabled(false);
                editTextNickName.setText(userUpdate.getNickName());
                mListener.updateData(userUpdate);
                Toast.makeText(getContext(),getString(R.string.info_update_nickname),Toast.LENGTH_LONG).show();
            }
        }else{
            mProgress.dismiss();
            errorUpdateNickName(getString(R.string.error_nick));
        }
    }

    private void errorUpdateNickName(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(message)
                .setPositiveButton(R.string.info_dialog_err, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public void updateFullNameInFirebase(boolean end) {
        if(end){
            if(mListener!=null){
                mProgress.dismiss();
                buttonUpdateFullName.setEnabled(false);
                editTextFullName.setText(userUpdate.getFullName());
                mListener.updateData(userUpdate);
                Toast.makeText(getContext(),getString(R.string.info_update_name),Toast.LENGTH_LONG).show();
            }
        }else{
            mProgress.dismiss();
            errorUpdateName(getString(R.string.error_name));
        }
    }

    private void errorUpdateName(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(message)
                .setPositiveButton(R.string.info_dialog_err, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public void deleteUserInFirebase(boolean end) {
        if(end){
            mProgress.dismiss();
            Toast.makeText(getContext(),getString(R.string.info_delete_user),Toast.LENGTH_LONG).show();
            navigatedUserLoginRegister();
        }else{
            mProgress.dismiss();
            errorDeleteAccunt(getString(R.string.error_delete_user));
        }
    }

    private void errorDeleteAccunt(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setMessage(message)
                .setPositiveButton(R.string.info_dialog_err, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;
        if (resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                putPhotoUser(imageUri.toString());
                buttonUpdatePhoto.setEnabled(true);
                if(imageUri!=null){
                   userUpdate.setImgUser(imageUri.toString());
                   onClickButtonUpdatePhoto();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_gallery_1), Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_gallery_2),Toast.LENGTH_LONG).show();
        }
    }

    private void showDialog(String title, String message){
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle(title);
        mProgress.setMessage(message);
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
    }

    private void navigatedUserLoginRegister(){
        Intent intent =new Intent(getContext(),FirstActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditPersonalDataUser.OnFragmentInteractionListener) {
            mListener = (EditPersonalDataUser.OnFragmentInteractionListener) context;
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
        void  updateData(User user);
    }
}
