package com.utad.david.planfit.DialogFragment.User.EditUserProfiler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import com.utad.david.planfit.Data.User.SessionUser;
import com.utad.david.planfit.Data.User.User.EditUser.EditUserRepository;
import com.utad.david.planfit.Data.User.User.EditUser.GetEditUser;
import com.utad.david.planfit.Data.User.User.UserRepository;
import com.utad.david.planfit.Model.User.User;
import com.utad.david.planfit.Utils.Utils;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.io.File;
import java.io.IOException;

public class EditUserProfilerPresenter implements GetEditUser {

    private EditUserProfilerView view;
    private Uri imagen;

    public EditUserProfilerPresenter(EditUserProfilerView view) {
        this.view = view;
        EditUserRepository.getInstance().setGetEditUser(this);
    }

    public boolean checkInternetDevice(Context context) {
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
            return true;
        } else {
            this.view.deviceOfflineMessage();
            return false;
        }
    }

    public User getUser() {
        return SessionUser.getInstance().getUser();
    }

    public void checkImagenUser(String imagen) {
        if (imagen == null) {
            this.view.putDefaultImage();
        }else{
            this.view.putUserImage(imagen);
        }
    }

    public void updateBottonImage(User user) {
        if (user.getImgUser() == null || user.getImgUser().equals("")) {
            this.view.updateButtonImagen(false);
        } else {
            this.view.updateButtonImagen(true);
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

    public void getImagenForGallery(Intent data) {
        try {
            this.imagen = data.getData();
            this.view.getImagenGallery(imagen.toString());
        } catch (Exception e) {
            e.printStackTrace();
            this.view.getErrorGallery();
        }
    }

    public void getImagenForCamera(String photoPath, Context context) {
        Bitmap photo = Utils.getBitmapFromPath(photoPath);
        if (photo != null) {
            Uri tempUri = Utils.getImageUri(context, photo);
            this.view.getImagenCamera(tempUri);
        }
    }

    public boolean fullNameValidate(String fullName, User user) {
        return !fullName.equals(user.getFullName());
    }

    public boolean nickNameValidate(String nickName, User user){
        return !nickName.equals(user.getNickName());
    }

    public void clickDeletePhoto(User userUpdate) {
        UserRepository.getInstance().setUser(userUpdate);
        EditUserRepository.getInstance().deletePhoto();
    }

    public void clickUpdatePhoto(User userUpdate) {
        UserRepository.getInstance().setUser(userUpdate);
        EditUserRepository.getInstance().updatePhoto();
    }

    public void clickUpdateFullName(User userUpdate) {
        UserRepository.getInstance().setUser(userUpdate);
        EditUserRepository.getInstance().updateFullName();
    }

    public void clickUpdateNickName(User userUpdate) {
        UserRepository.getInstance().setUser(userUpdate);
        EditUserRepository.getInstance().updateNickName();
    }

    public void clickDeleteUser() {
        EditUserRepository.getInstance().deleteAccount();
    }

    @Override
    public void deletePhoto(boolean status) {
        if (status) {
            SessionUser.getInstance().deletePhoto();
            this.view.deletePhoto();

        } else {
            this.view.errorDeletePhoto();
        }
    }

    @Override
    public void updatePhoto(boolean status) {
        if (status) {
            this.view.updatePhoto();
        } else {
            this.view.errorUpdatePhoto();
        }
    }

    @Override
    public void updateNickName(boolean status) {
        if (status) {
            this.view.updateNickName();
        } else {
            this.view.errorUpdateNickName();
        }
    }

    @Override
    public void updateFullName(boolean status) {
        if (status) {
            this.view.updateFullName();
        } else {
            this.view.errorUpdateFullName();
        }
    }

    @Override
    public void deleteUser(boolean status) {
        if (status) {
            this.view.deleteUser();
        } else {
            this.view.errorDeleteUser();
        }
    }
}
