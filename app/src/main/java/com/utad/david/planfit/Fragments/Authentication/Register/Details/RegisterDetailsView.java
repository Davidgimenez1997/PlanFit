package com.utad.david.planfit.Fragments.Authentication.Register.Details;

import android.graphics.Bitmap;

public interface RegisterDetailsView {
    void deviceOffline();
    void setImagenGallery(Bitmap selectedImage);
    void errorGallery();
    void setImagenCamera(Bitmap photo);

    void registerUser();

    void errorRegister();

    void navigateToMainMenu();

    void errorAddUserData();
}
