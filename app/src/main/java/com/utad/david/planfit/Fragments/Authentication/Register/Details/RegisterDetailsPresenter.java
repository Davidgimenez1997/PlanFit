package com.utad.david.planfit.Fragments.Authentication.Register.Details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.utad.david.planfit.Data.User.Register.GetRegister;
import com.utad.david.planfit.Data.User.Register.RegisterRepository;
import com.utad.david.planfit.Data.User.SessionUser;
import com.utad.david.planfit.Data.User.User.GetUser;
import com.utad.david.planfit.Data.User.User.UserRepository;
import com.utad.david.planfit.Utils.Utils;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class RegisterDetailsPresenter implements GetRegister, GetUser {

    private RegisterDetailsView view;
    private Uri imagen;
    private String fullName;
    private String nickName;
    private boolean endRegister = false;

    public RegisterDetailsPresenter(RegisterDetailsView view) {
        this.view = view;
        RegisterRepository.getInstance().setGetRegister(this);
        UserRepository.getInstance().setGetUser(this);
    }

    public boolean checkInternetDevice(Context context) {
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
            return true;
        } else {
            this.view.deviceOffline();
            return false;
        }
    }

    public File getFileForImage() {
        File photoFile = null;
        try {
            photoFile = Utils.createImageFile();
        } catch (IOException ex) {
            // TODO: Set error occurred while creating the File
            ex.printStackTrace();
        }
        return photoFile;
    }

    public String getPatchForImage(File file) {
        return file.getAbsolutePath();
    }

    public void getImagenForGallery(Intent data, Context context) {
        try {
            this.imagen = data.getData();
            final InputStream imageStream = context.getApplicationContext().getContentResolver().openInputStream(this.imagen);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            this.view.setImagenGallery(selectedImage);
            if (this.imagen != null) {
                SessionUser.getInstance().setPhoto(this.imagen.toString());
            } else {
                SessionUser.getInstance().setPhoto(null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.view.errorGallery();
        }
    }

    public void getImagenForCamera(String path, Context context) {
        Bitmap photo = Utils.getBitmapFromPath(path);
        this.view.setImagenCamera(photo);
        this.imagen = Utils.getImageUri(context, photo);
        if (this.imagen != null){
            SessionUser.getInstance().setPhoto(this.imagen.toString());
        } else {
            SessionUser.getInstance().setPhoto(null);
        }
    }

    public boolean enableButtonOk(Context context, String nickName, String fullName) {
        this.nickName = nickName;
        this.fullName = fullName;
        if (this.checkInternetDevice(context)) {
            return !checkEditText(nickName, fullName);
        }
        return false;
    }

    private boolean checkEditText(String nickName, String fullName){
        return nickName.isEmpty() || fullName.isEmpty();
    }

    public void setDataUser() {
        if (SessionUser.getInstance().checkImg()) {
            SessionUser.getInstance().setDataUser(this.fullName, this.nickName, this.imagen.toString());
        } else {
            SessionUser.getInstance().setDataUser(this.fullName, this.nickName, null);
        }
    }

    @Override
    public void registerWithEmailAndPassword(boolean status) {
        if (status) {
            this.endRegister = true;
            UserRepository.getInstance().addUserData();
            this.view.registerUser();
        } else {
            this.endRegister = false;
            this.view.errorRegister();
        }
    }

    @Override
    public void addUserData(boolean status) {
        if (this.endRegister) {
            if (status) {
                this.view.navigateToMainMenu();
            } else {
                this.view.errorRegister();
            }
        } else {
            this.view.errorAddUserData();
        }
    }

    @Override
    public void getUserData(boolean status) {}
}
