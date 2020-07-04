package com.utad.david.planfit.DialogFragment.User.EditUserProfiler;

import android.net.Uri;

public interface EditUserProfilerView {
    void deviceOfflineMessage();
    void putDefaultImage();
    void putUserImage(String imgUser);
    void updateButtonImagen(boolean enabled);
    void getImagenGallery(String imagen);
    void getErrorGallery();
    void getImagenCamera(Uri imagen);
    void deletePhoto();
    void errorDeletePhoto();
    void updatePhoto();
    void errorUpdatePhoto();
    void updateNickName();
    void errorUpdateNickName();
    void updateFullName();
    void errorUpdateFullName();
    void deleteUser();
    void errorDeleteUser();
}
